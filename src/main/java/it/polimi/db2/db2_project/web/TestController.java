package it.polimi.db2.db2_project.web;

import it.polimi.db2.db2_project.entities.ProductEntity;
import it.polimi.db2.db2_project.services.ProductService;
import org.thymeleaf.templatemode.TemplateMode;

import javax.ejb.EJB;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "TestController", value = "/test")
public class TestController extends TemplatingServlet {

    @EJB
    private ProductService productService;

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

        ctx.put("recipient", getDatabaseName());
        productService.findCurrentProduct();
        super.processTemplate(request, response, ctx);
    }

    public String getDatabaseName() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        String dbName = null;
        Map<String, Object> map = emf.getProperties();
        String url = (String) map.get("javax.persistence.jdbc.url");
        if(url != null) {
            dbName = url.substring(url.lastIndexOf("/") + 1);
        }
        return dbName;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

}
