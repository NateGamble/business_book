package com.revature.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.revature.util.RegexUtil;
import com.revature.util.UserRoleConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.sql.Timestamp;
import java.util.List;


/**
 * Java POJO for a User of the application
 */
@Entity @Table(name ="app_users")
@Data @NoArgsConstructor @AllArgsConstructor
public class User {

    /**
     * Integer Id for the User
     */
    @Id @Column(name="user_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer userId;

    /**
     * username that is used to login
     */
    @Column(nullable=false, unique=true)
    private String username;

    /**
     * String password that is used to login
     */
    @Column(nullable=false)
    private String password;

    /**
     * Email of the User
     */
//    @Pattern(regexp=RegexUtil.EMAIL_REGEX)
    @Column(nullable=false, unique=true)
    private String email;

    /**
     * Contact phone number for the User
     */
    @Column(name="phone_number")
    private String phoneNumber;

    /**
     * First name of the User
     */
    @Column(name= "firstname", nullable=false)
    private String firstName;

    /**
     * Last name of the User
     */
    @Column(name= "lastname", nullable=false)
    private String lastName;

    /**
     * Timestamp for when the User was registered
     */
    @Column(name="register_datetime", updatable=false, columnDefinition="timestamp default CURRENT_TIMESTAMP")
    private Timestamp registerDatetime;

    /**
     * Boolean for if the User is "deleted"
     */
    @Column(name="is_active")
    private boolean isActive;

    /**
     * ENUM Role for the User
     */
    @Column(name="user_role_id")
    @Convert(converter = UserRoleConverter.class)
    private Role role;

    /**
     * List of Businesses that the User has favorited
     */
    @ManyToMany
    @JoinTable(
        name = "user_favorites",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "business_id")
    )
    @JsonIgnore
    private List<Business> favorites;

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", firstname='" + firstName + '\'' +
                ", lastname='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", registerDatetime=" + registerDatetime +
                '}';
    }

}
