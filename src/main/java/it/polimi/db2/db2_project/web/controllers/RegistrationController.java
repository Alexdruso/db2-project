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
import java.util.Map;
import java.util.Optional;

@WebServlet(name = "RegistrationController", value = "/RegistrationController")
public class RegistrationController extends TemplatingServlet {

    @EJB
    private UserService userService;

    public RegistrationController() {
        super("registration", TemplateMode.HTML, "WEB-INF/templates/", ".html");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> ctx = new HashMap<>();
        ctx.put("missing", request.getParameter("missing"));
        ctx.put("duplicated", request.getParameter("duplicated"));
        super.processTemplate(request, response, ctx);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        boolean invalid = username.isBlank() || email.isBlank() || password.isBlank() ||
                username.isEmpty() || password.isEmpty() || email.isEmpty();

        if (invalid) {
            response.sendRedirect(getServletContext().getContextPath() + "/registration?missing=true");
            return;
        }

        if (userService.findUserByUsername(username).isPresent() || userService.findUserByEmail(email).isPresent()) {
            response.sendRedirect(getServletContext().getContextPath() + "/registration?duplicated=true");
            return;
        }

        Optional<UserEntity> result = userService.createUser(username, password, email);

        if (result.isEmpty()) {
            response.sendRedirect(getServletContext().getContextPath() + "/registration");
            return;
        }

        response.sendRedirect(getServletContext().getContextPath() + "/registration-success");
    }
}
