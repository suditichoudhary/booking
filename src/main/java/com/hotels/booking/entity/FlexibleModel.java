package com.hotels.booking.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FlexibleModel {

    /* Created by suditi on 2021-07-25 */
    public enum type { weekend,week,month }
    public enum month { jan,feb,mar,apr,may,jun,jul,aug,sep,oct,nov,dec }


}
