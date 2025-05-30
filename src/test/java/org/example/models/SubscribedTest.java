package org.example.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubscribedTest {

    private Student student;
    private Course course;
    private Subscribed subscribed;

    @BeforeEach
    void setUp() {
        student = new Student("test@email.com", "password");
        student.setId(1);

        Teacher teacher = new Teacher("teacher@email.com", "password");
        teacher.setId(1);

        course = new Course();
        course.setCourseId(1);
        course.setTeacher(teacher);

        subscribed = new Subscribed(student, course);
    }

    @Test
    void testConstructor() {
        assertNull(subscribed.getSubscribedId());
        assertEquals(student, subscribed.getStudent());
        assertEquals(course, subscribed.getCourse());
    }

    @Test
    void testGettersAndSetters() {
        Student newStudent = new Student("new@email.com", "password");
        newStudent.setId(2);

        Teacher newTeacher = new Teacher("newteacher@email.com", "password");
        newTeacher.setId(2);

        Course newCourse = new Course();
        newCourse.setCourseId(2);
        newCourse.setTeacher(newTeacher);

        subscribed.setStudent(newStudent);
        subscribed.setCourse(newCourse);

        assertEquals(newStudent, subscribed.getStudent());
        assertEquals(newCourse, subscribed.getCourse());
    }

    @Test
    void testEquals() {
        assertEquals(subscribed, subscribed);

        Subscribed otherSubscribed = new Subscribed(student, course);
        assertNotEquals(subscribed, otherSubscribed);

        Subscribed subscribed1 = new Subscribed(student, course);
        Subscribed subscribed2 = new Subscribed(student, course);

        try {
            java.lang.reflect.Field idField = Subscribed.class.getDeclaredField("subscribedId");
            idField.setAccessible(true);
            idField.set(subscribed1, 1);
            idField.set(subscribed2, 1);

            assertEquals(subscribed1, subscribed2);

            idField.set(subscribed2, 2);
            assertNotEquals(subscribed1, subscribed2);

        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    void testToString() {
        String toStringResult = subscribed.toString();

        assertTrue(toStringResult.contains("subscribedId="));
        assertTrue(toStringResult.contains("studentId="));
        assertTrue(toStringResult.contains("courseId="));
    }

    @Test
    void testNullValues() {
        Subscribed emptySubscribed = new Subscribed();
        assertNull(emptySubscribed.getStudent());
        assertNull(emptySubscribed.getCourse());

        subscribed.setStudent(null);
        subscribed.setCourse(null);
        assertNull(subscribed.getStudent());
        assertNull(subscribed.getCourse());
    }
}