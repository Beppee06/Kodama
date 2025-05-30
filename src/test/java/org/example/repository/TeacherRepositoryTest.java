package org.example.repository;

import org.example.models.Teacher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class TeacherRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TeacherRepository teacherRepository;

    @Test
    public void testExistsByEmail_WhenEmailExists_ShouldReturnTrue() {
        // Given
        Teacher teacher = new Teacher("teacher@example.com", "password");
        entityManager.persist(teacher);
        entityManager.flush();

        // When
        boolean exists = teacherRepository.existsByEmail("teacher@example.com");

        // Then
        assertTrue(exists);
    }

    @Test
    public void testExistsByEmail_WhenEmailDoesNotExist_ShouldReturnFalse() {
        // When
        boolean exists = teacherRepository.existsByEmail("nonexistent@example.com");

        // Then
        assertFalse(exists);
    }

    @Test
    public void testFindByEmail_WhenEmailExists_ShouldReturnTeacher() {
        // Given
        Teacher teacher = new Teacher("teacher@example.com", "password");
        entityManager.persist(teacher);
        entityManager.flush();

        // When
        Teacher found = teacherRepository.findByEmail("teacher@example.com");

        // Then
        assertNotNull(found);
        assertEquals("teacher@example.com", found.getEmail());
        assertEquals("password", found.getPassword());
    }

    @Test
    public void testFindByEmail_WhenEmailDoesNotExist_ShouldReturnNull() {
        // When
        Teacher found = teacherRepository.findByEmail("nonexistent@example.com");

        // Then
        assertNull(found);
    }

    @Test
    public void testSaveTeacher() {
        // Given
        Teacher teacher = new Teacher("new@example.com", "password");

        // When
        Teacher saved = teacherRepository.save(teacher);

        // Then
        assertNotNull(saved.getTeacherId());
        Teacher found = entityManager.find(Teacher.class, saved.getTeacherId());
        assertNotNull(found);
        assertEquals("new@example.com", found.getEmail());
        assertEquals("password", found.getPassword());
    }

    @Test
    public void testDeleteTeacher() {
        // Given
        Teacher teacher = new Teacher("delete@example.com", "password");
        entityManager.persist(teacher);
        entityManager.flush();
        Integer id = teacher.getTeacherId();

        // When
        teacherRepository.deleteById(id);

        // Then
        Teacher deleted = entityManager.find(Teacher.class, id);
        assertNull(deleted);
    }
}