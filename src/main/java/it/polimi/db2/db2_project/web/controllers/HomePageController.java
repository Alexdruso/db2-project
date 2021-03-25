package it.polimi.db2.db2_project.web.controllers;

import it.polimi.db2.db2_project.entities.UserEntity;
import it.polimi.db2.db2_project.services.ProductService;
import it.polimi.db2.db2_project.services.SubmissionService;
import it.polimi.db2.db2_project.web.TemplatingServlet;
import it.polimi.db2.db2_project.web.utils.ImageUtil;
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

    @EJB
    private SubmissionService submissionService;


    public HomePageController() {
        super("welcome", TemplateMode.HTML, "WEB-INF/templates/", ".html");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Optional<UserEntity> userOpt = SessionUtil.checkLogin(request);

        if (userOpt.isEmpty()) {
            String path = getServletContext().getContextPath() + "/login";
            response.sendRedirect(path);
            return;
        }

        UserEntity user = userOpt.get();

        if (user.getBan()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "You have been banned.");
            return;
        }

        final Map<String, Object> ctx = new HashMap<>();
        ctx.put("converter", new ImageUtil());

        productService.findCurrentProduct().ifPresentOrElse(
                product -> {
                    ctx.put("product", product);
                    ctx.put("reviews", productService.findProductReviews(product));
                },
                () -> ctx.put("product", null)
        );


        ctx.put("username", user.getUsername());
        super.processTemplate(request, response, ctx);
    }
}
