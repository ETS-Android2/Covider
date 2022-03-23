package com.cs310.covider.model;

import com.cs310.covider.model.MyLatLng;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Building {
    String name;
    private MyLatLng location;
    private ArrayList<String> coursesIDs;

    public Building(String name, MyLatLng location, ArrayList<String> coursesIDs)
    {
        this.name = name;
        this.coursesIDs = coursesIDs;
        this.location = location;
    }

    public Building(){

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

    public String getName() {return this.name;}

    public void setName(String name) {this.name = name;}

    public MyLatLng getLocation() {
        return this.location;
    }

    public ArrayList<String> getCoursesIDs() {
        return this.coursesIDs;
    }

    public void setLocation(MyLatLng location) {
        this.location = location;
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
        if (!other.canEqual((Object) this)) {
            return false;
        }
        if (!Objects.equals(this.getName(), other.getName())) {
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
        result = result * PRIME + this.getName().hashCode();
        return result;
    }

    @NotNull
    @Override
    public String toString() {
        return "Building(name=" + this.getName() + ", location=" + this.getLocation() + ", courses=" + this.getCoursesIDs() + ")";
    }
}
