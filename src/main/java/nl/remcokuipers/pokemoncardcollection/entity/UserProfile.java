package nl.remcokuipers.pokemoncardcollection.entity;

import jakarta.persistence.*;
import nl.remcokuipers.pokemoncardcollection.enums.Gender;

import java.time.LocalDate;

@Entity
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String displayName;
    private String country;
    private LocalDate dateOfBirth;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String profilePicturePath;
    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    public UserProfile() {
    }

    public UserProfile(String displayName, User user) {
        this.displayName = displayName;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
