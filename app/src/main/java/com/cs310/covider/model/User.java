package com.cs310.covider.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class User implements Serializable {
    private UserType userType = UserType.STUDENT;
    private ArrayList<String> userCoursesIDs;
    private String email = "";
    private Date lastSymptomsDate;
    private Date lastCheckDate;
    private Date lastInfectionDate;


    public User() {
    }

    public User(UserType userType, ArrayList<String> userCoursesIDs, String email, Date lastSymptomsDate, Date lastCheckDate, Date lastInfectionDate) {
        this.userType = userType;
        this.userCoursesIDs = userCoursesIDs;
        this.email = email;
        this.lastSymptomsDate = lastSymptomsDate;
        this.lastCheckDate = lastCheckDate;
        this.lastInfectionDate = lastInfectionDate;
    }

    public boolean risky(Util.TimeSpan span, Date queryDate) {
        //TODO
        return true;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserType getUserType() {
        return this.userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public ArrayList<String> getUserCoursesIDs() {
        return this.userCoursesIDs;
    }

    public void setUserCoursesIDs(ArrayList<String> userCoursesIDs) {
        this.userCoursesIDs = userCoursesIDs;
    }

    public Date getLastSymptomsDate() {
        return this.lastSymptomsDate;
    }

    public void setLastSymptomsDate(Date lastSymptomsDate) {
        this.lastSymptomsDate = lastSymptomsDate;
    }

    public Date getLastCheckDate() {
        return lastCheckDate;
    }

    public void setLastCheckDate(Date lastCheckDate) {
        this.lastCheckDate = lastCheckDate;
    }

    public Date getLastInfectionDate() {
        return lastInfectionDate;
    }

    public void setLastInfectionDate(Date lastInfectionDate) {
        this.lastInfectionDate = lastInfectionDate;
    }

    public enum UserType implements Serializable {
        INSTRUCTOR,
        STUDENT
    }
}
