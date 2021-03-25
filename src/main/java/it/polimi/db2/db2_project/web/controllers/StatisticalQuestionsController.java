package it.polimi.db2.db2_project.web.controllers;

import it.polimi.db2.db2_project.entities.QuestionEntity;
import it.polimi.db2.db2_project.entities.QuestionnaireEntity;
import it.polimi.db2.db2_project.entities.QuestionnaireSubmissionEntity;
import it.polimi.db2.db2_project.entities.UserEntity;
import it.polimi.db2.db2_project.services.SubmissionService;
import it.polimi.db2.db2_project.services.UserService;
import it.polimi.db2.db2_project.web.TemplatingServlet;
import it.polimi.db2.db2_project.web.utils.SessionUtil;
import org.thymeleaf.templatemode.TemplateMode;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@WebServlet(name = "StatisticalQuestionsController", value = "/StatisticalQuestionsController")
public class StatisticalQuestionsController extends TemplatingServlet {

    @EJB
    SubmissionService submissionService;
    @EJB
    UserService userService;

    public StatisticalQuestionsController() {
        super("statistical-questions", TemplateMode.HTML, "WEB-INF/templates/", ".html");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HashMap<String, Object> context = new HashMap<>();
        submissionService.findCurrentQuestionnaire().ifPresentOrElse(
                questionnaire -> {
                    context.put(
                            "statisticalQuestions",
                            submissionService.findStatisticalQuestions(questionnaire.getId())
                    );
                    context.put(
                            "available",
                            true
                    );
                },
                () -> context.put(
                        "available",
                        false
                )
        );
        super.processTemplate(request, response, context);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Optional<UserEntity> userOpt = SessionUtil.checkLogin(request);

        if (userOpt.isEmpty()) {
            String path = getServletContext().getContextPath() + "/login";
            response.sendRedirect(path);
            return;
        }

        UserEntity user = userOpt.get();

        if (user.getBan()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "You have been banned.");
            return;
        }

        Optional<QuestionnaireEntity> questionnaire = submissionService.findCurrentQuestionnaire();

        if (questionnaire.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "There is no questionnaire today");
            return;
        }

        //check already answered to marketing questions
        //and consequently all other checks have been performed in the MarketingQuestionsController
        Map<Long, String> answers = SessionUtil.getAnswersFromSession(request);

        if (answers.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "You have yet to answer the marketing questions");
            return;
        }

        //insert answers in the answers structure

        for (QuestionEntity question : submissionService.findStatisticalQuestions(questionnaire.get().getId())) {
            String answer = request.getParameter(question.getId().toString());

            if (answer != null && !answer.isEmpty()) answers.put(question.getId(), answer);
        }

        //check no offensive words

        if (submissionService.checkOffensiveWords(new ArrayList<>(answers.values()))) {
            userService.banUser(user.getId());

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Your answer contained some bad words, you are therefore banned"
            );
            return;
        }

        QuestionnaireSubmissionEntity questionnaireSubmission = submissionService
                .createQuestionnaireSubmission(user.getId(), questionnaire.get().getId());

        submissionService.submitAnswers(questionnaireSubmission.getId(), answers);

        String path = getServletContext().getContextPath() + "/congratulations";
        response.sendRedirect(path);
    }
}
