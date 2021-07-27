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
@Table(name = "reservation")
public class ReservationEntity {

    /* Created by suditi on 2021-07-27 */
    @Column(name = "id",nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "property_id")
    private PropertyEntity propertyEntity;
    @Column(name= "check_in")
    private String check_in;
    @Column(name= "check_out")
    private String check_out;

}
