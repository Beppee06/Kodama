package org.example.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.junit.jupiter.api.Assertions.*;

public class ExceptionTests {

    @Test
    public void testEmailAlreadyExistException() {
        String errorMessage = "Email already exists in the system";
        EmailAlreadyExistException exception = new EmailAlreadyExistException(errorMessage);

        // Test message
        assertEquals(errorMessage, exception.getMessage());

        // Test HTTP status code annotation
        ResponseStatus annotation = EmailAlreadyExistException.class.getAnnotation(ResponseStatus.class);
        assertNotNull(annotation);
        assertEquals(HttpStatus.BAD_REQUEST, annotation.value());
    }

    @Test
    public void testEmailMalformedException() {
        String errorMessage = "Email format is invalid";
        EmailMalformedException exception = new EmailMalformedException(errorMessage);

        // Test message
        assertEquals(errorMessage, exception.getMessage());

        // Test HTTP status code annotation
        ResponseStatus annotation = EmailMalformedException.class.getAnnotation(ResponseStatus.class);
        assertNotNull(annotation);
        assertEquals(HttpStatus.BAD_REQUEST, annotation.value());
    }

    @Test
    public void testPasswordDoNotMatch() {
        String errorMessage = "Passwords do not match";
        PasswordDoNotMatch exception = new PasswordDoNotMatch(errorMessage);

        // Test message
        assertEquals(errorMessage, exception.getMessage());

        // Test HTTP status code annotation
        ResponseStatus annotation = PasswordDoNotMatch.class.getAnnotation(ResponseStatus.class);
        assertNotNull(annotation);
        assertEquals(HttpStatus.NOT_ACCEPTABLE, annotation.value());
    }

    @Test
    public void testPasswordMalformedException() {
        String errorMessage = "Password does not meet requirements";
        PasswordMalformedException exception = new PasswordMalformedException(errorMessage);

        // Test message
        assertEquals(errorMessage, exception.getMessage());

        // Test HTTP status code annotation
        ResponseStatus annotation = PasswordMalformedException.class.getAnnotation(ResponseStatus.class);
        assertNotNull(annotation);
        assertEquals(HttpStatus.BAD_REQUEST, annotation.value());
    }

    @Test
    public void testResourceNotFoundException() {
        String errorMessage = "User with ID 123 not found";
        ResourceNotFoundException exception = new ResourceNotFoundException(errorMessage);

        // Test message
        assertEquals(errorMessage, exception.getMessage());

        // Test HTTP status code annotation
        ResponseStatus annotation = ResourceNotFoundException.class.getAnnotation(ResponseStatus.class);
        assertNotNull(annotation);
        assertEquals(HttpStatus.NOT_FOUND, annotation.value());
    }

    @Test
    public void testTokenInvalid() {
        String errorMessage = "Authentication token is invalid or expired";
        TokenInvalid exception = new TokenInvalid(errorMessage);

        // Test message
        assertEquals(errorMessage, exception.getMessage());

        // Test HTTP status code annotation
        ResponseStatus annotation = TokenInvalid.class.getAnnotation(ResponseStatus.class);
        assertNotNull(annotation);
        assertEquals(HttpStatus.FORBIDDEN, annotation.value());
    }

    @Test
    public void testExceptionInheritance() {
        // Test that all exceptions inherit from RuntimeException
        assertTrue(RuntimeException.class.isAssignableFrom(EmailAlreadyExistException.class));
        assertTrue(RuntimeException.class.isAssignableFrom(EmailMalformedException.class));
        assertTrue(RuntimeException.class.isAssignableFrom(PasswordDoNotMatch.class));
        assertTrue(RuntimeException.class.isAssignableFrom(PasswordMalformedException.class));
        assertTrue(RuntimeException.class.isAssignableFrom(ResourceNotFoundException.class));
        assertTrue(RuntimeException.class.isAssignableFrom(TokenInvalid.class));
    }
}