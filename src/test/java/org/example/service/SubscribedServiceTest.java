package org.example.service;

import org.example.exception.EmailAlreadyExistException;
import org.example.exception.ResourceNotFoundException;
import org.example.exception.TokenInvalid;
import org.example.models.Course;
import org.example.models.Student;
import org.example.models.Subscribed;
import org.example.models.Teacher;
import org.example.repository.CourseRepository;
import org.example.repository.SubscribedRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscribedServiceTest {

    @Mock
    private SubscribedRepository subscribedRepository;

    @Mock
    private StudentService studentService;

    @Mock
    private TeacherService teacherService;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private SubscribedService subscribedService;

    private final String VALID_TOKEN = "valid-token";
    private final Integer COURSE_ID = 1;
    private Student student;
    private Course course;
    private Teacher teacher;

    @BeforeEach
    void setUp() {
        student = new Student("student@test.com", "password");
        student.setId(1);

        teacher = new Teacher("teacher@test.com", "password");
        teacher.setId(1);

        course = new Course();
        course.setCourseId(COURSE_ID);
        course.setTeacher(teacher);
        course.setMaxSubscribed(10);
    }

    @Test
    void subscribeStudentToCourse_Success() {
        // Arrange
        when(studentService.getStudentByToken(VALID_TOKEN)).thenReturn(student);
        when(courseRepository.findById(COURSE_ID)).thenReturn(Optional.of(course));
        when(subscribedRepository.existsByStudentAndCourse(student, course)).thenReturn(false);
        when(subscribedRepository.countByCourse(course)).thenReturn(5L);
        when(subscribedRepository.save(any(Subscribed.class))).thenReturn(new Subscribed(student, course));

        // Act
        String result = subscribedService.subscribeStudentToCourse(VALID_TOKEN, COURSE_ID);

        // Assert
        assertEquals("Subscription successful.", result);
        verify(subscribedRepository, times(1)).save(any(Subscribed.class));
    }

    @Test
    void subscribeStudentToCourse_CourseNotFound() {
        // Arrange
        when(studentService.getStudentByToken(VALID_TOKEN)).thenReturn(student);
        when(courseRepository.findById(COURSE_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            subscribedService.subscribeStudentToCourse(VALID_TOKEN, COURSE_ID);
        });
    }

    @Test
    void subscribeStudentToCourse_CourseFinished() {
        // Arrange
        when(studentService.getStudentByToken(VALID_TOKEN)).thenReturn(student);

        course.setFinishDate(LocalDate.now().minusDays(1)); // Course finished yesterday
        when(courseRepository.findById(COURSE_ID)).thenReturn(Optional.of(course));

        // Act & Assert
        assertThrows(TokenInvalid.class, () -> {
            subscribedService.subscribeStudentToCourse(VALID_TOKEN, COURSE_ID);
        });
    }

    @Test
    void subscribeStudentToCourse_AlreadySubscribed() {
        // Arrange
        when(studentService.getStudentByToken(VALID_TOKEN)).thenReturn(student);
        when(courseRepository.findById(COURSE_ID)).thenReturn(Optional.of(course));
        when(subscribedRepository.existsByStudentAndCourse(student, course)).thenReturn(true);

        // Act & Assert
        assertThrows(EmailAlreadyExistException.class, () -> {
            subscribedService.subscribeStudentToCourse(VALID_TOKEN, COURSE_ID);
        });
    }

    @Test
    void subscribeStudentToCourse_CourseFull() {
        // Arrange
        when(studentService.getStudentByToken(VALID_TOKEN)).thenReturn(student);
        when(courseRepository.findById(COURSE_ID)).thenReturn(Optional.of(course));
        when(subscribedRepository.existsByStudentAndCourse(student, course)).thenReturn(false);
        when(subscribedRepository.countByCourse(course)).thenReturn(10L); // Course is full

        // Act & Assert
        assertThrows(TokenInvalid.class, () -> {
            subscribedService.subscribeStudentToCourse(VALID_TOKEN, COURSE_ID);
        });
    }

    @Test
    void unsubscribeStudentFromCourse_Success() {
        // Arrange
        when(studentService.getStudentByToken(VALID_TOKEN)).thenReturn(student);
        when(courseRepository.existsById(COURSE_ID)).thenReturn(true);
        when(courseRepository.findById(COURSE_ID)).thenReturn(Optional.of(course));

        Subscribed subscription = new Subscribed(student, course);
        when(subscribedRepository.findByStudentAndCourse(student, course)).thenReturn(Optional.of(subscription));
        doNothing().when(subscribedRepository).delete(subscription);

        // Act
        String result = subscribedService.unsubscribeStudentFromCourse(VALID_TOKEN, COURSE_ID);

        // Assert
        assertEquals("Unsubscription successful.", result);
        verify(subscribedRepository, times(1)).delete(subscription);
    }

    @Test
    void unsubscribeStudentFromCourse_CourseNotFound() {
        // Arrange
        when(studentService.getStudentByToken(VALID_TOKEN)).thenReturn(student);
        when(courseRepository.existsById(COURSE_ID)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            subscribedService.unsubscribeStudentFromCourse(VALID_TOKEN, COURSE_ID);
        });
    }

    @Test
    void unsubscribeStudentFromCourse_NotSubscribed() {
        // Arrange
        when(studentService.getStudentByToken(VALID_TOKEN)).thenReturn(student);
        when(courseRepository.existsById(COURSE_ID)).thenReturn(true);
        when(courseRepository.findById(COURSE_ID)).thenReturn(Optional.of(course));
        when(subscribedRepository.findByStudentAndCourse(student, course)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            subscribedService.unsubscribeStudentFromCourse(VALID_TOKEN, COURSE_ID);
        });
    }

    @Test
    void getStudentsSubscribedToCourse_Success() {
        // Arrange
        when(teacherService.getTeacherByToken(VALID_TOKEN)).thenReturn(teacher);
        when(courseRepository.findById(COURSE_ID)).thenReturn(Optional.of(course));

        List<Subscribed> subscriptions = new ArrayList<>();
        subscriptions.add(new Subscribed(student, course));

        Student student2 = new Student("student2@test.com", "password");
        student2.setId(2);
        subscriptions.add(new Subscribed(student2, course));

        when(subscribedRepository.findByCourse(course)).thenReturn(subscriptions);

        // Act
        List<Student> result = subscribedService.getStudentsSubscribedToCourse(VALID_TOKEN, COURSE_ID);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(student));
        assertTrue(result.contains(student2));
    }

    @Test
    void getStudentsSubscribedToCourse_CourseNotFound() {
        // Arrange
        when(teacherService.getTeacherByToken(VALID_TOKEN)).thenReturn(teacher);
        when(courseRepository.findById(COURSE_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            subscribedService.getStudentsSubscribedToCourse(VALID_TOKEN, COURSE_ID);
        });
    }

    @Test
    void getStudentsSubscribedToCourse_UnauthorizedTeacher() {
        // Arrange
        Teacher differentTeacher = new Teacher("different@test.com", "password");
        differentTeacher.setId(2);

        when(teacherService.getTeacherByToken(VALID_TOKEN)).thenReturn(differentTeacher);
        when(courseRepository.findById(COURSE_ID)).thenReturn(Optional.of(course));

        // Act & Assert
        assertThrows(TokenInvalid.class, () -> {
            subscribedService.getStudentsSubscribedToCourse(VALID_TOKEN, COURSE_ID);
        });
    }
}