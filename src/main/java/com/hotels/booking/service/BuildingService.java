package com.hotels.booking.service;

import com.hotels.booking.entity.AvailabilityEntity;
import com.hotels.booking.entity.BuildingEntity;
import com.hotels.booking.entity.PropertyEntity;
import com.hotels.booking.entity.ReservationEntity;
import com.hotels.booking.repository.AvailabilityRepository;
import com.hotels.booking.repository.BuildingRepository;
import com.hotels.booking.repository.PropertyRepository;
import com.hotels.booking.repository.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuildingService {

    /* Created by suditi on 2021-07-26 */
    private static final Logger LOGGER = LoggerFactory.getLogger(BuildingService.class);


    public final BuildingRepository buildingRepository;
    public final PropertyRepository propertyRepository;
    public final AvailabilityRepository availabilityRepository;
    public final ReservationRepository reservationRepository;

    @Autowired
    public BuildingService(BuildingRepository buildingRepository, PropertyRepository propertyRepository,
                           AvailabilityRepository availabilityRepository, ReservationRepository reservationRepository) {
        this.buildingRepository=buildingRepository;
        this.propertyRepository=propertyRepository;
        this.availabilityRepository=availabilityRepository;
        this.reservationRepository=reservationRepository;
    }

    public List<BuildingEntity> getCityAndBuildingId(String city){
        return buildingRepository.findByCity(city);
    }

    public List<PropertyEntity> getPropertyDetails(String apartmentType, long buildingId){
        return propertyRepository.findByBuildingAndApartment(apartmentType,buildingId);
    }

    public List<PropertyEntity> getPropertyDetails(long buildingId){
        return propertyRepository.findByBuildingId(buildingId);
    }

    public List<AvailabilityEntity> getAvailability(long propertyId){
        return availabilityRepository.findByPropertyId(propertyId);
    }
    public List<AvailabilityEntity> getAvailability(long propertyId,List<Integer> monthList){
        return availabilityRepository.findByMonths(propertyId,monthList,monthList);
    }

    public List<ReservationEntity> getReservations(long propertyId){
        return reservationRepository.findByPropertyId(propertyId);
    }
    public List<ReservationEntity> getReservations(long propertyId,List<Integer> monthList){
        return reservationRepository.findByMonths(propertyId,monthList,monthList);
    }
}
