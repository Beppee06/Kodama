package org.example.controller;

import org.example.models.PasswordChanger;
import org.example.models.SimpleUser;
import org.example.service.StudentService;
import org.example.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    private final String validEmail = "nome.cognome@aldini.istruzioneer.it";
    private final String validPassword = "Password123!";
    private final String newPassword = "NewPassword123!";
    private final SimpleUser simpleUser = new SimpleUser(validEmail, validPassword);
    private final PasswordChanger passwordChanger = new PasswordChanger(validPassword, newPassword, newPassword);

    @Test
    void registerStudent_WithValidData_ShouldReturnOk() {
        when(studentService.registerStudent(validEmail, validPassword))
            .thenReturn("Registration successful");

        ResponseEntity<?> response = studentController.registerStudent(simpleUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Registration successful", response.getBody());
    }

    @Test
    void registerStudent_WithError_ShouldReturnBadRequest() {
        when(studentService.registerStudent(validEmail, validPassword))
            .thenThrow(new IllegalArgumentException("Invalid data"));

        ResponseEntity<?> response = studentController.registerStudent(simpleUser);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("An error occurred"));
    }

    @Test
    void loginStudent_WithValidCredentials_ShouldReturnOk() {
        when(studentService.loginStudent(validEmail, validPassword))
            .thenReturn("token: test-token");

        ResponseEntity<?> response = studentController.loginStudent(simpleUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("token: test-token", response.getBody());
    }

    @Test
    void loginStudent_WithInvalidCredentials_ShouldReturnNotFound() {
        when(studentService.loginStudent(validEmail, validPassword))
            .thenThrow(new ResourceNotFoundException("Account not found"));

        ResponseEntity<?> response = studentController.loginStudent(simpleUser);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Account not found"));
    }

    @Test
    void changePassword_WithValidData_ShouldReturnOk() {
        String token = "valid-token";
        when(studentService.changePasswordStudent(token, validPassword, newPassword, newPassword))
            .thenReturn("Password successfully changed");

        ResponseEntity<?> response = studentController.changePassword(token, passwordChanger);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password successfully changed", response.getBody());
    }

    @Test
    void changePassword_WithError_ShouldReturnBadRequest() {
        String token = "valid-token";
        when(studentService.changePasswordStudent(anyString(), anyString(), anyString(), anyString()))
            .thenThrow(new IllegalArgumentException("Invalid data"));

        ResponseEntity<?> response = studentController.changePassword(token, passwordChanger);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("An error occurred"));
    }
}