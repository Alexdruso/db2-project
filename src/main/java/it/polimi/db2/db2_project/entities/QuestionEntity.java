package it.polimi.db2.db2_project.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "question", schema = "db2")
@NamedQueries(
        {
                @NamedQuery(
                        name = "Question.findByQuestionnaire",
                        query = "SELECT q " +
                                "FROM QuestionEntity q JOIN QuestionnaireEntity qr " +
                                "WHERE q.optional = :optional AND qr.id = :questionnaireId"
                )
        }
)
public class QuestionEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(nullable = false)
    private String text;
    @Column(nullable = false)
    private Boolean optional;

    //relationships definition part
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "questionnaire_to_question", schema = "db2",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "questionnaire_id"))
    private List<QuestionnaireEntity> questionnaires = new ArrayList<>();

    @OneToMany(mappedBy = "question",
            cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<AnswerEntity> answers;


    public List<QuestionnaireEntity> getQuestionnaires() {
        return questionnaires;
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

    @Basic
    @Column(name = "optional")
    public Boolean getOptional() {
        return optional;
    }

    public void setOptional(Boolean optional) {
        this.optional = optional;
    }

    public List<AnswerEntity> getAnswers() {
        return answers;
    }

    public void addAnswer(AnswerEntity answer) {
        answers.add(answer);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        QuestionEntity that = (QuestionEntity) o;

        if (optional != that.optional)
            return false;
        if (!Objects.equals(id, that.id))
            return false;
        return Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (optional != null ? optional.hashCode() : 0);
        return result;
    }
}
