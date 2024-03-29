package it.polimi.db2.db2_project.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "answer", schema = "db2")
@NamedQueries(
        {
                @NamedQuery(
                        name = "Product.findReviewsByProduct",
                        query = "SELECT a " +
                                "FROM AnswerEntity a " +
                                "WHERE a.questionnaireSubmission.questionnaire.product.id = :productId"
                ),
                @NamedQuery(
                        name = "Answer.findAnswersByUserAndQuestionnaire",
                        query = "SELECT a " +
                                "FROM AnswerEntity a " +
                                "JOIN QuestionnaireSubmissionEntity qs " +
                                "JOIN QuestionnaireEntity q " +
                                "JOIN UserEntity u " +
                                "WHERE u.id = :userId " +
                                "AND q.id = :questionnaireId"
                ),
                @NamedQuery(
                        name = "Answer.findAnswerByQuestionAndQuestionnaireSubmission",
                        query = "SELECT a " +
                                "FROM AnswerEntity a " +
                                "JOIN QuestionnaireSubmissionEntity qs " +
                                "JOIN QuestionEntity q " +
                                "WHERE q.id = :questionId " +
                                "AND qs.id = :questionnaireSubmissionId"
                )
        }
)
public class AnswerEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(nullable = false)
    private String text;

    //relationships definition part
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "question_id")
    private QuestionEntity question;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, optional = false)
    @JoinColumn(name = "QUESTIONNAIRE_SUBMISSION_ID", nullable = false)
    private QuestionnaireSubmissionEntity questionnaireSubmission;

    public AnswerEntity() {
    }

    public AnswerEntity(String text, QuestionEntity question, QuestionnaireSubmissionEntity questionnaireSubmission) {
        this.text = text;
        this.question = question;
        this.questionnaireSubmission = questionnaireSubmission;
    }

    public void setQuestion(QuestionEntity question) {
        this.question = question;
    }

    public QuestionEntity getQuestion() {
        return question;
    }

    public QuestionnaireSubmissionEntity getQuestionnaireSubmission() {
        return questionnaireSubmission;
    }

    public void setQuestionnaireSubmission(QuestionnaireSubmissionEntity questionnaireSubmission) {
        this.questionnaireSubmission = questionnaireSubmission;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Basic
    @Column(name = "text")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnswerEntity that = (AnswerEntity) o;

        if (!Objects.equals(id, that.id)) return false;
        return Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }
}
