package it.polimi.db2.db2_project.web.controllers;

import it.polimi.db2.db2_project.entities.ProductEntity;
import it.polimi.db2.db2_project.entities.UserEntity;
import it.polimi.db2.db2_project.services.ProductService;
import it.polimi.db2.db2_project.web.TemplatingServlet;
import it.polimi.db2.db2_project.web.utils.SessionUtil;
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

@WebServlet(name = "HomePageController", value = "/HomePageController")
public class HomePageController extends TemplatingServlet {

    @EJB
    private ProductService productService;

    public HomePageController() {
        super("welcome", TemplateMode.HTML, "WEB-INF/templates/", ".html");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Optional<UserEntity> user = SessionUtil.checkLogin(request);

        if (user.isEmpty()) {
            String path = getServletContext().getContextPath() + "/login";
            response.sendRedirect(path);
            return;
        }

        if (user.get().getBan()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "You have been banned.");
            return;
        }

        final Map<String, Object> context = new HashMap<>();
        context.put("username", user.get().getUsername());

        Optional<ProductEntity> product = productService.findCurrentProduct();

        context.put("product", product.orElse(null));
        product.ifPresent(p -> context.put("reviews", productService.findProductReviews(p)));
        super.processTemplate(request, response, context);
    }
}
