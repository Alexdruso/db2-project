package it.polimi.db2.db2_project.web.controllers;

import it.polimi.db2.db2_project.entities.UserEntity;
import it.polimi.db2.db2_project.services.AdminService;
import it.polimi.db2.db2_project.services.SubmissionService;
import it.polimi.db2.db2_project.web.TemplatingServlet;
import it.polimi.db2.db2_project.web.utils.ImageUtil;
import it.polimi.db2.db2_project.web.utils.SessionUtil;
import org.thymeleaf.templatemode.TemplateMode;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Optional;

@WebServlet(name = "AdminPanelController", value = "/AdminPanelController")
public class AdminPanelController extends TemplatingServlet {

    @EJB
    private AdminService adminService;

    @EJB
    private SubmissionService submissionService;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public AdminPanelController() {
        super("admin-panel", TemplateMode.HTML, "WEB-INF/templates/", ".html");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HashMap<String, Object> context = new HashMap<>();

        Optional<UserEntity> user = SessionUtil.checkLogin(request);

        if (user.isEmpty()) {
            response.sendRedirect(getServletContext().getContextPath() + "/login");
            return;
        }

        context.put("username", user.get().getUsername());
        context.put("isAdmin", user.get().getAdmin());
        context.put("questionnaire", submissionService.findCurrentQuestionnaire().orElse(null));
        context.put("converter", new ImageUtil());

        super.processTemplate(request, response, context);
    }
}
