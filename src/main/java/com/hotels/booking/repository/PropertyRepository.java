package com.hotels.booking.repository;

import com.hotels.booking.entity.PropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<PropertyEntity,Long> {

    /* Created by suditi on 2021-07-27 */

    @Query(value = "SELECT * FROM property where property_type=:property_type and building_id=:building_id",nativeQuery = true)
    List<PropertyEntity> findByBuildingAndApartment(@Param("property_type") String property_type,@Param("building_id") long building_id);

    @Query(value = "SELECT * FROM property where building_id=:building_id",nativeQuery = true)
    List<PropertyEntity> findByBuildingId(@Param("building_id") long building_id);


}
