package com.revature.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.revature.models.Role;
import com.revature.models.User;

public class Principal {

    @JsonIgnore
    private String token;

    private int id;
    private String username;
    private Role role;

    public Principal() {
        super();
    }

    public Principal(User user) {
        this.id = user.getUserId();
        this.username = user.getUsername();
        this.role = user.getRole();
    }
    public Principal(int id, String username, Role role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
