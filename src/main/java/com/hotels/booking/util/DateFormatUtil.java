package com.hotels.booking.util;

import com.hotels.booking.entity.AvailabilityEntity;
import com.hotels.booking.entity.ReservationEntity;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Pattern;

public class DateFormatUtil {

    /* Created by suditi on 2021-07-27 */
    private static Pattern DATE_ACCOUNT = Pattern.compile(
            "^\\d{4}-\\d{2}-\\d{2}$");

    public static boolean dateMatches(String date) {
        return DATE_ACCOUNT.matcher(date).matches();
    }

    public static boolean dateOverlap(String selectedSD,String selectedED, String reservedSD, String reservedED){
        if((LocalDate.parse(selectedSD).isBefore(LocalDate.parse(reservedED))) && (LocalDate.parse(selectedED).isAfter(LocalDate.parse(reservedSD))))
                return true;

        return false;
    }

    public static boolean weekendOverlap(String selectedSD,String selectedED, String reservedSD, String reservedED){
        if((LocalDate.parse(selectedSD).isBefore(LocalDate.parse(reservedED))) && (LocalDate.parse(selectedED).isAfter(LocalDate.parse(reservedSD))))
            return true;

        return false;
    }

    public static boolean weekOverlap(String selectedSD,String selectedED, String reservedSD, String reservedED){
        if((LocalDate.parse(selectedSD).isBefore(LocalDate.parse(reservedED))) && (LocalDate.parse(selectedED).isAfter(LocalDate.parse(reservedSD))))
            return true;

        return false;
    }


    public static boolean monthOverlapAvail(List<AvailabilityEntity> availabilityEntities, List<Integer> monInt){
        LocalDate temp =null;
        for(AvailabilityEntity avail : availabilityEntities){
            if(temp==null){
                temp=LocalDate.parse(avail.getEnd_date());
                continue;
            }
            if(monInt.contains(temp.getMonth())){
                if(ChronoUnit.DAYS.between(LocalDate.parse(avail.getEnd_date()), temp)>=30){
                    if(ChronoUnit.DAYS.between(LocalDate.parse(avail.getStart_date()), temp)>=30) {
                        return false;
                    }
                }
                temp=LocalDate.parse(avail.getEnd_date());
            }else{
                return true;
            }

        }
        if(monInt.get(monInt.size()-1)>temp.getMonth().getValue()){
            return false;
        }
        return true;
    }
    public static boolean monthOverlapReserve(List<ReservationEntity> reservationEntities, List<Integer> monInt){
        LocalDate temp =null;
        for(ReservationEntity avail : reservationEntities){
            if(temp==null){
                temp=LocalDate.parse(avail.getCheck_in());
                continue;
            }
            if(monInt.contains(temp.getMonth())){
                if(ChronoUnit.DAYS.between(LocalDate.parse(avail.getCheck_out()), temp)>=30){
                    if(ChronoUnit.DAYS.between(LocalDate.parse(avail.getCheck_in()), temp)>=30) {
                        return false;
                    }
                }
                temp=LocalDate.parse(avail.getCheck_out());
            }else{
                return true;
            }

        }
        if(monInt.get(monInt.size()-1)>temp.getMonth().getValue()){
            return false;
        }
        return false;
    }

}
