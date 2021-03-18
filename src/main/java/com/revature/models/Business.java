package com.revature.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * Java POJO representation of a Business in the Database
 */
@Entity
@Table(name ="business")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Business {

    /**
     * Integer Id value of the Business
     */
    @Id
    @Column(name="business_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    /**
     * The User Object for the Owner of the Business
     */
    @ManyToOne(targetEntity = User.class, optional = false)
    @JoinColumn(name = "owner_id", columnDefinition = "int4 NOT NULL")
    private User owner;

    /**
     * The String for the email of the Business
     */
    //@Pattern(regexp= RegexUtil.EMAIL_REGEX)
    @Column(nullable=false, unique=true)
    private String email;

    /**
     * The String value of the Business name
     */
    @Column(name="business_name", nullable=false)
    private String businessName;

    /**
     * String for the address of the Business
     */
    @Column(nullable=false)
    private String location;

    /**
     * String for the Business type
     */
    @Column(name="business_type", nullable=false)
    private String businessType;

    /**
     * Timestamp for when the Business was created
     */
    @Column(name="register_datetime", updatable=false, columnDefinition="timestamp default CURRENT_TIMESTAMP")
    private Timestamp registerDatetime;

    /**
     * Boolean for if the Business is "deleted" or not
     */
    @Column(name="is_active")
    private boolean isActive;

    /**
     * List of Reviews for the Business
     */
    @OneToMany(mappedBy = "business")
    List<Review> reviews;

    /**
     * List of Hours for the Business
     */
    @OneToMany(mappedBy = "business")
    List<Hours> hours;

    /**
     * List of Posts for the Business
     */
    @OneToMany(mappedBy = "business")
    List<Post> posts;

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
