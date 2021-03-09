package it.polimi.db2.db2_project.services;

import it.polimi.db2.db2_project.entities.*;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This service provides all functionalities related to the submission of a questionnaire.
 */
@Stateless
public class SubmissionService {

    @PersistenceContext
    private EntityManager em;

    /**
     * Find the questionnaire for the current date.
     *
     * @return an optional containing the questionnaire of the current date
     */
    public Optional<QuestionnaireEntity> findCurrentQuestionnaire() {
        return findQuestionnaire(new Date());
    }

    /**
     * Find the questionnaire for the specified date.
     *
     * @param date the date of the questionnaire
     * @return an optional containing the questionnaire of the given date
     */
    public Optional<QuestionnaireEntity> findQuestionnaire(Date date) {
        return Optional.ofNullable(
                em.createNamedQuery("Questionnaire.findByDate", QuestionnaireEntity.class)
                        .setParameter("date", date, TemporalType.DATE)
                        .getSingleResult()
        );
    }

    /**
     * Find the marketing questions of a questionnaire.
     *
     * @param questionnaireId the questionnaire id
     * @return the list of marketing questions associated to a questionnaire
     */
    public List<QuestionEntity> findMarketingQuestions(long questionnaireId) {
        return em.createNamedQuery("Question.findByQuestionnaire", QuestionEntity.class)
                .setParameter("optional", false)
                .setParameter("questionnaireId", questionnaireId)
                .getResultList();
    }

    /**
     * Find the statistical questions of a questionnaire.
     *
     * @param questionnaireId the questionnaire id
     * @return the list of statistical questions associated to a questionnaire
     */
    public List<QuestionEntity> findStatisticalQuestions(long questionnaireId) {
        return em.createNamedQuery("Question.findByQuestionnaire", QuestionEntity.class)
                .setParameter("optional", true)
                .setParameter("questionnaireId", questionnaireId)
                .getResultList();
    }

    /**
     * Create a questionnaire submission.
     *
     * @param userId          the user id
     * @param questionnaireId the questionnaire id
     * @return the created questionnaire submission entity
     */
    public QuestionnaireSubmissionEntity createQuestionnaireSubmission(long userId, long questionnaireId) {
        UserEntity user = em.find(UserEntity.class, userId);
        QuestionnaireEntity questionnaire = em.find(QuestionnaireEntity.class, questionnaireId);
        QuestionnaireSubmissionEntity questionnaireSubmission = new QuestionnaireSubmissionEntity(user, questionnaire);

        user.addQuestionnaireSubmission(questionnaireSubmission);
        questionnaire.addQuestionnaireSubmission(questionnaireSubmission);

        em.persist(user);
        em.persist(questionnaire);
        em.persist(questionnaireSubmission);

        em.flush();

        return questionnaireSubmission;
    }

    /**
     * Find a questionnaire submission by the creator and the questionnaire.
     *
     * @param userId          the user id
     * @param questionnaireId the questionnaire id
     * @return the questionnaire submission, if present
     */
    public Optional<QuestionnaireSubmissionEntity> findQuestionnaireSubmission(long userId, long questionnaireId) {
        return Optional.ofNullable(
                em.createNamedQuery(
                        "QuestionnaireSubmission.findByUserAndQuestionnaire",
                        QuestionnaireSubmissionEntity.class
                )
                        .setParameter("userId", userId)
                        .setParameter("questionnaireId", questionnaireId)
                        .getSingleResult()
        );
    }


    /**
     * Insert the answers by a user to a questionnaire into the database.
     *
     * @param questionnaireId the questionnaire id
     * @param userId          the user id
     * @param answers         the map between the id of the question and the answers
     */
    public void submitAnswers(long questionnaireId, long userId, Map<Long, String> answers) {
        submitAnswers(
                //retrieve the questionnaire submission id
                em.createNamedQuery(
                        "QuestionnaireSubmission.findByUserAndQuestionnaire",
                        QuestionnaireSubmissionEntity.class
                )
                        .setParameter("userId", userId)
                        .setParameter("questionnaireId", questionnaireId)
                        .getSingleResult()
                        .getId(),
                answers
        );
    }

    /**
     * Submit answers in a specific questionnaire submission.
     *
     * @param questionnaireSubmissionId the questionnaire submission id
     * @param answers                   the map between the id of the question and the answer
     */
    public void submitAnswers(long questionnaireSubmissionId, Map<Long, String> answers) {
        QuestionnaireSubmissionEntity questionnaireSubmission = em.find(QuestionnaireSubmissionEntity.class, questionnaireSubmissionId);

        answers.keySet().forEach(
                questionId -> {
                    QuestionEntity question = em.find(QuestionEntity.class, questionId);
                    AnswerEntity answer = new AnswerEntity(answers.get(questionId), question, questionnaireSubmission);

                    questionnaireSubmission.addAnswer(answer);
                    question.addAnswer(answer);

                    em.persist(question);
                    em.persist(answer);
                }
        );

        em.persist(questionnaireSubmission);

        em.flush();
    }

    /**
     * Update an answer.
     *
     * @param questionId      the question id
     * @param questionnaireId the questionnaire id
     * @param userId          the user id
     * @param answerText      the answer text
     */
    public void updateAnswer(long questionId, long questionnaireId, long userId, String answerText) {
        this.findQuestionnaireSubmission(userId, questionnaireId).ifPresentOrElse(
                questionnaireSubmission -> this.updateAnswer(
                        questionId,
                        questionnaireSubmission.getId(),
                        answerText
                ),
                () -> {
                    throw new IllegalArgumentException(
                            String.format(
                                    "Questionnaire submission for questionnaire with with ID = %d does not exist!",
                                    questionnaireId
                            )
                    );
                }
        );

    }

    /**
     * Update an answer.
     *
     * @param questionId                the question id
     * @param questionnaireSubmissionId the questionnaire submission id
     * @param answerText                the answer text
     */
    public void updateAnswer(long questionId, long questionnaireSubmissionId, String answerText) {
        AnswerEntity answer = em.createNamedQuery("Answer.findAnswerByQuestionAndQuestionnaireSubmission", AnswerEntity.class)
                .setParameter("questionId", questionId)
                .setParameter("questionnaireSubmissionId", questionnaireSubmissionId)
                .getSingleResult();

        answer.setText(answerText);

        em.persist(answer);

        em.flush();
    }

    /**
     * Find answers by a user to a questionnaire.
     *
     * @param userId          the user id
     * @param questionnaireId the questionnaire id
     * @return the list of answers
     */
    public List<AnswerEntity> findAnswers(long userId, long questionnaireId) {
        return em.createNamedQuery("Answer.findAnswersByUserAndQuestionnaire", AnswerEntity.class)
                .setParameter("userId", userId)
                .setParameter("questionnaireId", questionnaireId)
                .getResultList();
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
