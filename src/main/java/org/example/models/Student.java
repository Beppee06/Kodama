package org.example.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.Objects; // For equals/hashCode implementation

@Entity // Marks this class as a JPA entity (maps to a database table)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "student") // Optional: Specifies the table name (defaults to class name)
public class Student {
    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tells JPA to use DB auto-increment
    @Column(name = "student_id", updatable = false, nullable = false) // Optional: Customize column name, prevent updates
    private Integer studentId; // Use Integer (wrapper class) instead of primitive int because of its nullable

    @Column(name = "email", nullable = false, unique = true) // Optional: Customize, add constraints
    private String email;

    @Column(name = "password", nullable = false) // Optional: Customize, mark as non-nullable
    private String password;

    // JPA requires a no-argument constructor (can be protected or public)
    public Student() {
    }

    public Student(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // --- Optional but Recommended: equals() and hashCode() ---
    // Often based on the primary key (StudentId) if it's assigned

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        // Use Objects.equals to handle null IDs safely, though IDs should typically not be null
        return Objects.equals(studentId, student.studentId);
    }

    @Override
    public String toString() {
        return "Student{" +
                "StudentId=" + studentId +
                ", email='" + email + '\'' +
                '}';
    }

    public void setId(int i) {
    }
}