package com.revature.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Java POJO for a Review of a Business
 */
@Entity @Table(name = "business_reviews")
@Data @AllArgsConstructor @NoArgsConstructor
public class Review {

    /**
     * Integer Id of the Review
     */
    @Id @Column(name = "review_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Business that the Review is for
     */
    @JoinColumn(name = "business_id")
    @ManyToOne(targetEntity = Business.class, optional = false)
    @JsonIgnore
    private Business business;

    /**
     * User that created the Review
     */
    @JoinColumn(name = "user_id")
    @ManyToOne(targetEntity = User.class, optional = false)
    @JsonIgnore
    private User user;

    /**
     * Rating provided by the User
     */
    @Column(name = "rating", nullable = false)
    private Double rating;

    /**
     * String content of the Review
     */
    @Column(name = "review")
    private String review;
    
}
