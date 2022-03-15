package com.cs310.covider.model;

import java.util.ArrayList;
import java.util.Objects;

public class Course {

    public Course(String id, Building building, ArrayList<User> students, ArrayList<User> instructors) {
        this.id = id;
        this.building = building;
        this.students = students;
        this.instructors = instructors;
    }

    public String getId() {
        return this.id;
    }

    public Building getBuilding() {
        return this.building;
    }

    public ArrayList<User> getStudents() {
        return this.students;
    }

    public ArrayList<User> getInstructors() {
        return this.instructors;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public void setStudents(ArrayList<User> students) {
        this.students = students;
    }

    public void setInstructors(ArrayList<User> instructors) {
        this.instructors = instructors;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Course)) {
            return false;
        }
        final Course other = (Course) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        return Objects.equals(this$id, other$id);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Course;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        return result;
    }

    public String toString() {
        return "Course(id=" + this.getId() + ", building=" + this.getBuilding() + ", students=" + this.getStudents() + ", instructors=" + this.getInstructors() + ")";
    }

    public enum CourseMode{
        REMOTE,
        INPERSON
    }

    private String id;
    private Building building;
    private ArrayList<User> students;
    private ArrayList<User> instructors;

}
