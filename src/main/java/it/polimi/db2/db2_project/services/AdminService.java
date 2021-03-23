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
        QuestionnaireEntity questionnaire = em.find(QuestionnaireEntity.class, questionnaireId);
        em.refresh(questionnaire);
        return questionnaire;
    }

    public QuestionnaireEntity createQuestionnaire(long userId, long productId, Date date) {

        UserEntity user = em.find(UserEntity.class, userId);
        if(user == null) {
            throw new IllegalArgumentException(String.format("User with ID = %d does not exist!", userId));
        }

        ProductEntity product = em.find(ProductEntity.class, productId);

        if(product == null) {
            throw new IllegalArgumentException(String.format("Product with ID = %d does not exist!", productId));
        }

        Date today = new Date();

        if(!date.after(today)) {
            throw new IllegalArgumentException("Questionnaire date must be set in the future");
        }

        QuestionnaireEntity questionnaire = new QuestionnaireEntity(date, user, product);
        em.persist(questionnaire);
        em.flush();

        return questionnaire;
    }

    public void deleteQuestionnaire(long questionnaireId)  {
        QuestionnaireEntity questionnaire = em.find(QuestionnaireEntity.class, questionnaireId);
        if(questionnaire == null) {
            throw new IllegalArgumentException(String.format("Questionnaire with ID = %d does not exist!", questionnaireId));
        }
        em.remove(questionnaire);

        em.flush();
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

        em.persist(newQuestion);

        em.flush();

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
