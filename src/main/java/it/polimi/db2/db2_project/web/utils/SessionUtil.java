package it.polimi.db2.db2_project.web.utils;

import it.polimi.db2.db2_project.entities.QuestionnaireEntity;
import it.polimi.db2.db2_project.entities.UserEntity;
import it.polimi.db2.db2_project.services.AdminService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SessionUtil {

    public static Optional<UserEntity> checkLogin(HttpServletRequest request) {
        // Retrieve user from the session
        HttpSession session = request.getSession();
        UserEntity user = (UserEntity) session.getAttribute("user");

        // Return empty optional if the login status is invalid
        if (session.isNew() || user == null) {
            return Optional.empty();
        }

        return Optional.of(user);
    }

    public static void saveAnswersToSession(HttpServletRequest request, Map<Long, String> answers) {
        HttpSession session = request.getSession();
        session.setAttribute("marketingAnswers", answers);
    }

    @SuppressWarnings("unchecked")
    public static Map<Long, String> getAnswersFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession();

        Map<Long, String> answers;
        answers = (Map<Long, String>) session.getAttribute("marketingAnswers");

        if (session.isNew() || answers == null) {
            return new HashMap<>();
        }

        return answers;
    }

    public static Optional<QuestionnaireEntity> getQuestionnaire(AdminService adminService, HttpServletRequest request,
                                                                 String parameterName) {
        Optional<String> questionnaireId = Optional.ofNullable(request.getParameter(parameterName));

        if (questionnaireId.isPresent()) {
            if (questionnaireId.get().matches("\\d+")) {
                long id = Long.parseLong(questionnaireId.get());

                return adminService.getQuestionnaire(id);
            }
        }

        return Optional.empty();
    }
}
