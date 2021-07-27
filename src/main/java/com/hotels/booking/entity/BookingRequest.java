package com.hotels.booking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingRequest {

    /* Created by suditi on 2021-07-25 */
    String city;
    @JsonProperty("date")
    DateModel dateModel;
    @JsonProperty("flexible")
    FlexibleModel flexibleModel;
    String apartmentType;
    List<String> amenities;
    @JsonIgnore
    boolean flagFlexi=false;


}
