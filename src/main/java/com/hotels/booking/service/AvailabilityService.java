package com.hotels.booking.service;

import com.hotels.booking.entity.AvailabilityResponse;
import com.hotels.booking.entity.BookingRequest;
import com.hotels.booking.entity.BuildingEntity;
import com.hotels.booking.entity.Response;
import com.hotels.booking.util.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvailabilityService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AvailabilityService.class);

    /* Created by suditi on 2021-07-25 */

    public final BuildingService buildingService;
    public AvailabilityService(BuildingService buildingService) {
        this.buildingService=buildingService;
    }

    public ResponseEntity<?> getHotelAvailability(BookingRequest bookingRequest){
        Response response = new Response();
        try {
            LOGGER.info("getHotelAvailability request :dto:{}", Utility.toJson(bookingRequest));

            response = validateRequest(bookingRequest);
            //response.

            List<BuildingEntity> buildingEntityList = buildingService.getCityAndBuildingId(bookingRequest.getCity());

            if (true) {
                response.setCode(HttpStatus.OK.value());
                response.setData(new AvailabilityResponse());
                response.setMessage("success");

            }else{
                response.setMessage("Couldnt find any such req");
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.info("Exception occurred while saveWalletCategory the object:{}", e.getMessage());
            return getErrorResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<?> getErrorResponseEntity(Response response, HttpStatus httpStatus) {
        response.setMessage(httpStatus.getReasonPhrase());
        response.setCode(httpStatus.value());
        return new ResponseEntity<>(response, httpStatus);
    }

    public Response validateRequest(BookingRequest bookingRequest){
        Response response = new Response();
        try {
            LOGGER.info("getHotelAvailability request :dto:{}", Utility.toJson(bookingRequest));
            response.setCode(HttpStatus.OK.value());
            response.setMessage("WalletCategory created successfully");
            return response;
        } catch (Exception e) {
            LOGGER.info("Exception occurred while saveWalletCategory the object:{}", e.getMessage());
            response.setCode()
            return response;
        }
    }

}
