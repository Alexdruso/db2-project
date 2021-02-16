package it.polimi.db2.db2_project.services;

import it.polimi.db2.db2_project.entities.UserEntity;

import javax.ejb.Stateless;
import java.util.Optional;

@Stateless
public class UserService {
    public Optional<UserEntity> checkCredentials(String username, String password) {
        //TODO
        return null;
    }

    public void banUser(long userId) {

    }


}
