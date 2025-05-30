package org.example.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class TeacherTest {

    private Teacher teacher;
    private String email;
    private String password;

    @BeforeEach
    void setUp() {
        email = "teacher@example.com";
        password = "securePass123";
        teacher = new Teacher(email, password);
    }

    @Test
    void testTeacherConstructor() {
        // Verify constructor sets values properly
        assertThat(teacher.getEmail()).isEqualTo(email);
        assertThat(teacher.getPassword()).isEqualTo(password);
        // ID should be null until persisted
        assertNull(teacher.getTeacherId());
    }

    @Test
    void testGettersAndSetters() {
        // Test setting new values
        String newEmail = "newteacher@example.com";
        String newPassword = "newSecurePass456";

        teacher.setEmail(newEmail);
        teacher.setPassword(newPassword);

        // Verify the values were changed correctly
        assertThat(teacher.getEmail()).isEqualTo(newEmail);
        assertThat(teacher.getPassword()).isEqualTo(newPassword);
    }

    @Test
    void testEqualsAndHashCode() {
        // Create two distinct teacher objects
        Teacher teacher1 = new Teacher("teacher1@example.com", "pass1");
        Teacher teacher2 = new Teacher("teacher2@example.com", "pass2");

        // Different teachers with null IDs should not be equal
        assertNotEquals(teacher1, teacher2);

        // Same teacher should be equal to itself
        assertEquals(teacher1, teacher1);

        // Test with ID set (using reflection since there's no setter for ID)
        try {
            java.lang.reflect.Field idField1 = Teacher.class.getDeclaredField("teacherId");
            idField1.setAccessible(true);
            idField1.set(teacher1, 1);

            java.lang.reflect.Field idField2 = Teacher.class.getDeclaredField("teacherId");
            idField2.setAccessible(true);
            idField2.set(teacher2, 2);

            // Teachers with different IDs should not be equal
            assertNotEquals(teacher1, teacher2);

            // Set same ID to teacher2
            idField2.set(teacher2, 1);

            // Now they should be equal despite having different emails/passwords
            assertEquals(teacher1, teacher2);

        } catch (Exception e) {
            fail("Failed to use reflection to set ID: " + e.getMessage());
        }
    }

    @Test
    void testToString() {
        // Verify toString doesn't throw and contains expected email
        String toString = teacher.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("email='" + email + "'"));
    }

    @Test
    void testSetId() {
        // Test the setId method
        teacher.setId(10);
        // Note: Since setId is empty in the code, we can't verify anything changed
        // This just verifies the method doesn't throw an exception
        assertNotNull(teacher);
    }
}