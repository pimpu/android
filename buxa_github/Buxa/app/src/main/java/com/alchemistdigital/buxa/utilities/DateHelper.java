package com.alchemistdigital.buxa.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by user on 9/13/2016.
 */
public class DateHelper {
    private final static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
    private final static SimpleDateFormat MONTH = new SimpleDateFormat("MM");
    private final static SimpleDateFormat STRING_MONTH = new SimpleDateFormat("MMM");
    private final static SimpleDateFormat DAY = new SimpleDateFormat("dd");
    private final static SimpleDateFormat YEAR = new SimpleDateFormat("yyyy");
    private final static SimpleDateFormat HOURS = new SimpleDateFormat("hh");
    private final static SimpleDateFormat MINUTE= new SimpleDateFormat("mm");
    private final static SimpleDateFormat SECOND = new SimpleDateFormat("ss");
    private final static SimpleDateFormat sdfQuotationDate = new SimpleDateFormat("dd/MM/yyyy");
    private final static SimpleDateFormat sdfBookId = new SimpleDateFormat("ddMMyyhhmmss");
    private final static SimpleDateFormat ENQUIRY_TODAY_TIME = new SimpleDateFormat("hh:mm a");


    public static long convertToMillis () {
        Date dates = new Date();
        return dates.getTime();
    }

    /**
     *
     * @param millis
     * @return date in string formatted as
     */
    public static String convertToString(long millis) {
        Date currentDate = new Date(millis);
        return sdf.format(currentDate);
    }

    public static String convertToTodayEnquiryString(long millis) {
        Date currentDate = new Date(millis);
        return ENQUIRY_TODAY_TIME.format(currentDate);
    }

    public static String convertToFullDateString(long millis) {
        Date dates = new Date(millis);
        return sdfQuotationDate.format(dates);
    }

    public static String getBookId(){
        Date dates = new Date();
        return sdfBookId.format(dates);
    }

    public static String getMonth(long millis) {
        Date currentDate = new Date(millis);
        return MONTH.format(currentDate);
    }

    public static String getMonthString(long millis) {
        Date currentDate = new Date(millis);
        return STRING_MONTH.format(currentDate);
    }

    public static String getDay(long millis) {
        Date currentDate = new Date(millis);
        return DAY.format(currentDate);
    }

    public static String getYear(long millis) {
        Date currentDate = new Date(millis);
        return YEAR.format(currentDate);
    }

    public static String getHours(long millis) {
        Date currentDate = new Date(millis);
        return HOURS.format(currentDate);
    }

    public static String getMinute(long millis) {
        Date currentDate = new Date(millis);
        return MINUTE.format(currentDate);
    }

    public static String getSecond(long millis) {
        Date currentDate = new Date(millis);
        return SECOND.format(currentDate);
    }
}
