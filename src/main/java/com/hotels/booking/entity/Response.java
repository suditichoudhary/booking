package com.hotels.booking.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response<T> {

    /* Created by suditi on 2021-07-25 */
    private Integer code;
    private String message;
    T data;

}
