package com.revature.models;

import javax.persistence.*;
import java.util.Objects;


@Entity
@Table( name = "ers_users", schema = "project_1")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int userId;

    @Column(name = "username", length = 25, unique = true)
    private String username;

    @Column(name = "password", length = 256)
    private String password;

    @Column(name = "first_name", length = 25)
    private String firstname;

    @Column(name = "last_name", length = 25)
    private String lastname;

    @Column(name = "email", length = 256, unique = true)
    private String email;

      @ManyToOne(targetEntity = Role.class)
      @JoinColumn(name = "user_role_id")
      @Enumerated(EnumType.ORDINAL)
    @Column(name = "user_role_id")
    private Integer userRole;

//
}
