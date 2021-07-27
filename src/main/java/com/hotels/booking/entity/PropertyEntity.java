package com.hotels.booking.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
@Table(name = "property")
public class PropertyEntity {

    /* Created by suditi on 2021-07-27 */

    @Column(name = "id",nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "building_id")
    private BuildingEntity buildingEntity;
    @Column(name= "title")
    private String title;
    @Column(name= "property_type")
    private String property_type;
    @Column(name= "amenities")
    private String amenities;

}
