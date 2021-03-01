package it.polimi.db2.db2_project.web;

import org.thymeleaf.templatemode.TemplateMode;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@WebServlet(name = "TestController", value = "/test")
public class TestController extends TemplatingServlet {

    public TestController() {
        super("test",  TemplateMode.HTML, "WEB-INF/templates/", ".html");

    }

    @Override
    public void init() throws ServletException {
        super.init();
    }



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HashMap<String, Object> ctx = new HashMap<>();
        ctx.put("recipient", "ciao");
        super.processTemplate(request, response, ctx);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

}
