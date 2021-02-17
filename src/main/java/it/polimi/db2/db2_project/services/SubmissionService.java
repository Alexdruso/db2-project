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
        return findQuestionnaire(new Date());
    }

    public Optional<QuestionnaireEntity> findQuestionnaire(Date date) {
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


    public void submitAnswers(long questionnaireId, long userId, Map<Long, String> answers) {
        submitAnswers(
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

    public void updateAnswer(long questionId, long questionnaireSubmissionId, String answerText) {
        AnswerEntity answer = em.createNamedQuery("Answer.findAnswerByQuestionAndQuestionnaireSubmission", AnswerEntity.class)
                .setParameter("questionId", questionId)
                .setParameter("questionnaireSubmissionId", questionnaireSubmissionId)
                .getSingleResult();

        answer.setText(answerText);

        em.persist(answer);

        em.flush();
    }

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
