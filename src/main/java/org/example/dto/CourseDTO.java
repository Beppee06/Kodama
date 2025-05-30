package org.example.dto;

import java.time.LocalDate;

public class CourseDTO {
    private Integer courseId;
    private Integer teacherId; // Include Teacher ID instead of the full TeacherDTO/Entity
    private String name;
    private Integer year;
    private String subject;
    private Integer maxSubscribed;
    private String classroom;
    private LocalDate startDate;
    private LocalDate finishDate;

    // Constructors
    public CourseDTO() {
    }

    public CourseDTO(Integer courseId, Integer teacherId, String name, Integer year, String subject, Integer maxSubscribed, String classroom, LocalDate startDate, LocalDate finishDate) {
        this.courseId = courseId;
        this.teacherId = teacherId;
        this.name = name;
        this.year = year;
        this.subject = subject;
        this.maxSubscribed = maxSubscribed;
        this.classroom = classroom;
        this.startDate = startDate;
        this.finishDate = finishDate;
    }

    // Getters and Setters for all fields...
    public Integer getCourseId() { return courseId; }
    public void setCourseId(Integer courseId) { this.courseId = courseId; }
    public Integer getTeacherId() { return teacherId; }
    public void setTeacherId(Integer teacherId) { this.teacherId = teacherId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public Integer getMaxSubscribed() { return maxSubscribed; }
    public void setMaxSubscribed(Integer maxSubscribed) { this.maxSubscribed = maxSubscribed; }
    public String getClassroom() { return classroom; }
    public void setClassroom(String classroom) { this.classroom = classroom; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getFinishDate() { return finishDate; }
    public void setFinishDate(LocalDate finishDate) { this.finishDate = finishDate; }
}