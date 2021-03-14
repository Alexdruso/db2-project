package it.polimi.db2.db2_project.web.controllers;

import it.polimi.db2.db2_project.entities.UserEntity;
import it.polimi.db2.db2_project.services.UserService;
import it.polimi.db2.db2_project.web.TemplatingServlet;
import org.thymeleaf.templatemode.TemplateMode;

import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

@WebServlet(name = "LoginController", value = "/LoginController")
public class LoginController extends TemplatingServlet {

    @EJB
    private UserService userService;

    public LoginController() {
        super("login",  TemplateMode.HTML, "WEB-INF/templates/", ".html");

    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        Optional<UserEntity> result = userService.checkCredentials(username, password);
        if(result.isPresent()) {

            Cookie loginCookie = new Cookie("user", username);
            loginCookie.setMaxAge(30*60);
            response.addCookie(loginCookie);
            response.sendRedirect("/homepage");

        } else {
            HashMap<String, Object> ctx = new HashMap<>();
            response
        }
    }
}
