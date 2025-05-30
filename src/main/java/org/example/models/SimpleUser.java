package org.example.models;

import jakarta.persistence.*;
import java.util.Objects; // For equals/hashCode implementation

public class SimpleUser {

    private String email;

    private String password;

    // JPA requires a no-argument constructor (can be protected or public)
    public SimpleUser() {
    }

    public SimpleUser(String email, String password) {
        this.email = email;
        this.password = password;
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

    @Override
    public String toString() {
        return "Student{" +
                "StudentId=" +
                ", email='" + email + '\'' +
                '}';
    }
}