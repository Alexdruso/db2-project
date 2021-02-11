package it.polimi.db2.db2_project.entities;

import javax.persistence.*;

@Entity
@Table(name = "questionnaire_submission", schema = "db2", catalog = "")
public class QuestionnaireSubmissionEntity {
    private Long id;
    private Integer points;

    @Id
    @GeneratedValue
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "points")
    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuestionnaireSubmissionEntity that = (QuestionnaireSubmissionEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (points != null ? !points.equals(that.points) : that.points != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (points != null ? points.hashCode() : 0);
        return result;
    }
}
