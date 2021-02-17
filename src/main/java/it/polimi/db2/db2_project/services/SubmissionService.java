package it.polimi.db2.db2_project.services;

import it.polimi.db2.db2_project.entities.*;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;

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

    public QuestionnaireSubmissionEntity createQuestionnaireSubmission(long userId, long questionnaireId) {
        UserEntity user = em.find(UserEntity.class, userId);
        QuestionnaireEntity questionnare = em.find(QuestionnaireEntity.class, questionnaireId);
        QuestionnaireSubmissionEntity questionnaireSubmission = new QuestionnaireSubmissionEntity(user, questionnare);

        user.addQuestionnaireSubmission(questionnaireSubmission);
        questionnare.addQuestionnaireSubmission(questionnaireSubmission);

        em.persist(user);
        em.persist(questionnare);
        em.persist(questionnaireSubmission);

        em.flush();

        return questionnaireSubmission;
    }

    /**
     * @param userId
     * @param answers A map having as keys the question ID and as values the strings
     * @return
     */
    public void submitMarketingAnswers(long userId, Map<Long, String> answers) {
        //TODO CHeck badwors
        //TODO Chcek all answers present
        //TODO CHeck if already presented
    }

    public void submitStatisticalAnswers(long userId, Map<Long, String> answers) {
        //TODO
    }

    public List<AnswerEntity> findAnswers(long userId, long questionnaireId) {
        //TODO Useful for changing marketing question
        return null;
    }

    /**
     * This method checks if a list of answers contains any offensive word.
     * The check is case insensitive.
     *
     * @param answers the answers to be checked
     * @return true if anw answer contains any offensive word
     */
    public boolean checkOffensiveWords(List<String> answers) {
        List<String> offensiveWords = em.createNamedQuery("OffensiveWord.findAll", OffensiveWordEntity.class)
                .getResultStream()
                .map(OffensiveWordEntity::getWord)
                .collect(Collectors.toList());

        return answers.stream().anyMatch(
                answer -> checkOffensiveWords(
                        answer,
                        offensiveWords
                )
        );
    }

    /**
     * This method checks if an answer contains any offensive word.
     * The check is case insensitive.
     *
     * @param answer the answer to be checked
     * @return true if the answer contains any offensive word
     */
    public boolean checkOffensiveWords(String answer) {
        return checkOffensiveWords(
                answer,
                em.createNamedQuery("OffensiveWord.findAll", OffensiveWordEntity.class)
                        .getResultStream()
                        .map(OffensiveWordEntity::getWord)
                        .collect(Collectors.toList())
        );
    }

    /**
     * This method checks if an answer contains any of the listed offensive words.
     * The check is case insensitive.
     *
     * @param answer         the answer to be checked
     * @param offensiveWords the offensive words
     * @return true if the answer contains any offensive word
     */
    public boolean checkOffensiveWords(String answer, List<String> offensiveWords) {
        return offensiveWords
                .stream()
                .anyMatch(
                        offensiveWord -> answer.toLowerCase(Locale.ROOT).contains(offensiveWord.toLowerCase(Locale.ROOT))
                );
    }
}
