package com.revature.models;


public enum Role {

    ADMIN("Admin"),
    OWNER("Owner"),
    USER("User");

    private String roleName;

    Role(String name) {
        this.roleName = name;
    }

    public static Role getByName(String name) {
        for (Role role : Role.values()) {
            if (role.roleName.equals(name)) {
                return role;
            }
        }
        return USER;
    }

    @Override
    public String toString() {
        return roleName;
    }

}
