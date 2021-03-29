package it.polimi.db2.db2_project.web.controllers;

import it.polimi.db2.db2_project.entities.AnswerEntity;
import it.polimi.db2.db2_project.entities.QuestionnaireEntity;
import it.polimi.db2.db2_project.entities.UserEntity;
import it.polimi.db2.db2_project.services.AdminService;
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
import java.util.*;

@WebServlet(name = "InspectionPageController", value = "/InspectionPageController")
public class InspectionPageController extends TemplatingServlet {

    @EJB
    private AdminService adminService;

    @EJB
    private UserService userService;

    public InspectionPageController() {
        super("inspection-page", TemplateMode.HTML, "WEB-INF/templates/", ".html");
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

        Optional<QuestionnaireEntity> questionnaire = SessionUtil.getQuestionnaire(adminService, request, "questionnaire");
        Optional<UserEntity> selectedUser = getUserId(request);

        context.put("questionnaire", questionnaire.orElse(null));
        context.put("user", selectedUser.orElse(null));

        if (questionnaire.isPresent()) {
            if (selectedUser.isPresent()) {
                List<AnswerEntity> answers = adminService.getAllAnswersByUser(selectedUser.get().getId(), questionnaire.get().getId());

                context.put("answers", answers);
            } else {
                List<UserEntity> submitters = adminService.getAllSubmitters(questionnaire.get().getId());
                List<UserEntity> cancellations = adminService.getAllCancellations(questionnaire.get().getId());

                context.put("submissions", submitters);
                context.put("cancellations", cancellations);
            }
        } else {
            List<QuestionnaireEntity> allQuestionnaires = adminService.getAllQuestionnaires();
            allQuestionnaires.sort(Comparator.comparing(QuestionnaireEntity::getDate));

            context.put("questionnaires", allQuestionnaires);
            context.put("today", new Date());
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

        String questionnaireId = request.getParameter("questionnaire");
        String userId = request.getParameter("user");

        String path = String.format("%s/inspection-page?questionnaire=%s&user=%s",
                getServletContext().getContextPath(), questionnaireId, userId);

        response.sendRedirect(path);
    }

    private Optional<UserEntity> getUserId(HttpServletRequest request) {
        String user = request.getParameter("user");

        if (user != null) {
            if (user.matches("\\d+")) {
                return userService.findUser(Long.parseLong(user));
            }
        }

        return Optional.empty();
    }
}
