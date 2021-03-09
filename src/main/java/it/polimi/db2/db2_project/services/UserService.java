package it.polimi.db2.db2_project.services;

import it.polimi.db2.db2_project.entities.UserEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

@Stateless
public class UserService {

    @PersistenceContext
    private EntityManager em;

    public UserEntity createUser(String username, String password, String email) {
        UserEntity user = new UserEntity(username, password, email, new Date(), false, false);

        em.persist(user);
        em.flush();

        return user;
    }

    public Optional<UserEntity> checkCredentials(String username, String password) {
        UserEntity user = em.createNamedQuery("User.findByUsername", UserEntity.class)
                .setParameter("username", username)
                .getSingleResult();
        if (user == null || !password.equals(user.getPassword())) {
            return Optional.empty();
        } else {
            //log user
            user.setLastLogin(new Timestamp(1));
            em.persist(user);
            em.flush();
            return Optional.of(user);
        }

    }

    public void banUser(long userId) {
        UserEntity user = em.find(UserEntity.class, userId);
        if(user == null) {
            throw new IllegalArgumentException(String.format("User with ID = %d does not exist!", userId));
        }
        user.setBan(true);
    }
}
