package com.hotels.booking.repository;

import com.hotels.booking.entity.BuildingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuildingRepository extends JpaRepository<BuildingEntity,Long> {

    /* Created by suditi on 2021-07-26 */

    @Query(value = "SELECT * FROM building where city=:city",nativeQuery = true)
    List<BuildingEntity> findByCity(@Param("city") String city);

}
