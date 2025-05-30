package org.example.controller;

import org.example.models.PasswordChanger;
import org.example.models.SimpleUser;
import org.example.service.TeacherService;
import org.example.exception.ResourceNotFoundException;
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
public class TeacherControllerTest {

    @Mock
    private TeacherService teacherService;

    @InjectMocks
    private TeacherController teacherController;

    private final String validEmail = "nome.cognome@avbo.it";
    private final String validPassword = "Password123!";
    private final String newPassword = "NewPassword123!";
    private final SimpleUser simpleUser = new SimpleUser(validEmail, validPassword);
    private final PasswordChanger passwordChanger = new PasswordChanger(validPassword, newPassword, newPassword);

    @Test
    void registerTeacher_WithValidData_ShouldReturnOk() {
        when(teacherService.registerTeacher(validEmail, validPassword))
            .thenReturn("Registration successful");

        ResponseEntity<?> response = teacherController.registerTeacher(simpleUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Registration successful", response.getBody());
    }

    @Test
    void registerTeacher_WithError_ShouldReturnBadRequest() {
        when(teacherService.registerTeacher(validEmail, validPassword))
            .thenThrow(new IllegalArgumentException("Invalid data"));

        ResponseEntity<?> response = teacherController.registerTeacher(simpleUser);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("An error occurred"));
    }

    @Test
    void loginTeacher_WithValidCredentials_ShouldReturnOk() {
        when(teacherService.loginTeacher(validEmail, validPassword))
            .thenReturn("token: test-token");

        ResponseEntity<?> response = teacherController.loginTeacher(simpleUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("token: test-token", response.getBody());
    }

    @Test
    void loginTeacher_WithInvalidCredentials_ShouldReturnNotFound() {
        when(teacherService.loginTeacher(validEmail, validPassword))
            .thenThrow(new ResourceNotFoundException("Account not found"));

        ResponseEntity<?> response = teacherController.loginTeacher(simpleUser);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Account not found"));
    }

    @Test
    void changePassword_WithValidData_ShouldReturnOk() {
        String token = "valid-token";
        when(teacherService.changePasswordTeacher(token, validPassword, newPassword, newPassword))
            .thenReturn("Password successfully changed");

        ResponseEntity<?> response = teacherController.changePassword(token, passwordChanger);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password successfully changed", response.getBody());
    }

    @Test
    void changePassword_WithError_ShouldReturnBadRequest() {
        String token = "valid-token";
        when(teacherService.changePasswordTeacher(anyString(), anyString(), anyString(), anyString()))
            .thenThrow(new IllegalArgumentException("Invalid data"));

        ResponseEntity<?> response = teacherController.changePassword(token, passwordChanger);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("An error occurred"));
    }
}