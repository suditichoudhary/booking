package com.hotels.booking.service;

import com.hotels.booking.entity.*;
import com.hotels.booking.util.DateFormatUtil;
import com.hotels.booking.util.Utility;
import io.micrometer.core.instrument.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.EnumUtils;

import java.time.LocalDate;
import java.util.*;

@Service
public class AvailabilityService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AvailabilityService.class);

    /* Created by suditi on 2021-07-25 */

    public final BuildingService buildingService;

    @Autowired
    public AvailabilityService(BuildingService buildingService) {
        this.buildingService=buildingService;
    }

    public ResponseEntity<?> getHotelAvailability(BookingRequest bookingRequest){
        Response response = new Response();
        try {
            LOGGER.info("getHotelAvailability request :dto:{}", Utility.toJson(bookingRequest));

            response = validateRequest(bookingRequest);
            if(response.getMessage().equalsIgnoreCase("success")){
                // validated successfuly proceed further
                response = buildingMatcher((BookingRequest) response.getData());
            }

            if(response.getMessage().equalsIgnoreCase("success")){
                response.setCode(HttpStatus.OK.value());
            }else{
                response.setData(null);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception occurred while getHotelAvailability the object:{}", e);
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
                        if(dateFrom.compareTo(LocalDate.now())<0) {
                            // date from cannot be bigger than To
                            response.setCode(HttpStatus.OK.value());
                            response.setMessage("Back Date is not possible!!!");
                            return response;
                        }
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
                    && bookingRequest.getFlexibleModel().getMonth()!=null
                    && bookingRequest.getFlexibleModel().getMonth().size()>0){

                // flexible found
                LOGGER.info("Flexible Model found.");
                bookingRequest.setFlagFlexi(true);

                if(!EnumUtils.isValidEnum(FlexiType.class, bookingRequest.getFlexibleModel().getType())){
                    response.setCode(HttpStatus.OK.value());
                    response.setMessage("This Type is invalid");
                    return response;
                }
                for (String mon : bookingRequest.getFlexibleModel().getMonth()) {
                    if (!EnumUtils.isValidEnum(FlexiMonth.class, mon)) {
                        response.setCode(HttpStatus.OK.value());
                        response.setMessage("Invalid value for month : "+mon);
                        return response;
                    }
                }

            }else{
                response.setCode(HttpStatus.OK.value());
                response.setMessage("Atleast select Date OR Flexible!!!");
                return response;
            }

            response.setCode(HttpStatus.OK.value());
            response.setData(bookingRequest);
            response.setMessage("success");
            return response;
        } catch (Exception e) {
            LOGGER.info("Exception occurred while validateRequest the object:{}", e);
            response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return response;
        }
    }

    public Response buildingMatcher(BookingRequest bookingRequest) {
        Response response = new Response();

        List<BuildingEntity> buildingEntityList = buildingService.getCityAndBuildingId(bookingRequest.getCity());
        if (buildingEntityList != null && buildingEntityList.size() > 0) {
            LOGGER.info("yayyy building found in this city ");
            for (BuildingEntity building : buildingEntityList) {
                // find all properties matching search result
                List<PropertyEntity> properties = null;
                if (!StringUtils.isEmpty(bookingRequest.getApartmentType())) {
                    properties = buildingService.getPropertyDetails(bookingRequest.getApartmentType(), building.getId());
                } else {
                    properties = buildingService.getPropertyDetails(building.getId());
                }

                if (properties != null && properties.size() > 0) {
                    // check availability on dates
                    response = propAvailableOnSelectedDate(bookingRequest,properties);
                    if(!response.getMessage().equalsIgnoreCase("success")){
                        // error is already there
                    }else{
                        // suggestion implementation?
                        AvailabilityResponse availabilityResponse = prepareActualResponse((List<PropertyEntity>)response.getData());
                        response.setData(availabilityResponse);
                    }
                } else {
                    response.setCode(HttpStatus.OK.value());
                    if (!StringUtils.isEmpty(bookingRequest.getApartmentType()))
                        response.setMessage("OOPS!! No Property for selected Apartment type ");
                    else
                        response.setMessage("OOPS!! No Property is currently available in this city");

                }
            }
        } else {
            response.setCode(HttpStatus.OK.value());
            response.setMessage("OOPS!! We might COME soon to this city :) ");

        }
        return response;
    }
    public Response propAvailableOnSelectedDate(BookingRequest bookingRequest,List<PropertyEntity> properties){

        //List<PropertyEntity> tempProp = properties;
        Response response = new Response();
        Iterator<PropertyEntity> iter = properties.iterator();
        while(iter.hasNext()) {
            try{
                PropertyEntity prop = iter.next();
                // check for flexible
                if (bookingRequest.isFlagFlexi()) {
                    List<String> monStr = bookingRequest.getFlexibleModel().getMonth();
                    List<Integer> monInt = new ArrayList<>();
                    Collections.sort(monInt);
                    for (String mon : monStr) {
                        if(FlexiMonth.valueOf(mon).getMonId()>=LocalDate.now().getMonthValue())
                        monInt.add(FlexiMonth.valueOf(mon).getMonId());
                    }

                    if(monInt.size()<1){
                        response.setCode(HttpStatus.OK.value());
                        response.setMessage("Please choose month greater than current month of this year");
                        return response;
                    }
                    boolean res = propFlexiCheckAvailability(bookingRequest,prop.getId(),monInt);
                    if(res) {
                        iter.remove();
                        LOGGER.info("Removing property due to availability issue : " + prop.getId());
                        continue;
                    }
                    res = propFlexiCheckReserved(bookingRequest,prop.getId(),monInt);
                    if(res) {
                        iter.remove();
                        LOGGER.info("Removing property due to availability issue : " + prop.getId());
                        continue;
                    }
                    continue;
                }
                // runs in case of date selection
                List<AvailabilityEntity> availabilityEntities = buildingService.getAvailability(prop.getId());
                if (availabilityEntities != null && availabilityEntities.size() > 0) {
                    for (AvailabilityEntity avail : availabilityEntities) {
                        if (DateFormatUtil.dateOverlap(bookingRequest.getDateModel().getStart(), bookingRequest.getDateModel().getEnd(), avail.getStart_date(), avail.getEnd_date())) {
                            // date overlaps can't help it
                            iter.remove();
                            LOGGER.info("Removing property due to availability issue : " + prop.getId());
                            continue;
                        } else {
                            // all gooddddd
                        }
                    }
                }
                // check if amenties matches
                if (bookingRequest.getAmenities() != null && bookingRequest.getAmenities().size() > 0) {
                    List<String> propAmenties = Utility.stringToList(prop.getAmenities());
                    if (!Collections.disjoint(propAmenties, bookingRequest.getAmenities())) {
                        // all ok
                    } else {
                        LOGGER.info("Removing property due to amenity mismatch : " + prop.getId());
                        iter.remove();
                        continue;
                    }
                }

                // check for particular date

                boolean flagReserved = propReservedOnSelectedDate(bookingRequest, prop.getId());
                if (flagReserved) {
                    LOGGER.info("Removing property due to reservation issue : " + prop.getId());
                    iter.remove();
                    continue;
                }

            }catch(Exception e){
                LOGGER.error("Some exception in property lets loop further : ",e);
            }
        }
        if(properties.size()>0){
            response.setData(properties);
            response.setMessage("success");
        }else{
            response.setCode(HttpStatus.OK.value());
            response.setMessage("No Property available for the selected date :(");
            response.setData("noProp");
        }

        return response;
    }

    public boolean propReservedOnSelectedDate(BookingRequest bookingRequest,long propId){

        boolean flagReserved=false;
        List<ReservationEntity> reservationEntities = buildingService.getReservations(propId);
        if(reservationEntities!=null && reservationEntities.size()>0){
            for(ReservationEntity reserve : reservationEntities) {
                if (DateFormatUtil.dateOverlap(bookingRequest.getDateModel().getStart(), bookingRequest.getDateModel().getEnd(), reserve.getCheck_in(),reserve.getCheck_out())) {
                    // date overlaps can't help it
                    flagReserved=true;
                } else {
                    // all gooddddd
                }
            }
        }
        return flagReserved;
    }
    public AvailabilityResponse prepareActualResponse(List<PropertyEntity> propertyEntities) {
        AvailabilityResponse availabilityResponse= new AvailabilityResponse();
        List<Long> matchUnits = new ArrayList<>();
        for(PropertyEntity prop:propertyEntities) {
            matchUnits.add(prop.getId());
        }
        availabilityResponse.setMatch(matchUnits);
        return availabilityResponse;
    }

    public boolean propFlexiCheckAvailability(BookingRequest bookingRequest,long propId,List<Integer> monInt ) {
        boolean flagNotAvail = true;
        List<AvailabilityEntity> availabilityEntities = buildingService.getAvailability(propId, monInt);
        if (availabilityEntities != null && availabilityEntities.size() > 0) {

            switch (FlexiType.valueOf(bookingRequest.getFlexibleModel().getType())){
                case month:
                    flagNotAvail = DateFormatUtil.monthOverlapAvail(availabilityEntities,monInt);
                    break;
                case week:
                    //flagNotAvail = DateFormatUtil.weekOverlap();
                    break;
                case weekend:
                    //flagNotAvail = DateFormatUtil.weekendOverlap();
                    break;
            }
        } else {
            flagNotAvail = false;
        }
        return flagNotAvail;
    }
    public boolean propFlexiCheckReserved(BookingRequest bookingRequest,long propId,List<Integer> monInt){
        boolean flagReserved = true;
        List<ReservationEntity> reservationEntities = buildingService.getReservations(propId,monInt);
        if(reservationEntities!=null && reservationEntities.size()>0){
            for(ReservationEntity reserve : reservationEntities) {
                switch (FlexiType.valueOf(bookingRequest.getFlexibleModel().getType())){
                    case month:
                        flagReserved = DateFormatUtil.monthOverlapReserve(reservationEntities,monInt);
                        break;
                    case week:
                      //  flagReserved = DateFormatUtil.weekOverlap();
                        break;
                    case weekend:
                      //  flagReserved = DateFormatUtil.weekendOverlap();
                        break;

                }
            }
        }else {
            flagReserved= false;
        }
        return flagReserved;
    }
    public enum FlexiType
    {
        weekend(1),week(2),month(3);
        private final Integer type1;

        FlexiType(Integer i) {
            this.type1 = i;
        }
        public Integer getType() {
            return this.type1;
        }

    }
}





enum FlexiMonth
{
    jan(1),
    feb(2),
    mar(3),
    apr(4),
    may(5),
    jun(6),
    jul(7),
    aug(8),
    sep(9),
    oct(10),
    nov(11),
    dec(12);
    private final Integer monId;

    FlexiMonth(Integer i) {
        this.monId = i;
    }
    public Integer getMonId() {
        return this.monId;
    }

}
