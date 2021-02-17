package it.polimi.db2.db2_project.services;

import it.polimi.db2.db2_project.entities.UserEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Stateless
public class UserService {

    @PersistenceContext
    private EntityManager em;

    public Optional<UserEntity> checkCredentials(String username, String password) {
        UserEntity user = em.createNamedQuery("User.findByUsername", UserEntity.class)
                .setParameter("username", username)
                .getSingleResult();
        if (user == null || !password.equals(user.getPassword())) {
            return Optional.empty();
        } else {
            return Optional.of(user);
        }

    }

    public void banUser(long userId) {
        UserEntity user = em.find(UserEntity.class, userId);
        if(user == null) {
            throw new IllegalArgumentException(String.format("User with ID = %d does not exist!", userId));
        }
        user.setBan((byte) 1);
    }


}
