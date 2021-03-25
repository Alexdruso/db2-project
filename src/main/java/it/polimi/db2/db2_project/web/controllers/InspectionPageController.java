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
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@WebServlet(name = "InspectionPageController", value = "/InspectionPageController")
public class InspectionPageController extends TemplatingServlet {

    @EJB
    private AdminService adminService;

    @EJB
    private ProductService productService;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public InspectionPageController() {
        super("inspection-page", TemplateMode.HTML, "WEB-INF/templates/", ".html");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HashMap<String, Object> context = new HashMap<>();

        if (request.getParameter("selected") != null) {
            long questionnaireId = Long.parseLong(request.getParameter("selected"));
            QuestionnaireEntity questionnaire = adminService.getQuestionnaire(questionnaireId);
            context.put("selected", questionnaire);

            if (request.getParameter("user") != null) {
                long userId = Long.parseLong(request.getParameter("user"));
//                adminService.getAllAnswersByUser(userId, questionnaireId).get(0).getQuestion()
                context.put("answers", adminService.getAllAnswersByUser(userId, questionnaireId));
            } else {
                context.put("submissions", adminService.getAllSubmitters(questionnaireId));
                context.put("cancellations", adminService.getAllCancellations(questionnaireId));
            }
        } else {
            List<QuestionnaireEntity> allQuestionnaires = adminService.getAllQuestionnaires();
            allQuestionnaires.sort(Comparator.comparing(QuestionnaireEntity::getDate));

            context.put("questionnaires", allQuestionnaires);
        }

        super.processTemplate(request, response, context);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //check user logged in
        HttpSession session = request.getSession();
        UserEntity user = (UserEntity) session.getAttribute("user");

//        if (session.isNew() || user == null) {
//            String path = getServletContext().getContextPath() + "/";
//            response.sendRedirect(path);
//            return;
//        }

//        System.out.println(user.getAdmin());


        String questionnaireId = request.getParameter("selected");
        String userId = request.getParameter("user");


        String redirect = String.format("%s/inspection-page?selected=%s&user=%s",
                getServletContext().getContextPath(), questionnaireId, userId);

        response.sendRedirect(redirect);
    }
}
