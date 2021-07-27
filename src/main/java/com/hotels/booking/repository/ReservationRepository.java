package com.hotels.booking.repository;

import com.hotels.booking.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository  extends JpaRepository<ReservationEntity,Long> {

    /* Created by suditi on 2021-07-27 */
    @Query(value = "SELECT * FROM reservation where property_id=:property_id",nativeQuery = true)
    List<ReservationEntity> findByPropertyId(@Param("property_id") long property_id);


    @Query(nativeQuery =true,value = "select * from reservation where property_id=:property_id and (month(check_in) in (:startMonths) || month(check_out) in (:endMonths))")
    List<ReservationEntity> findByMonths(@Param("property_id") long property_id,@Param("startMonths") List<Integer> startMonths,@Param("endMonths") List<Integer> endMonths);

}
