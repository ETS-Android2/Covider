package com.cs310.covider.model;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Building implements Serializable {
    private String name;
    private MyLatLng location;
    private ArrayList<String> coursesIDs;
    private Date checkInDataValidDate;
    private ArrayList<String> checkedInUserEmails;
    private String entryRequirement;
    private String howToSatisfyRequirement;

    public Building(String name, MyLatLng location, ArrayList<String> coursesIDs, Date checkInDataValidDate, ArrayList<String> checkedInUserEmails, String entryRequirement, String howToSatisfyRequirement) {
        this.name = name;
        this.coursesIDs = coursesIDs;
        this.location = location;
        this.checkedInUserEmails = checkedInUserEmails;
        this.checkInDataValidDate = checkInDataValidDate;
        this.howToSatisfyRequirement = howToSatisfyRequirement;
        this.entryRequirement = entryRequirement;
    }

    public Building() {

    }

    public int getInfectedNumber(Util.TimeSpan span, Date date) {
        //TODO
        return 0;
    }

    public int getRiskFactor(Util.TimeSpan span, Date date) {
        //TODO
        return 0;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MyLatLng getLocation() {
        return this.location;
    }

    public void setLocation(MyLatLng location) {
        this.location = location;
    }

    public ArrayList<String> getCoursesIDs() {
        return this.coursesIDs;
    }

    public void setCoursesIDs(ArrayList<String> coursesIDs) {
        this.coursesIDs = coursesIDs;
    }

    @NotNull
    @Override
    public String toString() {
        return "Building(name=" + this.getName() + ", location=" + this.getLocation() + ", courses=" + this.getCoursesIDs() + ")";
    }

    public Date getCheckInDataValidDate() {
        return checkInDataValidDate;
    }

    public void setCheckInDataValidDate(Date checkInDataValidDate) {
        this.checkInDataValidDate = checkInDataValidDate;
    }

    public ArrayList<String> getCheckedInUserEmails() {
        return checkedInUserEmails;
    }

    public void setCheckedInUserEmails(ArrayList<String> checkedInUserEmails) {
        this.checkedInUserEmails = checkedInUserEmails;
    }

    public String getEntryRequirement() {
        return entryRequirement;
    }

    public void setEntryRequirement(String entryRequirement) {
        this.entryRequirement = entryRequirement;
    }

    public String getHowToSatisfyRequirement() {
        return howToSatisfyRequirement;
    }

    public void setHowToSatisfyRequirement(String howToSatisfyRequirement) {
        this.howToSatisfyRequirement = howToSatisfyRequirement;
    }
}
