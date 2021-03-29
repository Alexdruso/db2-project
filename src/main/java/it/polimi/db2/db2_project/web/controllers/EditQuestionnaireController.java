package it.polimi.db2.db2_project.web.controllers;

import it.polimi.db2.db2_project.entities.QuestionEntity;
import it.polimi.db2.db2_project.entities.QuestionnaireEntity;
import it.polimi.db2.db2_project.entities.UserEntity;
import it.polimi.db2.db2_project.services.AdminService;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet(name = "EditQuestionnaireController", value = "/EditQuestionnaireController")
public class EditQuestionnaireController extends TemplatingServlet {

    @EJB
    private AdminService adminService;

    public EditQuestionnaireController() {
        super("edit-questionnaire", TemplateMode.HTML, "WEB-INF/templates/", ".html");
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

        Optional<QuestionnaireEntity> questionnaire = SessionUtil.getQuestionnaire(adminService, request, "id");

        context.put("isAdmin", user.get().getAdmin());
        context.put("questionnaire", questionnaire.orElse(null));

        if (questionnaire.isPresent()) {
            List<QuestionEntity> mandatoryQuestions = questionnaire.get().getQuestions().stream()
                    .filter(q -> !q.getOptional())
                    .collect(Collectors.toList());

            context.put("questions", mandatoryQuestions);
        }

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

        if (!user.get().getAdmin()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid permissions.");
            return;
        }

        Optional<QuestionnaireEntity> questionnaire = SessionUtil.getQuestionnaire(adminService, request, "id");

        if (questionnaire.isPresent()) {
            if (request.getParameter("add") != null) {
                addPlaceholderQuestion(questionnaire.get());
            } else if (request.getParameter("update") != null) {
                updateQuestions(questionnaire.get(), request);
            }

            String redirect = String.format("%s/edit-questionnaire?id=%s",
                    getServletContext().getContextPath(), questionnaire.get().getId());

            response.sendRedirect(redirect);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid questionnaire.");
        }
    }

    private void updateQuestions(QuestionnaireEntity questionnaire, HttpServletRequest request) {
        questionnaire.getQuestions().stream()
                .map(QuestionEntity::getId)
                .filter(id -> request.getParameter(id.toString()) != null)
                .forEach(id -> {
                    String newText = request.getParameter(id.toString());
                    if (!newText.isEmpty()) {
                        adminService.updateQuestion(id, newText);
                    } else {
                        adminService.removeQuestion(id);
                    }
                });
    }

    private void addPlaceholderQuestion(QuestionnaireEntity questionnaire) {
        adminService.addMarketingQuestion(questionnaire.getId(), "<Placeholder Question>");
    }
}
