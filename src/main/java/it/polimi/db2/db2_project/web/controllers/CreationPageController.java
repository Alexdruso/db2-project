package it.polimi.db2.db2_project.web.controllers;

import it.polimi.db2.db2_project.entities.QuestionnaireEntity;
import it.polimi.db2.db2_project.entities.UserEntity;
import it.polimi.db2.db2_project.services.AdminService;
import it.polimi.db2.db2_project.services.ProductService;
import it.polimi.db2.db2_project.web.TemplatingServlet;
import org.thymeleaf.templatemode.TemplateMode;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@WebServlet(name = "CreationPageController", value = "/CreationPageController")
public class CreationPageController extends TemplatingServlet {

    @EJB
    private AdminService adminService;

    @EJB
    private ProductService productService;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public CreationPageController() {
        super("creation-page", TemplateMode.HTML, "WEB-INF/templates/", ".html");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HashMap<String, Object> context = new HashMap<>();

        context.put("products", productService.findAllProducts());
        context.put("date", dateFormat.format(new Date()));

        super.processTemplate(request, response, context);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //check user logged in
        HttpSession session = request.getSession();
        UserEntity user = (UserEntity) session.getAttribute("user");

        if (session.isNew() || user == null) {
            String path = getServletContext().getContextPath() + "/";
            response.sendRedirect(path);
            return;
        }

        long userId = user.getId();
        long productId = Long.parseLong(request.getParameter("product"));
        Date questionnaireDate;

        try {
            questionnaireDate = dateFormat.parse(request.getParameter("date"));
        } catch (ParseException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid date format");
            return;
        }

        QuestionnaireEntity newQuestionnaire = adminService.createQuestionnaire(userId, productId, questionnaireDate);

        String redirect = String.format("%s/edit-questionnaire?id=%d",
                getServletContext().getContextPath(), newQuestionnaire.getId());

        response.sendRedirect(redirect);
    }
}
