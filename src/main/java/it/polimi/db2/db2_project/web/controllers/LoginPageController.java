package it.polimi.db2.db2_project.web.controllers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@WebServlet(name = "LoginController", value = "/LoginController")
public class LoginPageController {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HashMap<String, Object> ctx = new HashMap<>();

        ctx.put("recipient", getDatabaseName());
        productService.findCurrentProduct();
        super.processTemplate(request, response, ctx);
    }

}
