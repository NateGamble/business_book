package com.revature.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;

/**
 * Java POJO class for Business Hours
 */
@Entity
@Table(name ="business_hours")
@Data
@AllArgsConstructor @NoArgsConstructor
public class Hours {

    /**
     * Integer Id value
     */
    @Id
    @Column(name="hours_id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer hoursId;

    /**
     * The Business the Hours refer too
     */
    @ManyToOne(targetEntity = Business.class, optional = false)
    @JoinColumn(name = "business_id", columnDefinition = "int4 NOT NULL")
    @JsonIgnore
    private Business business;

    /**
     * int value corresponding to the day of the week (0 = Sunday -> 6 = Saturday)
     */
    @Column(nullable=false)
    private int day;

    /**
     * Timestamp for the open time of the day
     */
    @Column(nullable = false)
    private Timestamp open;

    /**
     * Timestamp for the close time of the day
     */
    @Column(nullable = false)
    private Timestamp closed;

}
