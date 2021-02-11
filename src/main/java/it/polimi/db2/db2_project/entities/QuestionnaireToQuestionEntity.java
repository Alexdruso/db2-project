package it.polimi.db2.db2_project.entities;

import javax.persistence.*;

@Entity
@Table(name = "questionnaire_to_question", schema = "db2", catalog = "")
@IdClass(QuestionnaireToQuestionEntityPK.class)
public class QuestionnaireToQuestionEntity {
    private Long id;
    private int questionnaireId;
    private int questionId;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    @Column(name = "questionnaire_id")
    public int getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(int questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    @Id
    @Column(name = "question_id")
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

        QuestionnaireToQuestionEntity that = (QuestionnaireToQuestionEntity) o;

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
