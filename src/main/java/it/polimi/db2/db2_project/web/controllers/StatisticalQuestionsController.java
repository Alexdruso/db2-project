package it.polimi.db2.db2_project.web.controllers;

import it.polimi.db2.db2_project.entities.QuestionEntity;
import it.polimi.db2.db2_project.entities.QuestionnaireEntity;
import it.polimi.db2.db2_project.services.SubmissionService;
import it.polimi.db2.db2_project.web.TemplatingServlet;
import org.thymeleaf.templatemode.TemplateMode;

import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.HashMap;

@WebServlet(name = "StatisticalQuestionsController", value = "/StatisticalQuestionsController")
public class StatisticalQuestionsController extends TemplatingServlet {

    @EJB
    SubmissionService submissionService;

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
                () -> {
                    context.put(
                            "available",
                            false
                    );
                }
        );
        super.processTemplate(request, response, context);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("POST invoked");

        String path = getServletContext().getContextPath() + "/congratulations";

        System.out.println(path);
        response.sendRedirect(path);
    }
}
