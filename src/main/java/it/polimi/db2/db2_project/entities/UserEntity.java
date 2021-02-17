package it.polimi.db2.db2_project.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "user", schema = "db2")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    private Date lastLogin;
    @Column(nullable = false)
    private byte ban;
    private Byte admin;

    //relationships definition part
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionnaireEntity> questionnaires;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionnaireSubmissionEntity> questionnaireSubmissions;

    public Long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = (long) id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "last_login")
    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Basic
    @Column(name = "ban")
    public byte getBan() {
        return ban;
    }

    public void setBan(byte ban) {
        this.ban = ban;
    }

    @Basic
    @Column(name = "admin")
    public Byte getAdmin() {
        return admin;
    }

    public void setAdmin(Byte admin) {
        this.admin = admin;
    }


    public List<QuestionnaireEntity> getQuestionnaires() {
        return questionnaires;
    }

    public void addQuestionnaire(QuestionnaireEntity questionnaire) {
        this.questionnaires.add(questionnaire);
    }

    public List<QuestionnaireSubmissionEntity> getQuestionnaireSubmissions() {
        return questionnaireSubmissions;
    }

    public void addQuestionnaireSubmission(QuestionnaireSubmissionEntity questionnaireSubmission) {
        this.questionnaireSubmissions.add(questionnaireSubmission);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity that = (UserEntity) o;

        if (ban != that.ban) return false;
        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(username, that.username)) return false;
        if (!Objects.equals(password, that.password)) return false;
        if (!Objects.equals(email, that.email)) return false;
        if (!Objects.equals(lastLogin, that.lastLogin)) return false;
        return Objects.equals(admin, that.admin);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (lastLogin != null ? lastLogin.hashCode() : 0);
        result = 31 * result + (int) ban;
        result = 31 * result + (admin != null ? admin.hashCode() : 0);
        return result;
    }
}
