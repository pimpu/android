package com.alchemistdigital.kissan.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by user on 1/11/2016.
 */
public class DateHelper {
    private final static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
    private final static SimpleDateFormat noFormatDateSdf = new SimpleDateFormat("ddMMyyhhmmss");
    private final static SimpleDateFormat MONTH = new SimpleDateFormat("MM");
    private final static SimpleDateFormat DAY = new SimpleDateFormat("dd");
    private final static SimpleDateFormat YEAR = new SimpleDateFormat("yyyy");
    private final static SimpleDateFormat HOURS = new SimpleDateFormat("hh");
    private final static SimpleDateFormat MINUTE= new SimpleDateFormat("mm");
    private final static SimpleDateFormat SECOND = new SimpleDateFormat("ss");


    /**
     *
     * @param dateFormili in format "yyyy/mm/dd"
     * @return long value in millis
     * @throws ParseException
     */
    public static long convertToMillis (String dateFormili) throws ParseException {
        Date date = sdf.parse(dateFormili);
        return date.getTime();
    }

    /**
     *
     * @param millis
     * @return date in string formatted as
     */
    public static String convertToString(long millis)
    {
        Date currentDate = new Date(millis);
        return sdf.format(currentDate);
    }

    public static String getRefStringDate(){
        Date date = new Date();
        return noFormatDateSdf.format(date);
    }

    public static String getMonth(long millis) {
        Date currentDate = new Date(millis);
        return MONTH.format(currentDate);
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
