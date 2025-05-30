package org.example.models;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EntityIntegrationTest {

    private static EntityManagerFactory emf;
    private static EntityManager em;

    private Student student;
    private Teacher teacher;
    private Course course;
    private Subscribed subscribed;

    @BeforeAll
    static void setupClass() {
        // Setup in-memory H2 database for testing
        // You'll need to create a test persistence unit in META-INF/persistence.xml
        emf = Persistence.createEntityManagerFactory("test-persistence-unit");
        em = emf.createEntityManager();
    }

    @AfterAll
    static void tearDownClass() {
        if (em != null) em.close();
        if (emf != null) emf.close();
    }

    @BeforeEach
    void setUp() {
        // Clear the database before each test
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Subscribed").executeUpdate();
        em.createQuery("DELETE FROM Course").executeUpdate();
        em.createQuery("DELETE FROM Student").executeUpdate();
        em.createQuery("DELETE FROM Teacher").executeUpdate();
        em.getTransaction().commit();

        // Prepare fresh entities for testing
        em.getTransaction().begin();

        student = new Student("student@test.com", "studentpass");
        em.persist(student);

        teacher = new Teacher("teacher@test.com", "teacherpass");
        em.persist(teacher);

        LocalDate startDate = LocalDate.of(2024, 9, 1);
        LocalDate finishDate = LocalDate.of(2025, 6, 30);

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
        em.persist(course);

        // Create subscription
        subscribed = new Subscribed(student, course);
        em.persist(subscribed);

        em.getTransaction().commit();

        // Clear persistence context to ensure we're reading from the database
        em.clear();
    }

    @Test
    void testPersistStudent() {
        Student fetchedStudent = em.find(Student.class, student.getStudentId());
        assertNotNull(fetchedStudent);
        assertEquals("student@test.com", fetchedStudent.getEmail());
    }

    @Test
    void testPersistTeacher() {
        Teacher fetchedTeacher = em.find(Teacher.class, teacher.getTeacherId());
        assertNotNull(fetchedTeacher);
        assertEquals("teacher@test.com", fetchedTeacher.getEmail());
    }

    @Test
    void testPersistCourse() {
        Course fetchedCourse = em.find(Course.class, course.getCourseId());
        assertNotNull(fetchedCourse);
        assertEquals("Corso Test", fetchedCourse.getName());
        assertEquals(2024, fetchedCourse.getYear());

        // Verify teacher relationship
        assertNotNull(fetchedCourse.getTeacher());
        assertEquals(teacher.getTeacherId(), fetchedCourse.getTeacher().getTeacherId());
    }

    @Test
    void testPersistSubscribed() {
        Subscribed fetchedSubscribed = em.find(Subscribed.class, subscribed.getSubscribedId());
        assertNotNull(fetchedSubscribed);

        // Verify relationships
        assertNotNull(fetchedSubscribed.getStudent());
        assertNotNull(fetchedSubscribed.getCourse());
        assertEquals(student.getStudentId(), fetchedSubscribed.getStudent().getStudentId());
        assertEquals(course.getCourseId(), fetchedSubscribed.getCourse().getCourseId());
    }

    @Test
    void testQueryStudentCourses() {
        // Query all courses a student is subscribed to
        List<Course> studentCourses = em.createQuery(
                        "SELECT s.course FROM Subscribed s WHERE s.student.studentId = :studentId",
                        Course.class)
                .setParameter("studentId", student.getStudentId())
                .getResultList();

        assertThat(studentCourses).hasSize(1);
        assertEquals(course.getCourseId(), studentCourses.get(0).getCourseId());
        assertEquals("Corso Test", studentCourses.get(0).getName());
    }

    @Test
    void testQueryTeacherCourses() {
        // Query all courses taught by a teacher
        List<Course> teacherCourses = em.createQuery(
                        "SELECT c FROM Course c WHERE c.teacher.teacherId = :teacherId",
                        Course.class)
                .setParameter("teacherId", teacher.getTeacherId())
                .getResultList();

        assertThat(teacherCourses).hasSize(1);
        assertEquals(course.getCourseId(), teacherCourses.get(0).getCourseId());
    }

    @Test
    void testUniqueConstraintOnSubscribed() {
        // This test verifies that we can't create duplicate subscriptions
        // for the same student and course (there's a unique constraint)

        em.getTransaction().begin();

        // Try to create another subscription for the same student and course
        Subscribed duplicateSubscription = new Subscribed(student, course);

        // This should throw an exception when we commit due to unique constraint
        try {
            em.persist(duplicateSubscription);
            em.flush(); // Force the persistence to happen now

            // If we get here, the test fails (should have thrown exception)
            em.getTransaction().rollback();
            org.junit.jupiter.api.Assertions.fail("Should have thrown exception for duplicate subscription");
        } catch (Exception e) {
            // Expected exception for unique constraint violation
            em.getTransaction().rollback();
            assertThat(e.getMessage()).contains("constraint");
        }
    }

    @Test
    void testCascadeDelete() {
        // Get IDs before deletion
        Integer courseId = course.getCourseId();
        Integer subscribedId = subscribed.getSubscribedId();

        // Delete course (should cascade to subscriptions)
        em.getTransaction().begin();
        Course managedCourse = em.find(Course.class, courseId);
        em.remove(managedCourse);
        em.getTransaction().commit();

        // Verify course is deleted
        assertThat(em.find(Course.class, courseId)).isNull();

        // Verify subscription is also deleted due to cascade
        assertThat(em.find(Subscribed.class, subscribedId)).isNull();
    }
}