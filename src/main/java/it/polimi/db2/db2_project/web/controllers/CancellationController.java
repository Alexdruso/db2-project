package it.polimi.db2.db2_project.web.controllers;

import it.polimi.db2.db2_project.entities.QuestionnaireEntity;
import it.polimi.db2.db2_project.entities.UserEntity;
import it.polimi.db2.db2_project.services.SubmissionService;
import it.polimi.db2.db2_project.web.TemplatingServlet;
import org.thymeleaf.templatemode.TemplateMode;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
        //check user logged in
        HttpSession session = request.getSession();
        UserEntity user = (UserEntity) session.getAttribute("user");

        if (session.isNew() || user == null) {
            String path = getServletContext().getContextPath() + "/login";
            response.sendRedirect(path);
            return;
        }

        Optional<QuestionnaireEntity> questionnaireOptional = submissionService.findCurrentQuestionnaire();

        questionnaireOptional.ifPresent(
                questionnaire -> submissionService.cancelQuestionnaireSubmission(user.getId(), questionnaire.getId())
        );

        String path = getServletContext().getContextPath() + "/welcome";
        response.sendRedirect(path);
    }
}
