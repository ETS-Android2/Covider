package com.cs310.covider.model;


import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class User {

    public User(UserType userType, ArrayList<Course> userCourses, String id, Date lastInfectionDate, String email) {
        this.userType = userType;
        this.userCourses = userCourses;
        this.email = email;
        this.id = id;
        this.lastInfectionDate = lastInfectionDate;
    }

    public enum UserType {
        INSTRUCTOR,
        STUDENT
    }

    private UserType userType = UserType.STUDENT;
    private ArrayList<Course> userCourses;
    private String id = "";
    private String email = "";
    private Date lastInfectionDate;

    public boolean risky(Util.TimeSpan span, Date queryDate){
        //TODO
        return true;
    }

    public String getEmail() {return email;}

    public void setEmail(String email) {
        this.email = email;
    }

    public UserType getUserType() {
        return this.userType;
    }

    public ArrayList<Course> getUserCourses() {
        return this.userCourses;
    }

    public String getId() {
        return this.id;
    }

    public Date getLastInfectionDate() {
        return this.lastInfectionDate;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public void setUserCourses(ArrayList<Course> userCourses) {
        this.userCourses = userCourses;
    }

    public void setId(String id) {
        this.id = id;
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
        if (!other.canEqual((Object) this)) {
            return false;
        }
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        return Objects.equals(this$id, other$id);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof User;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "User(userType=" + this.getUserType() + ", userCourses=" + this.getUserCourses() + ", id=" + this.getId() + ", lastInfectionDate=" + this.getLastInfectionDate() + ")";
    }
}
