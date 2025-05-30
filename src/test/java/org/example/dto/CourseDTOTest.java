package org.example.dto;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class CourseDTOTest {

    @Test
    public void testEmptyConstructor() {
        CourseDTO course = new CourseDTO();
        assertNull(course.getCourseId());
        assertNull(course.getTeacherId());
        assertNull(course.getName());
        assertNull(course.getYear());
        assertNull(course.getSubject());
        assertNull(course.getMaxSubscribed());
        assertNull(course.getClassroom());
        assertNull(course.getStartDate());
        assertNull(course.getFinishDate());
    }

    @Test
    public void testParameterizedConstructor() {
        Integer courseId = 1;
        Integer teacherId = 2;
        String name = "Java Programming";
        Integer year = 2025;
        String subject = "Computer Science";
        Integer maxSubscribed = 30;
        String classroom = "A101";
        LocalDate startDate = LocalDate.of(2025, 5, 1);
        LocalDate finishDate = LocalDate.of(2025, 7, 31);

        CourseDTO course = new CourseDTO(courseId, teacherId, name, year, subject,
                maxSubscribed, classroom, startDate, finishDate);

        assertEquals(courseId, course.getCourseId());
        assertEquals(teacherId, course.getTeacherId());
        assertEquals(name, course.getName());
        assertEquals(year, course.getYear());
        assertEquals(subject, course.getSubject());
        assertEquals(maxSubscribed, course.getMaxSubscribed());
        assertEquals(classroom, course.getClassroom());
        assertEquals(startDate, course.getStartDate());
        assertEquals(finishDate, course.getFinishDate());
    }

    @Test
    public void testGettersAndSetters() {
        CourseDTO course = new CourseDTO();

        Integer courseId = 1;
        Integer teacherId = 2;
        String name = "Java Programming";
        Integer year = 2025;
        String subject = "Computer Science";
        Integer maxSubscribed = 30;
        String classroom = "A101";
        LocalDate startDate = LocalDate.of(2025, 5, 1);
        LocalDate finishDate = LocalDate.of(2025, 7, 31);

        course.setCourseId(courseId);
        course.setTeacherId(teacherId);
        course.setName(name);
        course.setYear(year);
        course.setSubject(subject);
        course.setMaxSubscribed(maxSubscribed);
        course.setClassroom(classroom);
        course.setStartDate(startDate);
        course.setFinishDate(finishDate);

        assertEquals(courseId, course.getCourseId());
        assertEquals(teacherId, course.getTeacherId());
        assertEquals(name, course.getName());
        assertEquals(year, course.getYear());
        assertEquals(subject, course.getSubject());
        assertEquals(maxSubscribed, course.getMaxSubscribed());
        assertEquals(classroom, course.getClassroom());
        assertEquals(startDate, course.getStartDate());
        assertEquals(finishDate, course.getFinishDate());
    }
}