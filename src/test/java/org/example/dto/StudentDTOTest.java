package org.example.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StudentDTOTest {

    @Test
    public void testEmptyConstructor() {
        StudentDTO student = new StudentDTO();
        assertNull(student.getStudentId());
        assertNull(student.getEmail());
    }

    @Test
    public void testParameterizedConstructor() {
        Integer studentId = 1;
        String email = "student@example.com";

        StudentDTO student = new StudentDTO(studentId, email);

        assertEquals(studentId, student.getStudentId());
        assertEquals(email, student.getEmail());
    }

    @Test
    public void testGettersAndSetters() {
        StudentDTO student = new StudentDTO();

        Integer studentId = 1;
        String email = "student@example.com";

        student.setStudentId(studentId);
        student.setEmail(email);

        assertEquals(studentId, student.getStudentId());
        assertEquals(email, student.getEmail());
    }
}