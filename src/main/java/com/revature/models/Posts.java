package com.revature.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name ="posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Posts {

    @Id
    @Column(name="post_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int postId;

    @ManyToOne(targetEntity = Business.class, optional = false)
    @JoinColumn(name = "business_id", columnDefinition = "int4 NOT NULL")
    private Business business;

    @Column(name="post_type", nullable=false)
    private int postType;

    @Column(nullable=false)
    private String body;

    @Column(name="created_time", updatable=false, columnDefinition="timestamp default CURRENT_TIMESTAMP")
    private Timestamp createdTime;

}
