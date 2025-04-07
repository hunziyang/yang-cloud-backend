package com.yang.portal.core.utils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtil {

    public static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String formatDate(ZonedDateTime zonedDateTime,DateTimeFormatter formatter) {
        return zonedDateTime.format(formatter);
    }
}
