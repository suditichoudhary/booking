package com.hotels.booking.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

}
