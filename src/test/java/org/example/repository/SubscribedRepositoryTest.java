package org.example.repository;

import org.example.models.Course;
import org.example.models.Student;
import org.example.models.Teacher;
import org.example.models.Subscribed;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscribedRepositoryTest {

    @Mock
    private SubscribedRepository subscribedRepository;

    @Test
    void testSave() {
        // Arrange
        Student student = new Student("test@email.com", "password");
        student.setId(1);

        Teacher teacher = new Teacher("teacher@email.com", "password");
        teacher.setId(1);

        Course course = new Course();
        course.setCourseId(1);
        course.setTeacher(teacher);

        Subscribed subscribed = new Subscribed(student, course);

        when(subscribedRepository.save(any(Subscribed.class))).thenReturn(subscribed);

        // Act
        Subscribed savedSubscribed = subscribedRepository.save(subscribed);

        // Assert
        assertNotNull(savedSubscribed);
        assertEquals(student, savedSubscribed.getStudent());
        assertEquals(course, savedSubscribed.getCourse());
        verify(subscribedRepository, times(1)).save(subscribed);
    }

    @Test
    void testFindByStudentAndCourse() {
        // Arrange
        Student student = new Student("test@email.com", "password");
        student.setId(1);

        Teacher teacher = new Teacher("teacher@email.com", "password");
        teacher.setId(1);

        Course course = new Course();
        course.setCourseId(1);
        course.setTeacher(teacher);

        Subscribed subscribed = new Subscribed(student, course);

        when(subscribedRepository.findByStudentAndCourse(student, course))
                .thenReturn(Optional.of(subscribed));

        // Act
        Optional<Subscribed> foundSubscribed = subscribedRepository.findByStudentAndCourse(student, course);

        // Assert
        assertTrue(foundSubscribed.isPresent());
        assertEquals(subscribed, foundSubscribed.get());
        verify(subscribedRepository, times(1)).findByStudentAndCourse(student, course);
    }

    @Test
    void testExistsByStudentAndCourse() {
        // Arrange
        Student student = new Student("test@email.com", "password");
        student.setId(1);

        Teacher teacher = new Teacher("teacher@email.com", "password");
        teacher.setId(1);

        Course course = new Course();
        course.setCourseId(1);
        course.setTeacher(teacher);

        when(subscribedRepository.existsByStudentAndCourse(student, course)).thenReturn(true);

        // Act
        boolean exists = subscribedRepository.existsByStudentAndCourse(student, course);

        // Assert
        assertTrue(exists);
        verify(subscribedRepository, times(1)).existsByStudentAndCourse(student, course);
    }

    @Test
    void testCountByCourse() {
        // Arrange
        Teacher teacher = new Teacher("teacher@email.com", "password");
        teacher.setId(1);

        Course course = new Course();
        course.setCourseId(1);
        course.setTeacher(teacher);

        when(subscribedRepository.countByCourse(course)).thenReturn(5L);

        // Act
        long count = subscribedRepository.countByCourse(course);

        // Assert
        assertEquals(5L, count);
        verify(subscribedRepository, times(1)).countByCourse(course);
    }

    @Test
    void testFindByCourse() {
        // Arrange
        Teacher teacher = new Teacher("teacher@email.com", "password");
        teacher.setId(1);

        Course course = new Course();
        course.setCourseId(1);
        course.setTeacher(teacher);

        List<Subscribed> subscriptionList = new ArrayList<>();
        Student student1 = new Student("student1@email.com", "password");
        student1.setId(1);
        Student student2 = new Student("student2@email.com", "password");
        student2.setId(2);

        subscriptionList.add(new Subscribed(student1, course));
        subscriptionList.add(new Subscribed(student2, course));

        when(subscribedRepository.findByCourse(course)).thenReturn(subscriptionList);

        // Act
        List<Subscribed> foundSubscriptions = subscribedRepository.findByCourse(course);

        // Assert
        assertNotNull(foundSubscriptions);
        assertEquals(2, foundSubscriptions.size());
        verify(subscribedRepository, times(1)).findByCourse(course);
    }

    @Test
    void testDeleteSubscription() {
        // Arrange
        Student student = new Student("test@email.com", "password");
        student.setId(1);

        Teacher teacher = new Teacher("teacher@email.com", "password");
        teacher.setId(1);

        Course course = new Course();
        course.setCourseId(1);
        course.setTeacher(teacher);

        Subscribed subscribed = new Subscribed(student, course);

        doNothing().when(subscribedRepository).delete(subscribed);

        // Act
        subscribedRepository.delete(subscribed);

        // Assert
        verify(subscribedRepository, times(1)).delete(subscribed);
    }
}