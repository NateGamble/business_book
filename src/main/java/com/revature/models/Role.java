package com.revature.models;

//@Entity
//@Table(name = "ers_user_roles")
public enum Role {
    //no role_id here ?

    ADMIN("Admin"),
    FINANCE_MANAGER("Finance Manager"),
    EMPLOYEE("Employee"),
    DELETED("Deleted");

//    @Column(name = "role_name")
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
        return EMPLOYEE;
    }

    @Override
    public String toString() {
        return roleName;
    }

}
