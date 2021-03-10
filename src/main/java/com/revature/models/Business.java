package com.revature.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.revature.util.RegexUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name ="business")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Business {

    @Id
    @Column(name="business_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @ManyToOne(targetEntity = User.class, optional = false)
    @JoinColumn(name = "owner_id", columnDefinition = "int4 NOT NULL")
    private User owner;

    //@Pattern(regexp= RegexUtil.EMAIL_REGEX)
    @Column(nullable=false, unique=true)
    private String email;

    @Column(name="business_name", nullable=false)
    private String businessName;

    @Column(nullable=false)
    private String location;

    @Column(name="business_type", nullable=false)
    private String businessType;

    @Column(name="register_datetime", updatable=false, columnDefinition="timestamp default CURRENT_TIMESTAMP")
    private Timestamp registerDatetime;

    @Column(name="is_active")
    private boolean isActive;

    @OneToMany(mappedBy = "business")
    List<BusinessReviews> reviews;

    @OneToMany(mappedBy = "business")
    List<Hours> hours;

    @OneToMany(mappedBy = "business")
    List<Posts> posts;

    @Override
    public String toString() {
        return "Business{" +
                "id=" + id +
                ", ownerId=" + owner.getUserId() +
                ", email='" + email + '\'' +
                ", businessName='" + businessName + '\'' +
                ", location='" + location + '\'' +
                ", businessType='" + businessType + '\'' +
                ", registerDatetime=" + registerDatetime +
                '}';
    }
}
