package com.revature.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;

/**
 * Java POJO for a Post on a Business
 */
@Entity
@Table(name ="posts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    /**
     * Integer Id for the Post
     */
    @Id
    @Column(name="post_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer postId;

    /**
     * Business object that the Post corresponds to
     */
    @ManyToOne(targetEntity = Business.class, optional = false)
    @JoinColumn(name = "business_id", columnDefinition = "int4 NOT NULL")
    @JsonIgnore
    private Business business;

    /**
     * The String type of the Post
     */
    @Column(name="post_type", nullable=false)
    private String postType;

    /**
     * String body of the Post
     */
    @Column(nullable=false)
    private String body;

    /**
     * Timestamp for when the Post was created
     */
    @Column(name="created_time", updatable=false, columnDefinition="timestamp default CURRENT_TIMESTAMP")
    private Timestamp createdTime;

}
