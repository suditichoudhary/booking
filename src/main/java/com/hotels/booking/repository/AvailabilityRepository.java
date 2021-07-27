package com.hotels.booking.repository;

import com.hotels.booking.entity.AvailabilityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvailabilityRepository extends JpaRepository<AvailabilityEntity,Long> {

    /* Created by suditi on 2021-07-27 */
    @Query(value = "SELECT * FROM availability where property_id=:property_id and is_blocked=1",nativeQuery = true)
    List<AvailabilityEntity> findByPropertyId(@Param("property_id") long property_id);


    @Query(nativeQuery =true,value = "select * from availability where property_id=:property_id and is_blocked=1 and (month(start_date) in (:startMonths) || month(end_date) in (:endMonths)) order by end_date")
    List<AvailabilityEntity> findByMonths(@Param("property_id") long property_id,@Param("startMonths") List<Integer> startMonths,@Param("endMonths") List<Integer> endMonths);
}
