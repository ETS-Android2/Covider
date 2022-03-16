package com.cs310.covider.model;

import android.location.Location;

import java.util.ArrayList;
import java.util.Date;

public class Building {
    int id;
    private Location location;
    private ArrayList<Course> courses;

    public Building(Location location, ArrayList<Course> courses, int id)
    {
        this.courses = courses;
        this.location = location;
        this.id = id;
    }

    public int getInfectedNumber(Util.TimeSpan span, Date date){
        //TODO
        return 0;
    }

    public int getRiskFactor(Util.TimeSpan span, Date date)
    {
        //TODO
        return 0;
    }

    public int getId() {
        return this.id;
    }

    public Location getLocation() {
        return this.location;
    }

    public ArrayList<Course> getCourses() {
        return this.courses;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
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
        if (!other.canEqual((Object) this)) {
            return false;
        }
        if (this.getId() != other.getId()) {
            return false;
        }
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Building;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getId();
        return result;
    }

    public String toString() {
        return "Building(id=" + this.getId() + ", location=" + this.getLocation() + ", courses=" + this.getCourses() + ")";
    }
}
