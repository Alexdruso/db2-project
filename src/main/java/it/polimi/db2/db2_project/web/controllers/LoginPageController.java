package it.polimi.db2.db2_project.web.controllers;

import it.polimi.db2.db2_project.entities.UserEntity;
import it.polimi.db2.db2_project.services.UserService;
import it.polimi.db2.db2_project.web.TemplatingServlet;
import org.thymeleaf.templatemode.TemplateMode;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

@WebServlet(name = "LoginPageController", value = "/LoginPageController")
public class LoginPageController extends TemplatingServlet {

    @EJB
    private UserService userService;

    public LoginPageController() {
        super("login", TemplateMode.HTML, "WEB-INF/templates/", ".html");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HashMap<String, Object> context = new HashMap<>();

        if (request.getParameter("login_failed") != null) {
            context.put("login_error", true);
        }

        super.processTemplate(request, response, context);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Optional<UserEntity> result = userService.checkCredentials(username, password);

        String path;

        if (result.isPresent()) {
            UserEntity user = result.get();
            request.getSession().setAttribute("user", user);


            if (user.getAdmin()) {
                path = getServletContext().getContextPath() + "/admin-panel";
            } else {
                path = getServletContext().getContextPath() + "/homepage";
            }

        } else {
            path = getServletContext().getContextPath() + "/login?login_failed=true";
        }

        response.sendRedirect(path);
    }

}
