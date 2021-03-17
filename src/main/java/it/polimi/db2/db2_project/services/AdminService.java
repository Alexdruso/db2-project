package it.polimi.db2.db2_project.services;

import it.polimi.db2.db2_project.entities.*;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Stateless
public class AdminService {

    @PersistenceContext
    private EntityManager em;

    public QuestionnaireEntity getQuestionnaire(long questionnaireId) {
        return em.createNamedQuery("Questionnaire.findById", QuestionnaireEntity.class)
                .setParameter("id", questionnaireId)
                .getSingleResult();
    }

    public QuestionnaireEntity createQuestionnaire(long creatorUserId, long productId, Date date) {
        QuestionnaireEntity newQ = new QuestionnaireEntity();
        newQ.setDate(date);
        UserEntity creatorUser = em.find(UserEntity.class, creatorUserId);

        if(creatorUser == null) {
            throw new IllegalArgumentException(String.format("User with ID = %d does not exist!", creatorUserId));
        }

        ProductEntity product = em.find(ProductEntity.class, productId);

        if(product == null) {
            throw new IllegalArgumentException(String.format("Product with ID = %d does not exist!", productId));
        }

        newQ.setProductEntity(product);
        Date today = new Date();

        if(!date.after(today)) {
            throw new IllegalArgumentException("Questionnaire date must be set in the future");
        }

        newQ.setDate(date);
        em.persist(newQ);

        return newQ;
    }

    public void deleteQuestionnaire(long questionnaireId)  {
        QuestionnaireEntity toDelete = em.find(QuestionnaireEntity.class, questionnaireId);
        if(toDelete == null) {
            throw new IllegalArgumentException(String.format("Questionnaire with ID = %d does not exist!", questionnaireId));
        }
        em.remove(toDelete);
    }

    public QuestionnaireEntity addMarketingQuestion(long questionnaireId, String questionText) {
        QuestionnaireEntity questionnaire = em.find(QuestionnaireEntity.class, questionnaireId);

        if(questionnaire == null) {
            throw new IllegalArgumentException(String.format("Questionnaire with ID = %d does not exist!", questionnaireId));
        }

        if(questionText.equals("")) {
            throw new IllegalArgumentException("Question can't be empty");
        }

        QuestionEntity newQuestion = new QuestionEntity();
        newQuestion.setText(questionText);
        newQuestion.setOptional(false);

        newQuestion.getQuestionnaires().add(questionnaire);
        questionnaire.getQuestions().add(newQuestion);

        em.persist(newQuestion);

        return questionnaire;
    }

    public List<QuestionnaireEntity> getAllQuestionnaires() {
        return em.createNamedQuery("Questionnaire.findAll", QuestionnaireEntity.class)
                .getResultList();
    }

    public List<UserEntity> getAllSubmitters(long questionnaireId) {
        return em.createNamedQuery("Questionnaire.findSubmitters", UserEntity.class)
                .setParameter("id", questionnaireId)
                .getResultList();
    }

    public List<UserEntity> getAllCancellations(long questionnaireId) {
        return em.createNamedQuery("Questionnaire.findCancellation", UserEntity.class)
                .setParameter("id", questionnaireId)
                .getResultList();
    }

    public List<AnswerEntity> getAllAnswersByUser(long userId, long questionnaireId) {
        return em.createNamedQuery("QuestionnaireSubmission.getAllUserAnswer", AnswerEntity.class)
                .setParameter("userId", userId)
                .setParameter("questionnaireId", questionnaireId)
                .getResultList();
    }
}
