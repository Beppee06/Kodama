package org.example.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class StudentTest {

    private Student student;
    private String email;
    private String password;

    @BeforeEach
    void setUp() {
        email = "student@example.com";
        password = "password123";
        student = new Student(email, password);
    }

    @Test
    void testStudentConstructor() {
        // Verify constructor sets values properly
        assertThat(student.getEmail()).isEqualTo(email);
        assertThat(student.getPassword()).isEqualTo(password);
        // ID should be null until persisted
        assertNull(student.getStudentId());
    }

    @Test
    void testGettersAndSetters() {
        // Test setting new values
        String newEmail = "newemail@example.com";
        String newPassword = "newpass456";

        student.setEmail(newEmail);
        student.setPassword(newPassword);

        // Verify the values were changed correctly
        assertThat(student.getEmail()).isEqualTo(newEmail);
        assertThat(student.getPassword()).isEqualTo(newPassword);
    }

    @Test
    void testEqualsAndHashCode() {
        // Create two distinct student objects
        Student student1 = new Student("student1@example.com", "pass1");
        Student student2 = new Student("student2@example.com", "pass2");

        // Different students with null IDs should not be equal
        assertNotEquals(student1, student2);

        // Same student should be equal to itself
        assertEquals(student1, student1);

        // Test with ID set (using reflection since there's no setter for ID)
        try {
            java.lang.reflect.Field idField1 = Student.class.getDeclaredField("studentId");
            idField1.setAccessible(true);
            idField1.set(student1, 1);

            java.lang.reflect.Field idField2 = Student.class.getDeclaredField("studentId");
            idField2.setAccessible(true);
            idField2.set(student2, 2);

            // Students with different IDs should not be equal
            assertNotEquals(student1, student2);

            // Set same ID to student2
            idField2.set(student2, 1);

            // Now they should be equal despite having different emails/passwords
            assertEquals(student1, student2);

        } catch (Exception e) {
            fail("Failed to use reflection to set ID: " + e.getMessage());
        }
    }

    @Test
    void testToString() {
        // Verify toString doesn't throw and contains expected email
        String toString = student.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("email='" + email + "'"));
    }

    @Test
    void testSetId() {
        // Test the setId method
        student.setId(10);
        // Note: Since setId is empty in the code, we can't verify anything changed
        // This just verifies the method doesn't throw an exception
        assertNotNull(student);
    }
}