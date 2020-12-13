package com.backend.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class DateUtil {


    public static long getTime(LocalDateTime localDateTime) {
        long milliSeconds;
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        milliSeconds = instant.getEpochSecond();

//        ZoneOffset offset = ZoneOffset.ofHours(8);
//        milliSeconds = localDateTime.toEpochSecond(offset);
//        return milliSeconds;

        return milliSeconds;
    }


    public static void main(String[] args) {

    }

}
