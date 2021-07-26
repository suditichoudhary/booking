package com.hotels.booking.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class BookingRequest {

    /* Created by suditi on 2021-07-25 */
    String city;
    DateModel dateModel;
    FlexibleModel flexibleModel;
    String apartmentType;
    List<String> amenities;


}
