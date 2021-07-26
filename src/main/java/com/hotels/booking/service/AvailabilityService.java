package com.hotels.booking.service;

import com.hotels.booking.entity.*;
import com.hotels.booking.util.DateFormatUtil;
import com.hotels.booking.util.Utility;
import io.micrometer.core.instrument.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.EnumUtils;

import java.time.LocalDate;
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
            if(response.getMessage().equalsIgnoreCase("success")){
                // proceed further
                System.out.println("I am here");
                List<BuildingEntity> buildingEntityList = buildingService.getCityAndBuildingId(bookingRequest.getCity());

                response.setData(buildingEntityList);
                System.out.println(buildingEntityList.toString());
            }
//            if (true) {
//                response.setCode(HttpStatus.OK.value());
//                response.setData(new AvailabilityResponse());
//                response.setMessage("success");
//
//            }else{
//                response.setMessage("Couldnt find any such req");
//            }
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

            if(StringUtils.isEmpty(bookingRequest.getCity())){
                response.setCode(HttpStatus.OK.value());
                response.setMessage("City is mandatory!!!");
                return response;
            }

            if(bookingRequest.getDateModel()!=null && !StringUtils.isEmpty(bookingRequest.getDateModel().getStart())
                    && !StringUtils.isEmpty(bookingRequest.getDateModel().getEnd())){
                // date found now check parsing

                try {
                    if(DateFormatUtil.dateMatches(bookingRequest.getDateModel().getStart()) && DateFormatUtil.dateMatches(bookingRequest.getDateModel().getEnd())) {
                        LocalDate dateFrom = LocalDate.parse(bookingRequest.getDateModel().getStart());
                        LocalDate dateTo = LocalDate.parse(bookingRequest.getDateModel().getEnd());
                        if(dateFrom.compareTo(dateTo)>0) {
                            // date from cannot be bigger than To
                            response.setCode(HttpStatus.OK.value());
                            response.setMessage("Start Date cannot be bigger than End Date");
                            return response;
                        }
                    }else {
                        response.setCode(HttpStatus.OK.value());
                        response.setMessage("Date format should be (yyyy-mm-dd)");
                        return response;
                    }
                }catch(Exception e) {
                    // if amount cannot be parsed in a date
                    response.setCode(HttpStatus.OK.value());
                    response.setMessage("Date format should be (yyyy-mm-dd)");
                    return response;
                }

            }else if(bookingRequest.getFlexibleModel()!=null
                    && !StringUtils.isEmpty(bookingRequest.getFlexibleModel().getType())
                    && !StringUtils.isEmpty(bookingRequest.getFlexibleModel().getMonth())){

                // flexible found
                if(!EnumUtils.isValidEnum(FlexiType.class, bookingRequest.getFlexibleModel().getType())){
                    response.setCode(HttpStatus.OK.value());
                    response.setMessage("This Type is invalid");
                    return response;
                }else if(!EnumUtils.isValidEnum(FlexiMonth.class, bookingRequest.getFlexibleModel().getMonth())){
                    response.setCode(HttpStatus.OK.value());
                    response.setMessage("Invalid value for month");
                    return response;
                }


            }else{
                response.setCode(HttpStatus.OK.value());
                response.setMessage("Atleast select Date OR Flexible!!!");
                return response;
            }

            response.setCode(HttpStatus.OK.value());
            response.setMessage("success");
            return response;
        } catch (Exception e) {
            LOGGER.info("Exception occurred while saveWalletCategory the object:{}", e.getMessage());
            response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return response;
        }
    }

}

enum FlexiType
{
    weekend,week,month

}

enum FlexiMonth
{
    jan,feb,mar,apr,may,jun,jul,aug,sep,oct,nov,dec

}
