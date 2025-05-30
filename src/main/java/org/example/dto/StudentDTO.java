package org.example.dto;

public class StudentDTO {
    private Integer studentId;
    private String email;
    // We DO NOT include the password in the DTO

    // Constructors
    public StudentDTO() {
    }

    public StudentDTO(Integer studentId, String email) {
        this.studentId = studentId;
        this.email = email;
    }

    // Getters and Setters
    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}


