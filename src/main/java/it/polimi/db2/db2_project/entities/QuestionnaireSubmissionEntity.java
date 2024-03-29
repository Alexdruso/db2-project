package it.polimi.db2.db2_project.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NamedQueries(
        {
                @NamedQuery(
                        name = "QuestionnaireSubmission.getAllUserAnswer",
                        query = "SELECT a " +
                                "FROM QuestionnaireSubmissionEntity s JOIN s.answers a " +
                                "WHERE s.user.id = :userId AND s.questionnaire.id = :questionnaireId"
                ),
                @NamedQuery(
                        name = "QuestionnaireSubmission.findByUserAndQuestionnaire",
                        query = "SELECT qs " +
                                "FROM QuestionnaireSubmissionEntity qs " +
                                "JOIN UserEntity u ON qs.user.id = u.id " +
                                "JOIN QuestionnaireEntity q on qs.questionnaire.id = q.id " +
                                "WHERE u.id = :userId " +
                                "AND q.id = :questionnaireId"
                ),
                @NamedQuery(
                        name = "QuestionnaireSubmission.findLeaderboardByDate",
                        query = "SELECT qs.user.username, qs.points " +
                                "FROM QuestionnaireSubmissionEntity qs " +
                                "JOIN QuestionnaireEntity q ON qs.questionnaire.id = q.id " +
                                "WHERE q.date = :date AND qs.points <> 0 " +
                                "ORDER BY qs.points DESC"
                )
        }
)
@Table(name = "questionnaire_submission", schema = "db2")
public class QuestionnaireSubmissionEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "points")
    private Integer points;

    //relationships definition part

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, optional = false)
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserEntity user;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, optional = false)
    @JoinColumn(name = "QUESTIONNAIRE_ID", nullable = false)
    private QuestionnaireEntity questionnaire;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "questionnaireSubmission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnswerEntity> answers = new ArrayList<>();

    public QuestionnaireSubmissionEntity() {
    }

    public QuestionnaireSubmissionEntity(UserEntity user, QuestionnaireEntity questionnaire) {
        this.user = user;
        this.questionnaire = questionnaire;
        this.points = 0;
    }

    public List<AnswerEntity> getAnswers() {
        return answers;
    }

    public void addAnswer(AnswerEntity answer) {
        this.answers.add(answer);
    }

    public QuestionnaireEntity getQuestionnaire() {
        return questionnaire;
    }

    public void setQuestionnaire(QuestionnaireEntity questionnaire) {
        this.questionnaire = questionnaire;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPoints() {
        return points;
    }

    public boolean isSubmitted() {
        return points > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuestionnaireSubmissionEntity that = (QuestionnaireSubmissionEntity) o;

        if (!Objects.equals(id, that.id)) return false;
        return Objects.equals(points, that.points);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (points != null ? points.hashCode() : 0);
        return result;
    }
}
