package com.hotels.booking.util;

import com.hotels.booking.entity.AvailabilityEntity;
import com.hotels.booking.entity.ReservationEntity;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
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

    public static boolean weekendOverlapAvail(List<AvailabilityEntity> availabilityEntities,List<Integer> monInt,String city){

        List<LocalDate> weekendList = weekendOfMonth(LocalDate.now().getYear(), monInt.get(0),monInt.get(monInt.size()-1), city);
        Iterator<LocalDate> iter = weekendList.iterator();
        for(AvailabilityEntity avail : availabilityEntities) {
            String reservedSD=avail.getStart_date();
            String reservedED=avail.getEnd_date();
            while (iter.hasNext()) {
                LocalDate temp = iter.next();
                if ((temp.isBefore(LocalDate.parse(reservedED))) && (temp.isAfter(LocalDate.parse(reservedSD)))) {
                    iter.remove();
                }
            }
        }

        if(weekendList.size()>=2){
            int i = 0;
            while (i<weekendList.size()-1){
                long elapsedDays = ChronoUnit.DAYS.between(weekendList.get(i), weekendList.get(i+1));
                if(elapsedDays==1){
                    return false;
                }
                i++;
            }
        }
        return true;
    }

    public static boolean weekendOverlapReserve(List<ReservationEntity> reservationEntities,List<Integer> monInt,String city){

        List<LocalDate> weekendList = weekendOfMonth(LocalDate.now().getYear(), monInt.get(0),monInt.get(monInt.size()-1), city);
        Iterator<LocalDate> iter = weekendList.iterator();
        for(ReservationEntity reservationEntity : reservationEntities) {
            String reservedSD=reservationEntity.getCheck_in();
            String reservedED=reservationEntity.getCheck_out();
            while (iter.hasNext()) {
                LocalDate temp = iter.next();
                if ((temp.isBefore(LocalDate.parse(reservedED))) && (temp.isAfter(LocalDate.parse(reservedSD)))) {
                     iter.remove();
                }
            }
        }

        if(weekendList.size()>=2){
            int i = 0;
            while (i<weekendList.size()-1){
                long elapsedDays = ChronoUnit.DAYS.between(weekendList.get(i), weekendList.get(i+1));
                if(elapsedDays==1){
                    return false;
                }
                i++;
            }
        }
        return true;
    }

    public static boolean datePossibleThismonth(List<Integer> monInt,int days){
        if(monInt.size()==1 && LocalDate.now().getMonthValue()==monInt.get(0)){
            LocalDate end = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
            if(end.getDayOfMonth()-LocalDate.now().getDayOfMonth()>=days)
                return false;
            else{
                return true;
            }
        }

        return false;
    }

    public static boolean weekendPossibleThismonth(List<Integer> monInt,String city){
        if(monInt.size()==1 && LocalDate.now().getMonthValue()==monInt.get(0)){
            List<LocalDate> weekendList = weekendOfMonth(LocalDate.now().getYear(), monInt.get(0),monInt.get(0), city);

            if(weekendList.size()>=2){
                int i = 0;
                while (i<weekendList.size()-1){
                    long elapsedDays = ChronoUnit.DAYS.between(weekendList.get(i), weekendList.get(i+1));
                    if(elapsedDays==1){
                        return false;
                    }
                    i++;
                }
            }
            return true;
        }

        return false;
    }


    public static boolean monthOverlapAvail(List<AvailabilityEntity> availabilityEntities, List<Integer> monInt,int days){
        LocalDate temp =null;
        for(AvailabilityEntity avail : availabilityEntities){
            if(temp==null){
                temp=LocalDate.parse(avail.getEnd_date());
                continue;
            }
            if(monInt.contains(temp.getMonth())){
                if(ChronoUnit.DAYS.between(LocalDate.parse(avail.getEnd_date()), temp)>=days){
                    if(ChronoUnit.DAYS.between(LocalDate.parse(avail.getStart_date()), temp)>=days) {
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
        if(monInt.get(monInt.size()-1)==temp.getMonth().getValue() && (days==7 && temp.getDayOfMonth()<30-7)){
            return false;
        }
        return true;
    }
    public static boolean monthOverlapReserve(List<ReservationEntity> reservationEntities, List<Integer> monInt,int days){
        LocalDate temp =null;
        for(ReservationEntity avail : reservationEntities){
            if(temp==null){
                temp=LocalDate.parse(avail.getCheck_in());
                continue;
            }
            if(monInt.contains(temp.getMonth())){
                if(ChronoUnit.DAYS.between(LocalDate.parse(avail.getCheck_out()), temp)>=days){
                    if(ChronoUnit.DAYS.between(LocalDate.parse(avail.getCheck_in()), temp)>=days) {
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
        if(monInt.get(monInt.size()-1)==temp.getMonth().getValue() && (days==7 && temp.getDayOfMonth()<30-7)){
            return false;
        }
        return false;
    }
    public static List<LocalDate> weekendOfMonth(int year, int startMonth,int endMonth, String city){
        List<LocalDate> weekends = new ArrayList<>();
        LocalDate startDate;
        LocalDate endDate=LocalDate.of(year, endMonth, 1).withDayOfMonth(LocalDate.of(year, endMonth, 1).lengthOfMonth());
        if(startMonth==LocalDate.now().getMonthValue()) {
            // start from today
            startDate = LocalDate.now();
        }else{
            // later month so start from day 1
            startDate = LocalDate.of(year, startMonth, 1);
        }

        while (startDate.isBefore(endDate)) {
            DayOfWeek day = DayOfWeek.of(startDate.get(ChronoField.DAY_OF_WEEK));
            switch (day) {
                case FRIDAY:
                    if (city.equalsIgnoreCase("Dubai"))
                        weekends.add(startDate);
                    break;
                case SATURDAY:
                    weekends.add(startDate);
                    break;
                case SUNDAY:
                    if (!city.equalsIgnoreCase("Dubai"))
                        weekends.add(startDate);
                    break;

            }
            startDate = startDate.plusDays(1);
        }

        return weekends;
    }

}
