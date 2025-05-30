package org.example.models;

public class CourseId {
    private Integer course_id;

    public CourseId(Integer course_id) {
        this.course_id = course_id;
    }

    public CourseId() {}

    public Integer getCourse_id() {
        return course_id;
    }
    public void setCourse_id(Integer course_id) {
        this.course_id = course_id;
    }
}
