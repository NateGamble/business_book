package com.revature.models;

import com.revature.util.RegexUtil;
import com.revature.util.UserRoleConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.security.Timestamp;
import java.util.List;



@Entity @Table(name ="app_users")
@Data @NoArgsConstructor @AllArgsConstructor
public class User {

    @Id @Column(name="user_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int userId;

    @Column(nullable=false, unique=true)
    private String username;

    @Column(nullable=false)
    private String password;

//    @Pattern(regexp=RegexUtil.EMAIL_REGEX)
    @Column(nullable=false, unique=true)
    private String email;

    @Column(name="phone_number")
    private String phoneNumber;

    @Column(name= "firstname", nullable=false)
    private String firstName;

    @Column(name= "lastname", nullable=false)
    private String lastName;

    @Column(name="register_datetime", updatable=false, columnDefinition="timestamp default CURRENT_TIMESTAMP")
    private Timestamp registerDatetime;

    @Column(name="is_active")
    private boolean isActive;

    @Column(name="user_role_id")
    @Convert(converter = UserRoleConverter.class)
    private Role role;

    @ManyToMany
    @JoinTable(
        name = "user_favorites",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "business_id")
    )
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
