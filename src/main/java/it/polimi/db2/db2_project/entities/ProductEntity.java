package it.polimi.db2.db2_project.entities;

import it.polimi.db2.db2_project.web.utils.ImageUtil;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "product", schema = "db2")
@NamedQueries(
        {
                @NamedQuery(
                        name = "Product.findByDate",
                        query = "SELECT p " +
                                "FROM ProductEntity p JOIN QuestionnaireEntity q ON p.id = q.product.id " +
                                "WHERE q.date = :date"
                ),
                @NamedQuery(
                        name = "Product.findAll",
                        query = "SELECT p " +
                                "FROM ProductEntity p"
                ),
        }
)
public class ProductEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Lob
    @Column
    private byte[] image;

    //relationships definition part

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionnaireEntity> questionnaire;

    public List<QuestionnaireEntity> getQuestionnaires() {
        return questionnaire;
    }

    public void setQuestionnaires(List<QuestionnaireEntity> questionnaire) {
        this.questionnaire = questionnaire;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "image")
    public byte[] getImage() {
        return image;
    }

    public String getImageBase64() {
        return ImageUtil.getImgData(getImage());
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductEntity that = (ProductEntity) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(name, that.name)) return false;
        return Arrays.equals(image, that.image);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(image);
        return result;
    }
}
