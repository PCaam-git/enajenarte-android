package com.svalero.enajenarte.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    public static final DateTimeFormatter INPUT_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public static final DateTimeFormatter OUTPUT_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static String formatDateTime(String isoDateTime) {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(isoDateTime, INPUT_FORMAT);
            return dateTime.format(OUTPUT_FORMAT);
        } catch (Exception e) {
            return isoDateTime; // aquí fallback por si algo falla
        }
    }
}