package it.polimi.db2.db2_project.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "offensive_word", schema = "db2")
public class OffensiveWordEntity {
    private Long id;
    private String word;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    @Column(name = "word")
    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OffensiveWordEntity that = (OffensiveWordEntity) o;

        if (!Objects.equals(word, that.word)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return word != null ? word.hashCode() : 0;
    }
}
