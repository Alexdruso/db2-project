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
        Optional<UserEntity> user = SessionUtil.checkLogin(request);

        if (user.isEmpty()) {
            return;
        }

        String path;

        if(request.getParameter("yes") != null) {
            Optional<QuestionnaireEntity> questionnaire = submissionService.findCurrentQuestionnaire();

            //insert an empty questionnaire submission such that the cancellation is recorded
            questionnaire.ifPresent(
                    q -> submissionService.createQuestionnaireSubmission(user.get().getId(), q.getId())
            );

            path = getServletContext().getContextPath() + "/homepage";
        } else {
            path = getServletContext().getContextPath() + "/statistical-questions";
        }

        response.sendRedirect(path);
    }
}
