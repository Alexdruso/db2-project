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

@WebServlet(name = "RegistrationController", value = "/RegistrationController")
public class RegistrationController extends TemplatingServlet {

    @EJB
    private UserService userService;

    public RegistrationController() {
        super("registration", TemplateMode.HTML, "WEB-INF/templates/", ".html");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.processTemplate(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        boolean invalid = username.isBlank() || email.isBlank() || password.isBlank() ||
                username.isEmpty() || password.isEmpty() || email.isEmpty();

        if (invalid) {
            response.sendRedirect(getServletContext().getContextPath() + "/registration");
            return;
        }

        Optional<UserEntity> result = userService.createUser(username, email, password);

        if (result.isEmpty()) {
            response.sendRedirect(getServletContext().getContextPath() + "/registration");
            return;
        }

        response.sendRedirect(getServletContext().getContextPath() + "/registration-success");
    }
}
