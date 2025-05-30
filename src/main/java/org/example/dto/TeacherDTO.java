package org.example.dto;

public class TeacherDTO {
    private Integer teacherId;
    private String email;
    // We DO NOT include the password in the DTO

    // Constructors
    public TeacherDTO() {
    }

    public TeacherDTO(Integer teacherId, String email) {
        this.teacherId = teacherId;
        this.email = email;
    }

    // Getters and Setters
    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}