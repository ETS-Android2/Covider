package com.cs310.covider.model;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Building implements Serializable {
    private String name;
    private MyLatLng location;
    private ArrayList<String> coursesIDs;
    private Date checkinDataValidDate;
    private ArrayList<String> checkedInUserEmails;

    public Building(String name, MyLatLng location, ArrayList<String> coursesIDs, Date checkinDataValidDate, ArrayList<String> checkedInUserEmails) {
        this.name = name;
        this.coursesIDs = coursesIDs;
        this.location = location;
        this.checkedInUserEmails = checkedInUserEmails;
        this.checkinDataValidDate = checkinDataValidDate;
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

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Building)) {
            return false;
        }
        final Building other = (Building) o;
        if (!other.canEqual(this)) {
            return false;
        }
        return Objects.equals(this.getName(), other.getName());
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Building;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getName().hashCode();
        return result;
    }

    @NotNull
    @Override
    public String toString() {
        return "Building(name=" + this.getName() + ", location=" + this.getLocation() + ", courses=" + this.getCoursesIDs() + ")";
    }

    public Date getCheckinDataValidDate() {
        return checkinDataValidDate;
    }

    public void setCheckinDataValidDate(Date checkinDataValidDate) {
        this.checkinDataValidDate = checkinDataValidDate;
    }

    public ArrayList<String> getCheckedInUserEmails() {
        return checkedInUserEmails;
    }

    public void setCheckedInUserEmails(ArrayList<String> checkedInUserEmails) {
        this.checkedInUserEmails = checkedInUserEmails;
    }
}
