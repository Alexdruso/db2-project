package it.polimi.db2.db2_project.web.controllers;

import it.polimi.db2.db2_project.entities.ProductEntity;
import it.polimi.db2.db2_project.entities.QuestionnaireEntity;
import it.polimi.db2.db2_project.entities.UserEntity;
import it.polimi.db2.db2_project.services.ProductService;
import it.polimi.db2.db2_project.services.SubmissionService;
import it.polimi.db2.db2_project.web.TemplatingServlet;
import it.polimi.db2.db2_project.web.utils.ImageUtil;
import org.thymeleaf.templatemode.TemplateMode;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

        //check user logged in
        HttpSession session = request.getSession();
        UserEntity user = (UserEntity) session.getAttribute("user");
        final Map<String, Object> ctx = new HashMap<>();
        ctx.put("converter", new ImageUtil());

        if (session.isNew() || user == null) {
            String path = getServletContext().getContextPath() + "/";
            response.sendRedirect(path);
            return;
        }
        productService.findAllProducts().forEach(productEntity -> System.out.println(submissionService.findCurrentQuestionnaire()));
        productService.findCurrentProduct().ifPresentOrElse(
                product -> {
                    System.out.println("eeee:" + product.getName());
                    ctx.put("product", product);
                    ctx.put("reviews", productService.findProductReviews(product));

                },
                () -> {
                    System.out.println("sono nullo");
                    ctx.put("product", null);
                }
        );


        ctx.put("username", user.getUsername());
        super.processTemplate(request, response, ctx);
    }
}
