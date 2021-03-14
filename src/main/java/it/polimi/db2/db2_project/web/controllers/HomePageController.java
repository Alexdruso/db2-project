package it.polimi.db2.db2_project.web.controllers;

import it.polimi.db2.db2_project.entities.UserEntity;
import it.polimi.db2.db2_project.web.TemplatingServlet;
import org.thymeleaf.templatemode.TemplateMode;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;

@WebServlet(name = "HomePageController", value = "/HomePageController")
public class HomePageController extends TemplatingServlet {

    public HomePageController() {
        super("welcome", TemplateMode.HTML, "WEB-INF/templates/", ".html");

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //check user logged in
        HttpSession session = request.getSession();
        UserEntity user = (UserEntity) session.getAttribute("user");

        if (session.isNew() || user == null) {
            String path = getServletContext().getContextPath() + "/";
            response.sendRedirect(path);
            return;
        }


        HashMap<String, Object> ctx = new HashMap<>();
        ctx.put("username", user.getUsername());
        super.processTemplate(request, response, ctx);
    }
}
