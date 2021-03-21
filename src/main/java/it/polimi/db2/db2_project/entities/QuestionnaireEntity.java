package it.polimi.db2.db2_project.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "questionnaire", schema = "db2")
@NamedQueries(
        {
                @NamedQuery(
                        name = "Questionnaire.findById",
                        query = "SELECT q " +
                                "FROM QuestionnaireEntity q " +
                                "WHERE q.id = :id"
                ),
                @NamedQuery(
                        name = "Questionnaire.findByDate",
                        query = "SELECT q " +
                                "FROM QuestionnaireEntity q " +
                                "WHERE q.date = :date"
                ),
                @NamedQuery(
                        name = "Questionnaire.findAll",
                        query = "SELECT q " +
                                "FROM QuestionnaireEntity q"
                ),
                @NamedQuery(
                        name = "Questionnaire.findSubmitters",
                        query = "SELECT s.user " +
                                "FROM QuestionnaireSubmissionEntity s " +
                                "WHERE s.points > 0 AND s.questionnaire.id = :id  "
                ),
                @NamedQuery(
                        name = "Questionnaire.findCancellation",
                        query = "SELECT s.user " +
                                "FROM QuestionnaireSubmissionEntity s " +
                                "WHERE s.points = 0 AND s.questionnaire.id = :id  "
                )

        }
)
public class QuestionnaireEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private Date date;

    //relationships definition part
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "questionnaire_to_question", schema = "db2",
            joinColumns = @JoinColumn(name = "questionnaire_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id"))
    private List<QuestionEntity> questions = new ArrayList<>();

    @OneToMany(mappedBy = "questionnaire", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<QuestionnaireSubmissionEntity> questionnaireSubmissions = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserEntity user;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    public QuestionnaireEntity() {
    }

    public QuestionnaireEntity(Date date, UserEntity user, ProductEntity product) {
        this.date = date;
        this.user = user;
        this.product = product;
    }

    public ProductEntity getProductEntity() {
        return product;
    }

    public void setProductEntity(ProductEntity productEntity) {
        this.product = productEntity;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity userEntity) {
        this.user = userEntity;
    }

    public List<QuestionnaireSubmissionEntity> getQuestionnaireSubmissions() {
        return questionnaireSubmissions;
    }

    public void addQuestionnaireSubmission(QuestionnaireSubmissionEntity questionnaireSubmission) {
        this.questionnaireSubmissions.add(questionnaireSubmission);
    }

    public void setQuestions(List<QuestionEntity> questions) {
        this.questions = questions;
    }

    public List<QuestionEntity> getQuestions() {
        return questions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "date")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuestionnaireEntity that = (QuestionnaireEntity) o;

        if (!Objects.equals(id, that.id)) return false;
        return Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }
}
