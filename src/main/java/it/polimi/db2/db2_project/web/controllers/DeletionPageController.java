package it.polimi.db2.db2_project.web.controllers;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@WebServlet(name = "DeletionPageController", value = "/DeletionPageController")
public class DeletionPageController extends TemplatingServlet {

    @EJB
    private AdminService adminService;

    public DeletionPageController() {
        super("deletion-page", TemplateMode.HTML, "WEB-INF/templates/", ".html");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HashMap<String, Object> context = new HashMap<>();

        Optional<UserEntity> user = SessionUtil.checkLogin(request);

        if (user.isEmpty()) {
            response.sendRedirect(getServletContext().getContextPath() + "/login");
            return;
        }

        List<QuestionnaireEntity> oldQuestionnaires = adminService.getAllQuestionnaires().stream()
                .filter(isBeforePresentDay())
                .collect(Collectors.toList());

        context.put("username", user.get().getUsername());
        context.put("isAdmin", user.get().getAdmin());
        context.put("questionnaires", oldQuestionnaires);

        super.processTemplate(request, response, context);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Optional<UserEntity> user = SessionUtil.checkLogin(request);

        if (user.isEmpty()) {
            response.sendRedirect(getServletContext().getContextPath() + "/login");
            return;
        }

        if (!user.get().getAdmin()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid permissions.");
            return;
        }

        adminService.getAllQuestionnaires().stream()
                .filter(isBeforePresentDay())
                .filter(q -> request.getParameter(q.getId().toString()) != null)
                .forEach(q -> adminService.deleteQuestionnaire(q.getId()));
    }

    private Predicate<QuestionnaireEntity> isBeforePresentDay() {
        Instant today = new Date().toInstant().truncatedTo(ChronoUnit.DAYS);

        return q -> q.getDate().toInstant().isBefore(today);
    }
}
