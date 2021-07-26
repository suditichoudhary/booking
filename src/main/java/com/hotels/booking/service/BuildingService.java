package com.hotels.booking.service;

import com.hotels.booking.entity.BuildingEntity;
import com.hotels.booking.repository.BuildingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BuildingService {

    /* Created by suditi on 2021-07-26 */
    private static final Logger LOGGER = LoggerFactory.getLogger(BuildingService.class);


    public final BuildingRepository buildingRepository;

    @Autowired
    public BuildingService(BuildingRepository buildingRepository) {
        this.buildingRepository=buildingRepository;
    }

    public List<BuildingEntity> getCityAndBuildingId(String city){
        return buildingRepository.findByCity(city);
    }
}
