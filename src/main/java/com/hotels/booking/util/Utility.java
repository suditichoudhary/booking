package com.hotels.booking.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Utility {

    /* Created by suditi on 2021-07-26 */
    private static final Logger LOGGER = LoggerFactory.getLogger(Utility.class);

    private static final ObjectMapper mapper = new ObjectMapper();


    public static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException ex) {
            LOGGER.info("Exception occurred object convert into json {}",ex.getMessage());
            return "";
        }
    }
    public static List<String> stringToList(String str) {
        List <String> list = new ArrayList<>();
        try {
            if(str!=null) {
                String[] arr = str.split(",");
                list = Arrays.asList(arr);
            }
        } catch (Exception ex) {
            LOGGER.info("Exception occurred stringToList",ex.getMessage());
        }
        return list;
    }
    public static void weekendForMonth(int year, String mon){
        // create a Calendar for the 1st of the required month

        int month = Calendar.JANUARY;
        Calendar cal = new GregorianCalendar(year, month, 1);
        do {
            // get the day of the week for the current day
            int day = cal.get(Calendar.DAY_OF_WEEK);
            // check if it is a Saturday or Sunday
            if (day == Calendar.SATURDAY || day == Calendar.SUNDAY) {
                // print the day - but you could add them to a list or whatever
                System.out.println(cal.get(Calendar.DAY_OF_MONTH));
            }
            // advance to the next day
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }  while (cal.get(Calendar.MONTH) == month);
// stop when we reach the start of the next month
    }

}
