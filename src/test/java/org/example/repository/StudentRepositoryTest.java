package org.example.repository;

import org.example.models.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class StudentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    public void testExistsByEmail_WhenEmailExists_ShouldReturnTrue() {
        // Given
        Student student = new Student("test@example.com", "password");
        entityManager.persist(student);
        entityManager.flush();

        // When
        boolean exists = studentRepository.existsByEmail("test@example.com");

        // Then
        assertTrue(exists);
    }

    @Test
    public void testExistsByEmail_WhenEmailDoesNotExist_ShouldReturnFalse() {
        // When
        boolean exists = studentRepository.existsByEmail("nonexistent@example.com");

        // Then
        assertFalse(exists);
    }

    @Test
    public void testFindByEmail_WhenEmailExists_ShouldReturnStudent() {
        // Given
        Student student = new Student("test@example.com", "password");
        entityManager.persist(student);
        entityManager.flush();

        // When
        Student found = studentRepository.findByEmail("test@example.com");

        // Then
        assertNotNull(found);
        assertEquals("test@example.com", found.getEmail());
        assertEquals("password", found.getPassword());
    }

    @Test
    public void testFindByEmail_WhenEmailDoesNotExist_ShouldReturnNull() {
        // When
        Student found = studentRepository.findByEmail("nonexistent@example.com");

        // Then
        assertNull(found);
    }

    @Test
    public void testSaveStudent() {
        // Given
        Student student = new Student("new@example.com", "password");

        // When
        Student saved = studentRepository.save(student);

        // Then
        assertNotNull(saved.getStudentId());
        Student found = entityManager.find(Student.class, saved.getStudentId());
        assertNotNull(found);
        assertEquals("new@example.com", found.getEmail());
        assertEquals("password", found.getPassword());
    }

    @Test
    public void testDeleteStudent() {
        // Given
        Student student = new Student("delete@example.com", "password");
        entityManager.persist(student);
        entityManager.flush();
        Integer id = student.getStudentId();

        // When
        studentRepository.deleteById(id);

        // Then
        Student deleted = entityManager.find(Student.class, id);
        assertNull(deleted);
    }
}