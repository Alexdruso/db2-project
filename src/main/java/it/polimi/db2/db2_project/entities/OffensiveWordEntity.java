package it.polimi.db2.db2_project.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "offensive_word", schema = "db2")
@NamedQuery(name = "OffensiveWord.findAll", query = "SELECT w FROM OffensiveWordEntity w")
public class OffensiveWordEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "word", nullable = false)
    private String word;

    public String getWord() {
        return word;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OffensiveWordEntity that = (OffensiveWordEntity) o;

        return Objects.equals(word, that.word);
    }

    @Override
    public int hashCode() {
        return word != null ? word.hashCode() : 0;
    }
}
