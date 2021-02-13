package it.polimi.db2.db2_project.services;

import it.polimi.db2.db2_project.entities.AnswerEntity;
import it.polimi.db2.db2_project.entities.ProductEntity;
import it.polimi.db2.db2_project.entities.QuestionEntity;
import it.polimi.db2.db2_project.entities.QuestionnaireEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SubmissionService {

    public Optional<QuestionnaireEntity> getCurrentQuestionnaire() {
        //TODO Check time from the server
    }

    public List<QuestionEntity> getMarketingQuestions(long questionnaireId) {
        //TODO
    }

    public List<QuestionEntity> getStatisticalQuestions(long questionnaireId) {

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
    }

    public boolean submitStatisticalAnswers(long userId, Map<Long,String> answers) {
        //TODO
    }

    public List<AnswerEntity> getAnswers(long userId, long questionId) {
        //TODO Useful for changing marketing question
    }
}
