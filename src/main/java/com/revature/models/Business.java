package com.revature.models;

import java.sql.Timestamp;
import java.util.Objects;

public class Business {
    private int id;
    private int ownerId;
    private String email;
    private String businessName;
    private String location;
    private String businessType;
    private Timestamp registerDatetime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public Timestamp getRegisterDatetime() {
        return registerDatetime;
    }

    public void setRegisterDatetime(Timestamp registerDatetime) {
        this.registerDatetime = registerDatetime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Business business = (Business) o;
        return id == business.id &&
                ownerId == business.ownerId &&
                Objects.equals(email, business.email) &&
                Objects.equals(businessName, business.businessName) &&
                Objects.equals(location, business.location) &&
                Objects.equals(businessType, business.businessType) &&
                Objects.equals(registerDatetime, business.registerDatetime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ownerId, email, businessName, location, businessType, registerDatetime);
    }

    @Override
    public String toString() {
        return "Business{" +
                "id=" + id +
                ", ownerId=" + ownerId +
                ", email='" + email + '\'' +
                ", businessName='" + businessName + '\'' +
                ", location='" + location + '\'' +
                ", businessType='" + businessType + '\'' +
                ", registerDatetime=" + registerDatetime +
                '}';
    }
}
