package com.revature.models;

import com.revature.util.RegexUtil;
import com.revature.util.UserRoleConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity @Table(name ="app_users")
@Data @NoArgsConstructor @AllArgsConstructor
public class User {

    @Id @Column(name="user_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int userId;

//    @NotNull @Length(min=5) @NotEmpty
    @Column(nullable=false, unique=true)
    private String username;

    //@Pattern(regexp=RegexUtil.PASSWORD_REGEX)
    @Column(nullable=false)
    private String password;

    @Pattern(regexp=RegexUtil.EMAIL_REGEX)
    @Column(nullable=false, unique=true)
    private String email;

    @Column(name="phone_number")
    private String phoneNumber;

//    @NotNull @NotEmpty
    @Column(nullable=false)
    private String firstname;

//    @NotNull @NotEmpty
    @Column(nullable=false)
    private String lastname;

    @Column(name="register_datetime", updatable=false, columnDefinition="timestamp default CURRENT_TIMESTAMP")
    private Timestamp registerDatetime;

    //?????????????????????????????????????????????????????
    @Column(name="user_role_id")
    @Convert(converter = UserRoleConverter.class)
    private Role roleId;

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", registerDatetime=" + registerDatetime +
                '}';
    }

//
}
