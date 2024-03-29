package it.polimi.db2.db2_project.web.controllers;

import it.polimi.db2.db2_project.entities.QuestionnaireEntity;
import it.polimi.db2.db2_project.entities.UserEntity;
import it.polimi.db2.db2_project.services.AdminService;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

@WebServlet(name = "CreationPageController", value = "/CreationPageController")
public class CreationPageController extends TemplatingServlet {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @EJB
    private AdminService adminService;
    @EJB
    private ProductService productService;

    public CreationPageController() {
        super("creation-page", TemplateMode.HTML, "WEB-INF/templates/", ".html");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HashMap<String, Object> context = new HashMap<>();

        Optional<UserEntity> user = SessionUtil.checkLogin(request);

        if (user.isEmpty()) {
            String path = getServletContext().getContextPath() + "/login";
            response.sendRedirect(path);
            return;
        }

        context.put("isAdmin", user.get().getAdmin());
        context.put("products", productService.findAllProducts());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();

        context.put("date", dateFormat.format(tomorrow));

        super.processTemplate(request, response, context);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Optional<UserEntity> user = SessionUtil.checkLogin(request);

        if (user.isEmpty()) {
            String path = getServletContext().getContextPath() + "/login";
            response.sendRedirect(path);
            return;
        }

        if (!user.get().getAdmin()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid permissions.");
            return;
        }

        long productId = Long.parseLong(request.getParameter("product"));
        Date questionnaireDate;

        try {
            questionnaireDate = dateFormat.parse(request.getParameter("date"));
        } catch (ParseException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid date format");
            return;
        }

        if (questionnaireDate.before(new Date())) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Questionnaires can only be created for a future date");
            return;
        }

        boolean dateTaken = adminService.getAllQuestionnaires().stream()
                .anyMatch(q -> q.getDate().equals(questionnaireDate));

        if (dateTaken) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "There is already a questionnaire scheduled for the selected date");
            return;
        }

        QuestionnaireEntity newQuestionnaire = adminService.createQuestionnaire(
                user.get().getId(),
                productId,
                questionnaireDate
        );

        String path = String.format("%s/edit-questionnaire?id=%d",
                getServletContext().getContextPath(), newQuestionnaire.getId());

        response.sendRedirect(path);
    }
}
