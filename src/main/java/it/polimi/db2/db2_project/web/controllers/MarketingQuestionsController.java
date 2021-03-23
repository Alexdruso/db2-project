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
import javax.websocket.Session;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static it.polimi.db2.db2_project.web.utils.SessionUtil.getAnswers;


@WebServlet(name = "MarketingQuestionsController", value = "/MarketingQuestionsController")
public class MarketingQuestionsController extends TemplatingServlet {

    @EJB
    private UserService userService;

    @EJB
    private SubmissionService submissionService;

    public MarketingQuestionsController() {
        super("marketing-questions", TemplateMode.HTML, "WEB-INF/templates/", ".html");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HashMap<String, Object> context = new HashMap<>();

        Optional<UserEntity> userOpt = SessionUtil.checkLogin(request);
        if(userOpt.isEmpty()) return;
        UserEntity user = userOpt.get();
        if(request.getParameter("missing") != null) {
            context.put("missing", true);
        }

        Map<Long, String> answers = SessionUtil.getAnswers(request);


        submissionService.findCurrentQuestionnaire().ifPresentOrElse(
                questionnaire -> {
                    context.put(
                            "marketingQuestions",
                            submissionService.findMarketingQuestions(questionnaire.getId())
                    );
                    if(!answers.isEmpty()) {
                        context.put("marketingAnswers", answers);
                    }
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
        if(userOpt.isEmpty()) return;
        UserEntity user = userOpt.get();

        Optional<QuestionnaireEntity> questionnaire = submissionService.findCurrentQuestionnaire();

        if(questionnaire.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "There is no questionnaire today");
            return;
        }

        Optional<QuestionnaireSubmissionEntity> questionnaireSubmission = submissionService.findQuestionnaireSubmission(
                user.getId(),
                questionnaire.get().getId()
        );


        if(questionnaireSubmission.isPresent() && questionnaireSubmission.get().getPoints() > 0) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "You have already answered to this questionnaire");
            return;
        }

        //insert statistical questions

        Map<Long, String> answers = new HashMap<>();
        boolean missing = false;
        for(QuestionEntity question : submissionService.findMarketingQuestions(questionnaire.get().getId())){
            String answer = request.getParameter(question.getId().toString());

            if(answer != null && !answer.isEmpty()) {
                answers.put(question.getId(), answer);
            } else {
                missing = true;
            }
        }
        SessionUtil.putAnswers(request, answers);
        if(missing) {
            String path = getServletContext().getContextPath() + "/marketing-questions?missing";
            response.sendRedirect(path);
            return;
        }


        String path = getServletContext().getContextPath() + "/statistical-questions";
        response.sendRedirect(path);
    }
}
