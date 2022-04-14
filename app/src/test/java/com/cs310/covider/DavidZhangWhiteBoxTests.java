package com.cs310.covider;

import com.cs310.covider.model.Building;
import com.cs310.covider.model.User;
import com.cs310.covider.model.Util;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static com.cs310.covider.model.Util.sameWeek;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class DavidZhangWhiteBoxTests {
    User instructor = new User(User.UserType.INSTRUCTOR, new ArrayList<>(), "apple@qq.com", new Date(), new Date(), new Date(), new HashMap<>());
    User student = new User(User.UserType.STUDENT, new ArrayList<>(), "apple@qq.com", new Date(), new Date(), new Date(), new HashMap<>());

    @Test
    public void WithInTwoWeeks() {
        Date today = new Date();
        assertTrue(Util.withInTwoWeeks(today));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DATE, -7);
        assertTrue(Util.withInTwoWeeks(calendar.getTime()));
        calendar.add(Calendar.DATE, -7);
        assertTrue(Util.withInTwoWeeks(calendar.getTime()));
        calendar.add(Calendar.DATE, -7);
        assertFalse(Util.withInTwoWeeks(calendar.getTime()));
    }

    @Test
    public void UserDidTodayCheck() {
        assertTrue(Util.userDidTodayCheck(student));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -4);
        student.setLastCheckDate(calendar.getTime());
        assertFalse(Util.userDidTodayCheck(student));
    }

    @Test
    public void BuildingCheckinDataValidForToday() {
        Building myHouses = new Building();
        myHouses.setCheckInDataValidDate(new Date());
        assertTrue(Util.buildingCheckinDataValidForToday(myHouses));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -4);
        myHouses.setCheckInDataValidDate(calendar.getTime());
        assertFalse(Util.buildingCheckinDataValidForToday(myHouses));
    }

    @Test
    public void UserCheckedIn() {
        Building myHouse = new Building();
        myHouse.setCheckedInUserEmails(new ArrayList<>());
        myHouse.setCheckInDataValidDate(new Date());
        myHouse.getCheckedInUserEmails().add("apple@qq.com");
        assertTrue(Util.userCheckedIn(myHouse, "apple@qq.com"));
        assertFalse(Util.userCheckedIn(myHouse, ""));
        assertFalse(Util.userCheckedIn(myHouse, "aaa"));
    }

    @Test
    public void SameWeek() {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DATE, -calendar.get(Calendar.DAY_OF_WEEK) + 1);
        assertTrue(sameWeek(today, calendar.getTime()));
        calendar.add(Calendar.DATE, -1);
        assertFalse(sameWeek(today, calendar.getTime()));
    }
}