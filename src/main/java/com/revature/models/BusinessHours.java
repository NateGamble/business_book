package com.revature.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name ="business_hours")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessHours {

    @Id
    @Column(name="hours_id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int hoursId;

    @ManyToOne(targetEntity = Business.class, optional = false)
    @JoinColumn(name = "business_id", columnDefinition = "int4 NOT NULL")
    private Business business;

    @Column(nullable=false)
    private String day;

    @Column(nullable = false)
    private Timestamp open;

    @Column(nullable = false)
    private Timestamp closed;

}
