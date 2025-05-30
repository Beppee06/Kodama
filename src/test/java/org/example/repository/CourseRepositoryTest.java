/*

package org.example.repository;

import org.example.models.Course;
import org.example.models.Teacher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CourseRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CourseRepository courseRepository;

    private Teacher createAndPersistTeacher() {
        Teacher teacher = new Teacher("teacher@example.com", "password");
        entityManager.persist(teacher);
        entityManager.flush();
        return teacher;
    }

    @Test
    public void testSaveCourse() {
        // Given
        Teacher teacher = createAndPersistTeacher();

        LocalDate startDate = LocalDate.of(2025, 5, 1);
        LocalDate finishDate = LocalDate.of(2025, 7, 31);

        Course course = new Course(teacher, "Java Programming", 2025, "Computer Science",
                30, "Room 101", startDate, finishDate, "Online");

        // When
        Course saved = courseRepository.save(course);

        // Then
        assertNotNull(saved.getCourseId());
        Course found = entityManager.find(Course.class, saved.getCourseId());
        assertNotNull(found);
        assertEquals("Java Programming", found.getName());
        assertEquals("Computer Science", found.getSubject());
        assertEquals(teacher.getTeacherId(), found.getTeacher().getTeacherId());
    }

    @Test
    public void testFindCourseById() {
        // Given
        Teacher teacher = createAndPersistTeacher();

        LocalDate startDate = LocalDate.of(2025, 6, 1);
        LocalDate finishDate = LocalDate.of(2025, 8, 31);

        Course course = new Course(teacher, "Database Design", 2025, "Computer Science",
                25, "Room 102", startDate, finishDate, "In-person");
        entityManager.persist(course);
        entityManager.flush();
        Integer id = course.getCourseId();

        // When
        Optional<Course> foundOptional = courseRepository.findById(id);

        // Then
        assertTrue(foundOptional.isPresent());
        Course found = foundOptional.get();
        assertEquals("Database Design", found.getName());
        assertEquals("Computer Science", found.getSubject());
        assertEquals(teacher.getTeacherId(), found.getTeacher().getTeacherId());
    }

    @Test
    public void testFindAllCourses() {
        // Given
        Teacher teacher = createAndPersistTeacher();

        LocalDate startDate1 = LocalDate.of(2025, 9, 1);
        LocalDate finishDate1 = LocalDate.of(2025, 12, 15);

        Course course1 = new Course(teacher, "Python Programming", 2025, "Computer Science",
                35, "Room 103", startDate1, finishDate1, "Hybrid");
        entityManager.persist(course1);

        LocalDate startDate2 = LocalDate.of(2025, 9, 1);
        LocalDate finishDate2 = LocalDate.of(2025, 12, 15);

        Course course2 = new Course(teacher, "Web Development", 2025, "Computer Science",
                40, "Room 104", startDate2, finishDate2, "In-person");
        entityManager.persist(course2);
        entityManager.flush();

        // When
        List<Course> courses = courseRepository.findAll();

        // Then
        assertFalse(courses.isEmpty());
        assertTrue(courses.size() >= 2);
        assertTrue(courses.stream().anyMatch(c -> c.getName().equals("Python Programming")));
        assertTrue(courses.stream().anyMatch(c -> c.getName().equals("Web Development")));
    }

    @Test
    public void testUpdateCourse() {
        // Given
        Teacher teacher = createAndPersistTeacher();

        LocalDate startDate = LocalDate.of(2025, 1, 15);
        LocalDate finishDate = LocalDate.of(2025, 5, 15);

        Course course = new Course(teacher, "Original Course", 2025, "Original Subject",
                20, "Room 105", startDate, finishDate, "Online");
        entityManager.persist(course);
        entityManager.flush();
        Integer id = course.getCourseId();

        // When
        Course toUpdate = entityManager.find(Course.class, id);
        toUpdate.setName("Updated Course");
        toUpdate.setSubject("Updated Subject");
        courseRepository.save(toUpdate);

        // Then
        Course updated = entityManager.find(Course.class, id);
        assertEquals("Updated Course", updated.getName());
        assertEquals("Updated Subject", updated.getSubject());
    }

    @Test
    public void testDeleteCourse() {
        // Given
        Teacher teacher = createAndPersistTeacher();

        LocalDate startDate = LocalDate.of(2025, 3, 1);
        LocalDate finishDate = LocalDate.of(2025, 6, 30);

        Course course = new Course(teacher, "Course to Delete", 2025, "Test Subject",
                15, "Room 106", startDate, finishDate, "In-person");
        entityManager.persist(course);
        entityManager.flush();
        Integer id = course.getCourseId();

        // When
        courseRepository.deleteById(id);

        // Then
        Course deleted = entityManager.find(Course.class, id);
        assertNull(deleted);
    }
}

 */