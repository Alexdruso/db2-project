package it.polimi.db2.db2_project.web.controllers;

import it.polimi.db2.db2_project.entities.QuestionEntity;
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
import java.util.HashMap;

@WebServlet(name = "EditQuestionnaireController", value = "/EditQuestionnaireController")
public class EditQuestionnaireController extends TemplatingServlet {

    @EJB
    private AdminService adminService;

    @EJB
    private ProductService productService;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public EditQuestionnaireController() {
        super("edit-questionnaire", TemplateMode.HTML, "WEB-INF/templates/", ".html");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HashMap<String, Object> context = new HashMap<>();

        HttpSession session = request.getSession();
        UserEntity user = (UserEntity) session.getAttribute("user");

        String questionnaireId = request.getParameter("id");

        System.out.println("OOF");

        if (questionnaireId != null) {
            QuestionnaireEntity questionnaire = adminService.getQuestionnaire(Long.parseLong(questionnaireId));

            context.put("questionnaire", questionnaire);
        }

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

        System.out.println(user.getAdmin());

        String questionnaireId = request.getParameter("id");

        if (questionnaireId != null) {
            QuestionnaireEntity questionnaire = adminService.getQuestionnaire(Long.parseLong(questionnaireId));

            if (request.getParameter("add") != null) {
                adminService.addMarketingQuestion(questionnaire.getId(), "placeholderoni?");

            } else if (request.getParameter("update") != null) {
                questionnaire.getQuestions().stream()
                        .map(QuestionEntity::getId)
                        .filter(id -> request.getParameter(id.toString()) != null)
                        .forEach(id -> {
                            String newText = request.getParameter(id.toString());
                            if (!newText.isEmpty()) {
                                adminService.updateQuestion(id, newText);
                            } else {
                                adminService.removeQuestion(id);
                            }
                        });
            }
        }

        String redirect = String.format("%s/edit-questionnaire?id=%s",
                getServletContext().getContextPath(), questionnaireId);

        response.sendRedirect(redirect);
    }
}
