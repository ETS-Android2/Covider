package com.cs310.covider.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class User implements Serializable {
    private UserType userType = UserType.STUDENT;
    private ArrayList<String> userCoursesIDs;
    private String email = "";
    private Date lastInfectionDate;
    private Date lastCheckDate;


    public User() {
    }

    public User(UserType userType, ArrayList<String> userCoursesIDs, String email, Date lastInfectionDate, Date lastCheckDate) {
        this.userType = userType;
        this.userCoursesIDs = userCoursesIDs;
        this.email = email;
        this.lastInfectionDate = lastInfectionDate;
        this.lastCheckDate = lastCheckDate;
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

    public Date getLastInfectionDate() {
        return this.lastInfectionDate;
    }

    public void setLastInfectionDate(Date lastInfectionDate) {
        this.lastInfectionDate = lastInfectionDate;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        final User other = (User) o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$id = this.getEmail();
        final Object other$id = other.getEmail();
        return Objects.equals(this$id, other$id);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof User;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getEmail();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "User(userType=" + this.getUserType() + ", userCourses=" + this.getUserCoursesIDs() + ", lastInfectionDate=" + this.getLastInfectionDate() + ")";
    }

    public Date getLastCheckDate() {
        return lastCheckDate;
    }

    public void setLastCheckDate(Date lastCheckDate) {
        this.lastCheckDate = lastCheckDate;
    }

    public enum UserType implements Serializable {
        INSTRUCTOR,
        STUDENT
    }
}
