package it.polimi.db2.db2_project.services;

import it.polimi.db2.db2_project.entities.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class AdminService {
    public Optional<QuestionnaireEntity> createQuestionnaire(long productId, Date date) {
        //TODO
    }

    public boolean deleteQuestionnaire(long questionnaireId) {
        //TODO
    }

    public Optional<QuestionnaireEntity> addMarketingQuestion(long questionnaireId, String question) {
        //TODO
    }

    public List<QuestionnaireEntity> getAllQuestionnaires() {
        //TODO
    }

    public List<UserEntity> getAllSubmitters(long questionnaireId) {
        //TODO
    }

    public List<UserEntity> getAllCancellations(long questionnaireId) {
        //TODO
    }

    public List<AnswerEntity> getAllAnswersByUser(long userId, long questionnaireId) {
        //TODO Exploit the full ORM potential
    }
}
