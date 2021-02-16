package it.polimi.db2.db2_project.services;

import it.polimi.db2.db2_project.entities.AnswerEntity;
import it.polimi.db2.db2_project.entities.QuestionEntity;
import it.polimi.db2.db2_project.entities.QuestionnaireEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SubmissionService {

    public Optional<QuestionnaireEntity> getCurrentQuestionnaire() {
        //TODO Check time from the server
        return null;
    }

    public List<QuestionEntity> getMarketingQuestions(long questionnaireId) {
        //TODO
        return null;
    }

    public List<QuestionEntity> getStatisticalQuestions(long questionnaireId) {
        //TODO
        return null;
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

    public List<AnswerEntity> getAnswers(long userId, long questionId) {
        //TODO Useful for changing marketing question
        return null;
    }
}
