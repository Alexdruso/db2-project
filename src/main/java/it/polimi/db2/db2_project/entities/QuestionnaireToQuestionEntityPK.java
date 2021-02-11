package it.polimi.db2.db2_project.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class QuestionnaireToQuestionEntityPK implements Serializable {
    private int questionnaireId;
    private int questionId;

    @Column(name = "questionnaire_id")
    @Id
    public int getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(int questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    @Column(name = "question_id")
    @Id
    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuestionnaireToQuestionEntityPK that = (QuestionnaireToQuestionEntityPK) o;

        if (questionnaireId != that.questionnaireId) return false;
        if (questionId != that.questionId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = questionnaireId;
        result = 31 * result + questionId;
        return result;
    }
}
