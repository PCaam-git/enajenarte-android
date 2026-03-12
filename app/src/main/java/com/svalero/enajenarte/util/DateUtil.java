package com.svalero.enajenarte.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    public static final DateTimeFormatter EVENT_INPUT_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public static final DateTimeFormatter EVENT_OUTPUT_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static final DateTimeFormatter EVENT_USER_INPUT_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static String formatDateTime(String isoDateTime) {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(isoDateTime, EVENT_INPUT_FORMAT);
            return dateTime.format(EVENT_OUTPUT_FORMAT);
        } catch (Exception e) {
            return isoDateTime; // aquí fallback por si algo falla
        }
    }

    public static String userToIsoDateTime(String userDateTime) {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(userDateTime, EVENT_USER_INPUT_FORMAT);
            return dateTime.format(EVENT_INPUT_FORMAT);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isFuture(String isoDateTime) {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(isoDateTime, EVENT_INPUT_FORMAT);
            return dateTime.isAfter(LocalDateTime.now());
        } catch (Exception exception) {
            return false;
        }
    }

    public static final DateTimeFormatter WORKSHOP_OUTPUT_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static final DateTimeFormatter WORKSHOP_USER_INPUT_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static final DateTimeFormatter WORKSHOP_API_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String formatDate(LocalDate date) {
        try {
            return date.format(WORKSHOP_OUTPUT_FORMAT);
        } catch (Exception e) {
            return String.valueOf(date); // aquí fallback por si algo falla
        }
    }

    public static String userToIsoDate(String userDate) {
        try {
            LocalDate date = LocalDate.parse(userDate, WORKSHOP_USER_INPUT_FORMAT);
            return date.format(WORKSHOP_API_FORMAT);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isFuture(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.isAfter(LocalDate.now());

    }
}
