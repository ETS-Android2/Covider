package com.cs310.covider.model;

import java.util.ArrayList;

public class Course {

    CourseMode courseMode;
    private String id;
    private String buildingName;
    private ArrayList<String> studentsEmails;
    private ArrayList<String> instructorsEmails;

    public Course(String id, String buildingName, ArrayList<String> studentsEmails, ArrayList<String> instructorsEmails, CourseMode mode) {
        this.courseMode = mode;
        this.id = id;
        this.buildingName = buildingName;
        this.studentsEmails = studentsEmails;
        this.instructorsEmails = instructorsEmails;
    }

    public Course() {
    }

    public String toString() {
        return "Course(id=" + this.getId() + ", building=" + this.getBuildingName() + ", students=" + this.getStudentsEmails() + ", instructors=" + this.getInstructorsEmails() + ")";
    }

    public CourseMode getCourseMode() {
        return courseMode;
    }

    public void setCourseMode(CourseMode courseMode) {
        this.courseMode = courseMode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public ArrayList<String> getStudentsEmails() {
        return studentsEmails;
    }

    public void setStudentsEmails(ArrayList<String> studentsEmails) {
        this.studentsEmails = studentsEmails;
    }

    public ArrayList<String> getInstructorsEmails() {
        return instructorsEmails;
    }

    public void setInstructorsEmails(ArrayList<String> instructorsEmails) {
        this.instructorsEmails = instructorsEmails;
    }

    public enum CourseMode {
        REMOTE,
        INPERSON
    }

}
