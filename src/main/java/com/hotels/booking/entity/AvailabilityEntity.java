package com.hotels.booking.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "availability")
public class AvailabilityEntity {

    /* Created by suditi on 2021-07-27 */
    @Column(name = "id",nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "property_id")
    private PropertyEntity propertyEntity;
    @Column(name= "start_date")
    private String start_date;
    @Column(name= "end_date")
    private String end_date;
    @Column(name= "is_blocked")
    private boolean is_blocked;
}
