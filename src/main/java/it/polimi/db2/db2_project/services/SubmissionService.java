package it.polimi.db2.db2_project.services;

import it.polimi.db2.db2_project.entities.AnswerEntity;
import it.polimi.db2.db2_project.entities.QuestionEntity;
import it.polimi.db2.db2_project.entities.QuestionnaireEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Stateless
public class SubmissionService {

    @PersistenceContext
    private EntityManager em;

    public Optional<QuestionnaireEntity> findCurrentQuestionnaire() {
        return findQuestionnaireByDate(new Date());
    }

    public Optional<QuestionnaireEntity> findQuestionnaireByDate(Date date) {
        return Optional.ofNullable(
                em.createNamedQuery("Questionnaire.findByDate", QuestionnaireEntity.class)
                        .setParameter("date", date)
                        .getSingleResult()
        );
    }

    public List<QuestionEntity> findMarketingQuestions(long questionnaireId) {
        return em.createNamedQuery("Question.findByQuestionnaire", QuestionEntity.class)
                .setParameter("optional", 0)
                .setParameter("questionnaireId", questionnaireId)
                .getResultList();
    }

    public List<QuestionEntity> findStatisticalQuestions(long questionnaireId) {
        return em.createNamedQuery("Question.findByQuestionnaire", QuestionEntity.class)
                .setParameter("optional", 1)
                .setParameter("questionnaireId", questionnaireId)
                .getResultList();
    }

    /**
     *
     * @param userId
     * @param answers A map having as keys the question ID and as values the strings
     * @return
     */
    public boolean submitMarketingAnswers(long userId, Map<Long, String> answers) {
        //TODO CHeck badwors
        //TODO Chcek all answers present
        //TODO CHeck if already presented
        return false;
    }

    public boolean submitStatisticalAnswers(long userId, Map<Long,String> answers) {
        //TODO
        return false;
    }

    public List<AnswerEntity> findAnswers(long userId, long questionnaireId) {
        //TODO Useful for changing marketing question
        return null;
    }
}
