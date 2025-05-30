package org.example.models;

import java.time.LocalDate;

public class SimplerCourse {
    private String token;
    private String name;
    private Integer year;
    private String subject;
    private Integer maxSubscribed;
    private Integer classroom;
    private LocalDate startDate;
    private LocalDate finishDate;
    private String type;
    private String description;

    // JPA requires a no-argument constructor (can be protected or public)
    public SimplerCourse() {
    }

    // Se ho solo il token
    public SimplerCourse(String token, String name, int year, String subject, int maxSubscribed, Integer classroom, LocalDate startDate, LocalDate finishDate, String type, String description) {
        this.token = token;
        this.name = name;
        this.year = year;
        this.subject = subject;
        this.maxSubscribed = maxSubscribed;
        this.classroom = classroom;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.type = type;
        this.description = description;
    }

    // --- Getters and Setters ---

    public String getTeacherToken() {
        return token;
    }

    public void setTeacherToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getMaxSubscribed() {
        return maxSubscribed;
    }

    public void setMaxSubscribed(int maxSubscribed) {
        this.maxSubscribed = maxSubscribed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getClassroom() {
        return classroom;
    }

    public void setClassroom(Integer classroom) {
        this.classroom = classroom;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDate finishDate) {
        this.finishDate = finishDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // --- equals(), hashCode(), toString() ---

    @Override
    public String toString() {
        return "Course{" +
                ", token='" + token + '\'' +
                ", name='" + name + '\'' +
                ", year=" + year +
                ", subject='" + subject + '\'' +
                ", maxSubscribed=" + maxSubscribed +
                ", classroom='" + classroom + '\'' +
                ", startDate=" + startDate +
                ", finishDate=" + finishDate + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description +
                '}';
    }
}
