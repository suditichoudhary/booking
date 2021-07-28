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

}
