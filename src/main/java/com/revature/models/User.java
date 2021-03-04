package com.revature.models;

import com.revature.util.RegexUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity @Table(name ="app_users")
@Data @NoArgsConstructor @AllArgsConstructor
public class User {

    @Id @Column(name="user_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int userId;

    @NotNull @Length(min=5) @NotEmpty
    @Column(nullable=false, unique=true)
    private String username;

    @Pattern(regexp=RegexUtil.PASSWORD_REGEX)
    @Column(nullable=false)
    private String password;

    @Pattern(regexp=RegexUtil.EMAIL_REGEX)
    @Column(nullable=false, unique=true)
    private String email;

    @Column
    private String phoneNumber;

    @NotNull @NotEmpty
    @Column(nullable=false)
    private String firstname;

    @NotNull @NotEmpty
    @Column(nullable=false)
    private String lastname;

    @Column(name="register_datetime", updatable=false, columnDefinition="timestamp default CURRENT_TIMESTAMP")
    private LocalDateTime registerDatetime;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId && username.equals(user.username) && password.equals(user.password) && Objects.equals(firstname, user.firstname) && Objects.equals(lastname, user.lastname) && Objects.equals(email, user.email) && Objects.equals(phoneNumber, user.phoneNumber) && registerDatetime.equals(user.registerDatetime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, password, firstname, lastname, email, phoneNumber, registerDatetime);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", registerDatetime=" + registerDatetime +
                '}';
    }

//
}
