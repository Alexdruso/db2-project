package it.polimi.db2.db2_project.web.controllers;

import it.polimi.db2.db2_project.entities.QuestionEntity;
import it.polimi.db2.db2_project.entities.QuestionnaireEntity;
import it.polimi.db2.db2_project.entities.QuestionnaireSubmissionEntity;
import it.polimi.db2.db2_project.entities.UserEntity;
import it.polimi.db2.db2_project.services.SubmissionService;
import it.polimi.db2.db2_project.web.TemplatingServlet;
import it.polimi.db2.db2_project.web.utils.SessionUtil;
import org.thymeleaf.templatemode.TemplateMode;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@WebServlet(name = "MarketingQuestionsController", value = "/MarketingQuestionsController")
public class MarketingQuestionsController extends TemplatingServlet {

    @EJB
    private SubmissionService submissionService;

    public MarketingQuestionsController() {
        super("marketing-questions", TemplateMode.HTML, "WEB-INF/templates/", ".html");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HashMap<String, Object> context = new HashMap<>();

        Optional<UserEntity> user = SessionUtil.checkLogin(request);

        if (user.isEmpty()) {
            String path = getServletContext().getContextPath() + "/login";
            response.sendRedirect(path);
            return;
        }

        context.put("missing", request.getParameter("missing") != null);

        Map<Long, String> answers = SessionUtil.getAnswersFromSession(request);

        Optional<QuestionnaireEntity> questionnaire = submissionService.findCurrentQuestionnaire();

        if (questionnaire.isPresent()) {
            context.put("marketingQuestions", submissionService.findMarketingQuestions(questionnaire.get().getId()));

            if (!answers.isEmpty()) {
                context.put("marketingAnswers", answers);
            }
        }

        context.put("available", questionnaire.isPresent());

        super.processTemplate(request, response, context);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Optional<UserEntity> user = SessionUtil.checkLogin(request);

        if (user.isEmpty()) {
            String path = getServletContext().getContextPath() + "/login";
            response.sendRedirect(path);
            return;
        }

        if (user.get().getBan()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "You have been banned.");
            return;
        }

        Optional<QuestionnaireEntity> questionnaire = submissionService.findCurrentQuestionnaire();

        if (questionnaire.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "There is no questionnaire today");
            return;
        }

        Optional<QuestionnaireSubmissionEntity> questionnaireSubmission = submissionService.findQuestionnaireSubmission(
                user.get().getId(),
                questionnaire.get().getId()
        );

        if (questionnaireSubmission.isPresent() && questionnaireSubmission.get().getPoints() > 0) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "You have already answered to this questionnaire");
            return;
        }

        //insert statistical questions

        Map<Long, String> answers = new HashMap<>();
        boolean missing = false;
        for (QuestionEntity question : submissionService.findMarketingQuestions(questionnaire.get().getId())) {
            String answer = request.getParameter(question.getId().toString());

            if (answer != null && !answer.isEmpty()) {
                answers.put(question.getId(), answer);
            } else {
                missing = true;
            }
        }

        if (missing) {
            String path = getServletContext().getContextPath() + "/marketing-questions?missing";
            response.sendRedirect(path);
            return;
        }

        SessionUtil.saveAnswersToSession(request, answers);

        String path = getServletContext().getContextPath() + "/statistical-questions";
        response.sendRedirect(path);
    }
}
