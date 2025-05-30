package org.example.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class CourseTest {

    private Course course;
    private Teacher teacher;
    private LocalDate startDate;
    private LocalDate finishDate;

    @BeforeEach
    void setUp() {
        // Create a teacher for testing
        teacher = new Teacher("teachertest@example.com", "password123");

        // Set dates for testing
        startDate = LocalDate.of(2024, 9, 1);
        finishDate = LocalDate.of(2025, 6, 30);

        // Create the course instance
        course = new Course(
                teacher,
                "Matematica Avanzata",
                2024,
                "Matematica",
                30,
                101,
                startDate,
                finishDate,
                "Obbligatorio",
                "integrali"

        );
    }

    @Test
    void testCourseConstructor() {
        // Verify that all properties are correctly set by the constructor
        assertThat(course.getTeacher()).isEqualTo(teacher);
        assertThat(course.getName()).isEqualTo("Matematica Avanzata");
        assertThat(course.getYear()).isEqualTo(2024);
        assertThat(course.getSubject()).isEqualTo("Matematica");
        assertThat(course.getMaxSubscribed()).isEqualTo(30);
        assertThat(course.getClassroom()).isEqualTo( 101);
        assertThat(course.getStartDate()).isEqualTo(startDate);
        assertThat(course.getFinishDate()).isEqualTo(finishDate);
        assertThat(course.getType()).isEqualTo("Obbligatorio");
    }

    @Test
    void testGettersAndSetters() {
        // Create new test values
        Teacher newTeacher = new Teacher("newteacher@example.com", "newpass");
        LocalDate newStartDate = LocalDate.of(2024, 10, 1);
        LocalDate newFinishDate = LocalDate.of(2025, 7, 31);

        // Use setters to change values
        course.setTeacher(newTeacher);
        course.setName("Fisica Applicata");
        course.setYear(2025);
        course.setSubject("Fisica");
        course.setMaxSubscribed(25);
        course.setClassroom(202);
        course.setStartDate(newStartDate);
        course.setFinishDate(newFinishDate);
        course.setType("Opzionale");

        // Verify the values were changed correctly
        assertThat(course.getTeacher()).isEqualTo(newTeacher);
        assertThat(course.getName()).isEqualTo("Fisica Applicata");
        assertThat(course.getYear()).isEqualTo(2025);
        assertThat(course.getSubject()).isEqualTo("Fisica");
        assertThat(course.getMaxSubscribed()).isEqualTo(25);
        assertThat(course.getClassroom()).isEqualTo(202);
        assertThat(course.getStartDate()).isEqualTo(newStartDate);
        assertThat(course.getFinishDate()).isEqualTo(newFinishDate);
        assertThat(course.getType()).isEqualTo("Opzionale");
    }

    @Test
    void testEqualsAndHashCode() {
        // Note: Since equals is based on courseId which is null until persisted,
        // we need to manually set it for this test
        Course course1 = new Course();
        Course course2 = new Course();

        // Two courses with null IDs should not be equal
        assertNotEquals(course1, course2);

        // Reflection to set IDs (since there's no setter for ID)
        try {
            java.lang.reflect.Field idField1 = Course.class.getDeclaredField("courseId");
            idField1.setAccessible(true);
            idField1.set(course1, 1);

            java.lang.reflect.Field idField2 = Course.class.getDeclaredField("courseId");
            idField2.setAccessible(true);
            idField2.set(course2, 2);

            // Now they should have IDs but be different
            assertNotEquals(course1, course2);

            // Set the same ID
            idField2.set(course2, 1);
            assertEquals(course1, course2);

        } catch (Exception e) {
            fail("Failed to use reflection to set ID: " + e.getMessage());
        }
    }

    @Test
    void testToString() {
        // Just verify that toString doesn't throw an exception
        String toString = course.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("name='Matematica Avanzata'"));
        assertTrue(toString.contains("subject='Matematica'"));
    }
}