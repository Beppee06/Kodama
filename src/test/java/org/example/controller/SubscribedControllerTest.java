package org.example.controller;

import org.example.exception.EmailAlreadyExistException;
import org.example.exception.ResourceNotFoundException;
import org.example.exception.TokenInvalid;
import org.example.models.CourseId;
import org.example.models.Student;
import org.example.service.SubscribedService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class SubscribedControllerTest {

    @Mock
    private SubscribedService subscribedService;

    @InjectMocks
    private SubscribedController subscribedController;

    private final String TOKEN = "valid-token";
    private final Integer COURSE_ID = 1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void subscribeStudent_Success() {
        // Arrange
        CourseId courseId = new CourseId();
        courseId.setCourse_id(COURSE_ID);
        when(subscribedService.subscribeStudentToCourse(TOKEN, COURSE_ID)).thenReturn("Subscription successful.");

        // Act
        ResponseEntity<?> response = subscribedController.subscribeStudent(TOKEN, courseId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Subscription successful.", response.getBody());
    }

    @Test
    void subscribeStudent_CourseNotFound() {
        // Arrange
        CourseId courseId = new CourseId();
        courseId.setCourse_id(COURSE_ID);
        when(subscribedService.subscribeStudentToCourse(TOKEN, COURSE_ID))
                .thenThrow(new ResourceNotFoundException("Course not found with ID: " + COURSE_ID));

        // Act
        ResponseEntity<?> response = subscribedController.subscribeStudent(TOKEN, courseId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Course not found with ID: " + COURSE_ID, response.getBody());
    }

    @Test
    void subscribeStudent_InvalidToken() {
        // Arrange
        CourseId courseId = new CourseId();
        courseId.setCourse_id(COURSE_ID);
        when(subscribedService.subscribeStudentToCourse(TOKEN, COURSE_ID))
                .thenThrow(new TokenInvalid("Invalid token"));

        // Act
        ResponseEntity<?> response = subscribedController.subscribeStudent(TOKEN, courseId);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Invalid token", response.getBody());
    }

    @Test
    void subscribeStudent_AlreadySubscribed() {
        // Arrange
        CourseId courseId = new CourseId();
        courseId.setCourse_id(COURSE_ID);
        when(subscribedService.subscribeStudentToCourse(TOKEN, COURSE_ID))
                .thenThrow(new EmailAlreadyExistException("Student is already subscribed to this course."));

        // Act
        ResponseEntity<?> response = subscribedController.subscribeStudent(TOKEN, courseId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Student is already subscribed to this course.", response.getBody());
    }

    @Test
    void unsubscribeStudent_Success() {
        // Arrange
        when(subscribedService.unsubscribeStudentFromCourse(TOKEN, COURSE_ID)).thenReturn("Unsubscription successful.");

        // Act
        ResponseEntity<?> response = subscribedController.unsubscribeStudent(TOKEN, COURSE_ID);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Unsubscription successful.", response.getBody());
    }

    @Test
    void unsubscribeStudent_CourseNotFound() {
        // Arrange
        when(subscribedService.unsubscribeStudentFromCourse(TOKEN, COURSE_ID))
                .thenThrow(new ResourceNotFoundException("Course not found with ID: " + COURSE_ID));

        // Act
        ResponseEntity<?> response = subscribedController.unsubscribeStudent(TOKEN, COURSE_ID);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Course not found with ID: " + COURSE_ID, response.getBody());
    }

    @Test
    void unsubscribeStudent_InvalidToken() {
        // Arrange
        when(subscribedService.unsubscribeStudentFromCourse(TOKEN, COURSE_ID))
                .thenThrow(new TokenInvalid("Invalid token"));

        // Act
        ResponseEntity<?> response = subscribedController.unsubscribeStudent(TOKEN, COURSE_ID);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Invalid token", response.getBody());
    }

    @Test
    void getSubscribedStudents_Success() {
        // Arrange
        List<Student> students = new ArrayList<>();
        students.add(new Student());
        when(subscribedService.getStudentsSubscribedToCourse(TOKEN, COURSE_ID)).thenReturn(students);

        // Act
        ResponseEntity<?> response = subscribedController.getSubscribedStudents(TOKEN, COURSE_ID);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(students, response.getBody());
    }

    @Test
    void getSubscribedStudents_CourseNotFound() {
        // Arrange
        when(subscribedService.getStudentsSubscribedToCourse(TOKEN, COURSE_ID))
                .thenThrow(new ResourceNotFoundException("Course not found with ID: " + COURSE_ID));

        // Act
        ResponseEntity<?> response = subscribedController.getSubscribedStudents(TOKEN, COURSE_ID);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Course not found with ID: " + COURSE_ID, response.getBody());
    }

    @Test
    void getSubscribedStudents_InvalidToken() {
        // Arrange
        when(subscribedService.getStudentsSubscribedToCourse(TOKEN, COURSE_ID))
                .thenThrow(new TokenInvalid("You are not authorized to view the subscribers for this course."));

        // Act
        ResponseEntity<?> response = subscribedController.getSubscribedStudents(TOKEN, COURSE_ID);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("You are not authorized to view the subscribers for this course.", response.getBody());
    }
}