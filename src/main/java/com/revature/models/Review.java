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

@Entity @Table(name = "business_reviews")
@Data @AllArgsConstructor @NoArgsConstructor
public class Review {

    @Id @Column(name = "review_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn(name = "business_id")
    @ManyToOne(targetEntity = Business.class, optional = false)
    @JsonIgnore
    private Business business;

    @JoinColumn(name = "user_id")
    @ManyToOne(targetEntity = User.class, optional = false)
    @JsonIgnore
    private User user;

    @Column(name = "rating", nullable = false)
    private Double rating;

    @Column(name = "review")
    private String review;
    
}
