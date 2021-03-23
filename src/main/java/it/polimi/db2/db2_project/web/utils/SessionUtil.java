package it.polimi.db2.db2_project.web.utils;

import it.polimi.db2.db2_project.entities.UserEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.net.http.HttpRequest;
import java.util.Optional;

public class LoginCheckUtil {
    public static Optional<UserEntity> checkLogin(HttpServletRequest request) {
        //check user logged in
        HttpSession session = request.getSession();
        UserEntity user = (UserEntity) session.getAttribute("user");
        if(session.isNew() || user == null) {
            return Optional.empty();
        }
        return Optional.of(user);
    }
}
