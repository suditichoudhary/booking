package com.hotels.booking.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class AvailabilityResponse {

    /* Created by suditi on 2021-07-25 */
    List<Long> match;
    List<AlternativeUnits> alternative;
    List<AlternativeUnits> other;
}
