package it.polimi.db2.db2_project.web.utils;

import it.polimi.db2.db2_project.entities.AnswerEntity;
import it.polimi.db2.db2_project.entities.UserEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.net.http.HttpRequest;
import java.util.*;

public class SessionUtil {
    public static Optional<UserEntity> checkLogin(HttpServletRequest request) {
        //check user logged in
        HttpSession session = request.getSession();
        UserEntity user = (UserEntity) session.getAttribute("user");
        if(session.isNew() || user == null) {
            return Optional.empty();
        }
        return Optional.of(user);
    }

    public static void putAnswers(HttpServletRequest request, Map<Long, String>  answers) {
        HttpSession session = request.getSession();
        session.setAttribute("marketingAnswers", answers);
    }

    @SuppressWarnings("unchecked")
    public static Map<Long, String> getAnswers(HttpServletRequest request) {
        HttpSession session = request.getSession();

        Map<Long, String>  answers;
        answers = (Map<Long, String> ) session.getAttribute("marketingAnswers");
        if(session.isNew() || answers == null) {
            return new HashMap<>();
        }
        return answers;
    }
}
