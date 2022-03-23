package com.cs310.covider.model;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.Calendar;
import java.util.Date;

public class Util {

    public enum TimeSpan {
        DAY,
        WEEK,
        MONTH
    }

    public static boolean inTimeFrame(TimeSpan span, Date a, Date b)
    {
        switch (span)
        {
            case DAY: {
                return sameDay(a, b);
            }
            case WEEK:{
                return sameWeek(a,b);
            }
            case MONTH:{
                return sameMonth(a, b);
            }
            default:{
                return false;
            }
        }
    }

    public static boolean sameWeek(Date a, Date b) {
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(b);
        int week = currentCalendar.get(Calendar.WEEK_OF_YEAR);
        int year = currentCalendar.get(Calendar.YEAR);
        Calendar targetCalendar = Calendar.getInstance();
        targetCalendar.setTime(a);
        int targetWeek = targetCalendar.get(Calendar.WEEK_OF_YEAR);
        int targetYear = targetCalendar.get(Calendar.YEAR);
        return week == targetWeek && year == targetYear;
    }

    public static boolean sameMonth(Date a, Date b) {
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(b);
        int month = currentCalendar.get(Calendar.MONTH);
        int year = currentCalendar.get(Calendar.YEAR);
        Calendar targetCalendar = Calendar.getInstance();
        targetCalendar.setTime(a);
        int targetMonth = targetCalendar.get(Calendar.MONTH);
        int targetYear = targetCalendar.get(Calendar.YEAR);
        return month == targetMonth && year == targetYear;
    }

    public static boolean sameDay(Date a, Date b) {
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(b);
        int day = currentCalendar.get(Calendar.DAY_OF_YEAR);
        int year = currentCalendar.get(Calendar.YEAR);
        Calendar targetCalendar = Calendar.getInstance();
        targetCalendar.setTime(a);
        int targetDay = targetCalendar.get(Calendar.DAY_OF_YEAR);
        int targetYear = targetCalendar.get(Calendar.YEAR);
        return day == targetDay && year == targetYear;
    }

    public static String emailError(String email){
        return email != null && EmailValidator.getInstance().isValid(email) ? null : "Invalid Email!";
    }

    public static String passwordError(String pass){
        return pass != null && pass.length() >= 8 ? null : "Needs to be at least 8 characters";
    }

}
