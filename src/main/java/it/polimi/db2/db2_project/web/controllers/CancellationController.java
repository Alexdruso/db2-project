package it.polimi.db2.db2_project.web.controllers;

import it.polimi.db2.db2_project.entities.QuestionnaireEntity;
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
import java.util.Optional;

@WebServlet(name = "CancellationController", value = "/CancellationController")
public class CancellationController extends TemplatingServlet {

    @EJB
    SubmissionService submissionService;

    public CancellationController() {
        super("cancellation", TemplateMode.HTML, "WEB-INF/templates/", ".html");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HashMap<String, Object> ctx = new HashMap<>();

        super.processTemplate(request, response, ctx);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Optional<UserEntity> userOpt = SessionUtil.checkLogin(request);
        if(userOpt.isEmpty()) return;
        UserEntity user = userOpt.get();

        Optional<QuestionnaireEntity> questionnaireOptional = submissionService.findCurrentQuestionnaire();

        questionnaireOptional.ifPresent(
                questionnaire -> submissionService.cancelQuestionnaireSubmission(user.getId(), questionnaire.getId())
        );

        String path = getServletContext().getContextPath() + "/homepage";
        response.sendRedirect(path);
    }
}
