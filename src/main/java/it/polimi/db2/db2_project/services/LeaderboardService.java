package it.polimi.db2.db2_project.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

@Stateless
public class LeaderboardService {

    @PersistenceContext
    private EntityManager em;

    public List<Object[]> findCurrentLeaderboard() {
        return findLeaderboardByDate(new Date());
    }

    public List<Object[]> findLeaderboardByDate(Date date) {
        return em.createNamedQuery("QuestionnaireSubmission.findLeaderboardByDate", Object[].class)
                .setParameter("date", date, TemporalType.DATE)
                .getResultList();
    }

}
