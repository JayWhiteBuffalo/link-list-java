package com.technews.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "user_attributes")
public class UserAttributes implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne(mappedBy = "userAttributes", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private User user;

    private String profilePicture;
    private String userHeadline;
    private String aboutSection;
    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Post> savedPosts;
    private int karma;
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date created_date;
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date created_time;

    private LocalDateTime lastLogin;

    public UserAttributes() {

    }

    public UserAttributes(User user, String profilePicture, String userHeadline, String aboutSection) {
        this.user = user;
        this.profilePicture = profilePicture != null ? profilePicture : "";
        this.userHeadline = userHeadline != null ? userHeadline : "";
        this.aboutSection = aboutSection != null ? aboutSection : "";
    }




    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getUserHeadline() {
        return userHeadline;
    }

    public void setUserHeadline(String userHeadline) {
        this.userHeadline = userHeadline;
    }

    public String getAboutSection() {
        return aboutSection;
    }

    public void setAboutSection(String aboutSection) {
        this.aboutSection = aboutSection;
    }

    public int getKarma() {
        return karma;
    }

    public void setKarma(int karma) {
        this.karma = karma;
    }

    public List<Post> getSavedPosts() {
        return savedPosts;
    }

    public void setSavedPosts(List<Post> savedPosts) {
        this.savedPosts = savedPosts;
    }

    public Date getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }

    public Date getCreated_time() {
        return created_time;
    }

    public void setCreated_time(Date created_time) {
        this.created_time = created_time;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    @PrePersist
    @PreUpdate
    public void updateLastLogin() {
        this.lastLogin = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "UserAttributes{" +
                "id=" + id +
                ", user=" + user +
                ", profilePicture='" + profilePicture + '\'' +
                ", userHeadline='" + userHeadline + '\'' +
                ", aboutSection='" + aboutSection + '\'' +
                ", savedPosts=" + savedPosts +
                ", karma=" + karma +
                ", created_date=" + created_date +
                ", created_time=" + created_time +
                ", lastLogin=" + lastLogin +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAttributes that = (UserAttributes) o;
        return getKarma() == that.getKarma() && Objects.equals(getId(), that.getId()) && Objects.equals(getUser(), that.getUser()) && Objects.equals(getProfilePicture(), that.getProfilePicture()) && Objects.equals(getUserHeadline(), that.getUserHeadline()) && Objects.equals(getAboutSection(), that.getAboutSection()) && Objects.equals(savedPosts, that.savedPosts) && Objects.equals(created_date, that.created_date) && Objects.equals(created_time, that.created_time) && Objects.equals(getLastLogin(), that.getLastLogin());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUser(), getProfilePicture(), getUserHeadline(), getAboutSection(), savedPosts, getKarma(), created_date, created_time, getLastLogin());
    }
}
