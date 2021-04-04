package it.polimi.db2.db2_project.web.controllers;

import it.polimi.db2.db2_project.entities.ProductEntity;
import it.polimi.db2.db2_project.entities.QuestionnaireEntity;
import it.polimi.db2.db2_project.entities.QuestionnaireSubmissionEntity;
import it.polimi.db2.db2_project.entities.UserEntity;
import it.polimi.db2.db2_project.services.ProductService;
import it.polimi.db2.db2_project.services.SubmissionService;
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

    @EJB
    private SubmissionService submissionService;

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

        Map<String, Object> context = new HashMap<>();
        context.put("username", user.get().getUsername());
        context.put("ban", user.get().getBan());

        Optional<ProductEntity> product = productService.findCurrentProduct();

        context.put("product", product.orElse(null));

        boolean hasSubmitted = false;

        if (product.isPresent()) {
            context.put("reviews", productService.findProductReviews(product.get()));

            Optional<QuestionnaireEntity> questionnaire = submissionService.findCurrentQuestionnaire();

            if (questionnaire.isPresent()) {

                Optional<QuestionnaireSubmissionEntity> submission = submissionService.findQuestionnaireSubmission(
                        user.get().getId(), questionnaire.get().getId());

                if (submission.isPresent()) {
                    hasSubmitted = submission.get().isSubmitted();
                }
            }
        }

        context.put("hasSubmitted", hasSubmitted);

        super.processTemplate(request, response, context);
    }
}
