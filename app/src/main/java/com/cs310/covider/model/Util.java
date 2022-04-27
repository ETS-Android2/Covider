package com.cs310.covider.model;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.Calendar;
import java.util.Date;

public class Util {
    public static boolean userDidTodayCheck(User user) {
        assert user != null;
        return user.getLastCheckDate() != null && Util.sameDay(new Date(), user.getLastCheckDate());
    }

    public static boolean buildingCheckinDataValidForToday(Building building) {
        assert building != null;
        return building.getCheckInDataValidDate() != null && Util.sameDay(new Date(), building.getCheckInDataValidDate());
    }

    public static Task<DocumentSnapshot> getCurrentUserTask() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return null;
        }
        return getUserWithEmailTask(user.getEmail());
    }

    public static Task<DocumentSnapshot> getUserWithEmailTask(String email) {
        return FirebaseFirestore.getInstance().collection("Users").document(email).get();
    }

    public static boolean withInTwoWeeks(Date date) {
        assert date != null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 14);
        Calendar today = Calendar.getInstance();
        today.setTime(new Date());
        return !calendar.before(today);
    }

    public static boolean userCheckedIn(Building building, String email) {
        assert building != null && email != null;
        if (building.getCheckInDataValidDate() != null && sameDay(new Date(), building.getCheckInDataValidDate())) {
            return building.getCheckedInUserEmails() != null && building.getCheckedInUserEmails().contains(email);
        }
        return false;
    }

    public static boolean inTimeFrame(TimeSpan span, Date a, Date b) {
        switch (span) {
            case DAY: {
                return sameDay(a, b);
            }
            case WEEK: {
                return sameWeek(a, b);
            }
            case MONTH: {
                return sameMonth(a, b);
            }
            default: {
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

    public static String emailError(String email) {
        return email != null && EmailValidator.getInstance().isValid(email) ? null : "Invalid Email!";
    }

    public static String passwordError(String pass) {
        return pass != null && pass.length() >= 8 ? null : "Needs to be at least 8 characters";
    }

    public enum TimeSpan {
        DAY,
        WEEK,
        MONTH
    }

}
