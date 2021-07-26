package com.hotels.booking.controller;

import com.hotels.booking.entity.BookingRequest;
import com.hotels.booking.service.AvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class BookingController {

    /* Created by suditi on 2021-07-25 */

    private final AvailabilityService availabilityService;

    @Autowired
    public BookingController(AvailabilityService availabilityResponse) {
        this.availabilityService=availabilityResponse;
    }

    @PostMapping("/v1/availability")
    public ResponseEntity<?> captureAndvalidateAccountAdmin(@RequestBody BookingRequest bookingRequest) {
        return availabilityService.getHotelAvailability(bookingRequest);
    }


}
