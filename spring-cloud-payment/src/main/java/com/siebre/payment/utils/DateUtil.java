package com.siebre.payment.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by tianci.huang on 2017/6/26.
 */
public class DateUtil {

    public static Date getDayStart(Date currentDate) {
        Date result = null;

        Calendar current = Calendar.getInstance();
        current.setTime(currentDate);
        current.set(Calendar.HOUR_OF_DAY, 0);
        current.clear(Calendar.MILLISECOND);
        current.clear(Calendar.MINUTE);
        current.clear(Calendar.SECOND);
        result = current.getTime();

        return result;
    }

    public static Date getDayEnd(Date day) {
        Date result = null;

        Calendar date = Calendar.getInstance();
        date.setTime(day);
        date.set(Calendar.HOUR_OF_DAY, 24);
        date.clear(Calendar.MINUTE);
        date.clear(Calendar.SECOND);
        date.set(Calendar.MILLISECOND, -1);
        result = date.getTime();

        return result;
    }

    public static Date getTodayStart() {
        return getDayStart(new Date());
    }

    public static Date getTodayEnd() {
        return getDayEnd(new Date());
    }

    public static Date getMonthStart(Date currentDate) {
        Date result = null;

        Calendar current = Calendar.getInstance();
        current.setTime(getDayStart(currentDate));
        current.set(Calendar.DAY_OF_MONTH, 1);
        result = current.getTime();

        return result;
    }

    public static Date getMonthEnd(Date currentDate) {
        Date result = null;

        Calendar current = Calendar.getInstance();
        current.setTime(getMonthStart(currentDate));
        current.add(Calendar.MONTH, 1);
        current.add(Calendar.MILLISECOND, -1);
        result = current.getTime();

        return result;
    }

    public static Date getYesterday() {
        Date today = new Date();
        return addDay(today, -1);
    }

    public static Date getTomorrow() {
        Date today = new Date();
        return addDay(today, 1);
    }

    public static int getYearOfDate(Date date) {
        Calendar gc = prepare(date);
        return gc.get(Calendar.YEAR);
    }

    public static int getMonthOfDate(Date date) {
        Calendar gc = prepare(date);
        return gc.get(Calendar.MONTH);
    }

    public static int getDayOfDate(Date date) {
        Calendar gc = prepare(date);
        return gc.get(Calendar.DAY_OF_MONTH);
    }

    public static Date addYear(Date date, int year) {
        Calendar gc = prepare(date);
        gc.add(Calendar.YEAR, year);
        return gc.getTime();
    }

    public static Date addMonth(Date date, int month) {
        Calendar gc = prepare(date);
        gc.add(Calendar.MONTH, month);
        return gc.getTime();
    }

    public static Date addDay(Date date, int day) {
        Calendar gc = prepare(date);
        gc.add(Calendar.DAY_OF_MONTH, day);
        return gc.getTime();
    }

    private static Calendar prepare(Date date) {
        Calendar gc = Calendar.getInstance();
        gc.setTime(date);
        return gc;
    }
}
