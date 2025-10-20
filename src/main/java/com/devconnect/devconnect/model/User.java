package com.devconnect.devconnect.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

// Jakarta Persistence API(jakarta.persistence)
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
// ...existing code...
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

import jakarta.validation.constraints.*;

@Entity
@Table(
    name = "users",
    indexes = {
        @Index(name = "idx_user_email", columnList = "email"),
    }
    // The below is redundant given the individual unique constraints on username and email
    // uniqueConstraints = {
    //     @UniqueConstraint(columnNames = {"username","email"})//Combined unique constraint on username and email(meaning a combination of username and email can't be duplicated)
    // }
)
public class User {
    // User entity fields and methods
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank // Ensures username is not null or empty at the validation level
    @Size(min = 3, max = 50)
    @Column(nullable = false, unique = true) // Ensures username is unique and not null in the database
    private String username;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Size(min = 8)
    @JsonIgnore // Prevents password from being serialized to JSON meaning it won't be exposed in API responses. Serialization is the process of converting an object into a format that can be easily stored or transmitted, such as JSON or XML. By adding @JsonIgnore to the password field, we ensure that when a User object is converted to JSON (for example, when sending user data in an API response), the password field will be omitted from the output. This is a crucial security measure to prevent sensitive information like passwords from being exposed in API responses or logs.
    @Column(nullable = false)
    private String password;

    @Size(max = 255)
    private String bio;

    private String profilePictureUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER; // Default role

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // --- Relationships ---
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY) // 'user' should match the field name in Post entity. 'user' is the "one" side of the relationship
    @JsonIgnore
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY) // 'user' should match the field name in Comment entity
    @JsonIgnore
    private List<Comment> comments = new ArrayList<>(); 

    // No-arg constructor required by JPA and used by other classes (make public so seeders/controllers can instantiate)
    public User() {}

    public User(String username, String email, String password, String bio, String profilePictureUrl) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.bio = bio;
        this.profilePictureUrl = profilePictureUrl;
        this.role = Role.USER;
    }

    public User(String username, String email, String password, String bio, String profilePictureUrl, Role role, LocalDateTime createdAt) {
        this(username, email, password, bio, profilePictureUrl);
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
    }

    // --- Lifecycle callbacks ---
    @PrePersist // Called before the row is persisted (inserted into the database)
    protected void onCreate() {
        if (createdAt == null) { // Allow setting createdAt manually for testing/seeding
            createdAt = LocalDateTime.now();
        }
        updatedAt = createdAt;
    }   

    @PreUpdate // Called before the entity is updated in the database
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    //  Helper functions to manage bi-directional relationships. Bi-directional means both entities have references to each other.
    //  This helps keep both sides of the relationship in sync when adding/removing posts or comments.
    public void addPost(Post post) {
        posts.add(post);
        post.setUser(this); // Set the user in the Post entity
    }

    public void removePost(Post post) {
        posts.remove(post);
        post.setUser(null); // Remove the user reference in the Post entity
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setUser(this); // Set the user in the Comment entity
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setUser(null); // Remove the user reference in the Comment entity
    }


    // --- Getters and Setters ---
    public Long getId() {
        return id;
    }
    protected void setId(Long id) {
        this.id = id;
    }

    public String getUsername(){
        return username;
    }   
    public void setUsername(String username){
        this.username = username;
    }

    public String getEmail(){
        return email;
    }               
    public void setEmail(String email){
        this.email = email;
    }

    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    } 

    public String getBio(){
        return bio;
    }
    public void setBio(String bio){
        this.bio = bio;
    }

    public String getProfilePictureUrl(){
        return profilePictureUrl;
    }
    public void setProfilePictureUrl(String profilePictureUrl){
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getRole(){
        return role.name();
    }
    public void setRole(Role role){
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<Post> getPosts() {
        return posts;
    }
    public List<Comment> getComments() {
        return comments;
    }

    // Role enum stored as STRING in the DB
    public enum Role {
        USER, ADMIN, MODERATOR
    }
}