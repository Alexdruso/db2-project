package it.polimi.db2.db2_project.entities;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "question", schema = "db2")
public class QuestionEntity {
    private Long id;
    private String text;
    private byte optional;

    @ManyToMany
    @JoinTable(name = "questionnaire_to_question", schema = "db2",
            joinColumns = @JoinColumn(name = "questionnaire_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id"))
    private List<QuestionnaireEntity> questionnaires;
    @OneToMany(mappedBy = "question_id",
            cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<AnswerEntity> answers;
    
    @Id
    @GeneratedValue
    @Column(name = "id")
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
    public byte getOptional() {
        return optional;
    }

    public void setOptional(byte optional) {
        this.optional = optional;
    }





    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuestionEntity that = (QuestionEntity) o;

        if (optional != that.optional) return false;
        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(text, that.text)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (int) optional;
        return result;
    }
}
