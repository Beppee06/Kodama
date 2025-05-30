package org.example.dto;

public class SubscribedDTO {
    private Integer subscribedId;
    private Integer studentId;    // ID of the student
    private Integer courseId;   // ID of the course

    // Constructors
    public SubscribedDTO() {
    }

    public SubscribedDTO(Integer subscribedId, Integer studentId, Integer courseId) {
        this.subscribedId = subscribedId;
        this.studentId = studentId;
        this.courseId = courseId;
    }

    // Getters and Setters
    public Integer getSubscribedId() {
        return subscribedId;
    }

    public void setSubscribedId(Integer subscribedId) {
        this.subscribedId = subscribedId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }
}