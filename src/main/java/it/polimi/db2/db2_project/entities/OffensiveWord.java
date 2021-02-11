package it.polimi.db2.db2_project.entities;

import javax.persistence.*;


/**
 * The type Offensive word.
 */
@Table(name = "OFFENSIVE_WORD")
@Entity
@NamedQuery(
        name="findAllOffensiveWords",
        query="SELECT w FROM OffensiveWord w"
)
public class OffensiveWord {
    @Id
    @Column(name = "WORD", nullable = false)
    private String word;

    /**
     * Gets word.
     *
     * @return the word
     */
    public String getWord() {
        return word;
    }

    /**
     * Sets word.
     *
     * @param word the word
     */
    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OffensiveWord that = (OffensiveWord) o;

        return word != null && word.equals(that.word);
    }

    @Override
    public int hashCode() {
        return this.getWord().hashCode();
    }
}