package it.polimi.db2.db2_project.web;

import it.polimi.db2.db2_project.services.UserService;
import org.thymeleaf.templatemode.TemplateMode;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "LoginController", value = "/LoginController")
public class LoginController extends TemplatingServlet {

    public LoginController() {
        super("Templates/Login/LoginSer.html",  TemplateMode.HTML, "", ".html");

    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //UserService us = new UserService();
        //us.checkCredentials();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
