package it.polimi.db2.db2_project.services;

import it.polimi.db2.db2_project.entities.UserEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

@Stateless
public class UserService {

    @PersistenceContext
    private EntityManager em;

    public Optional<UserEntity> findUser(long userId) {
        UserEntity user = em.find(UserEntity.class, userId);

        return Optional.ofNullable(user);
    }


    public Optional<UserEntity> createUser(String username, String password, String email) {
        UserEntity user = new UserEntity(username, password, email, new Date(), false, false);

        try {
            em.persist(user);
            em.flush();
            return Optional.of(user);
        } catch (ConstraintViolationException e) {
            return Optional.empty();
        }
    }

    public Optional<UserEntity> checkCredentials(String username, String password) {
        Optional<UserEntity> user = em.createNamedQuery("User.findByUsername", UserEntity.class)
                .setParameter("username", username)
                .getResultStream().findFirst();

        if (user.isEmpty()) {
            return Optional.empty();
        }

        if (!password.equals(user.get().getPassword())) {
            return Optional.empty();
        }

        user.get().setLastLogin(new Timestamp(System.currentTimeMillis()));

        em.persist(user.get());
        em.flush();

        return user;
    }

    public void banUser(long userId) {
        Optional<UserEntity> user = findUser(userId);

        if (user.isEmpty()) {
            throw new IllegalArgumentException(String.format("User with ID = %d does not exist!", userId));
        }

        user.get().setBan(true);
        em.persist(user);
        em.flush();
    }
}
