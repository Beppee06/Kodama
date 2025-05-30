package org.example.service;

import org.example.exception.EmailMalformedException;
import org.example.exception.PasswordDoNotMatch;
import org.example.exception.PasswordMalformedException;
import org.example.exception.ResourceNotFoundException;
import org.example.exception.TokenInvalid;
import org.example.models.Teacher;
import org.example.repository.TeacherRepository;
import org.example.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private TeacherService teacherService;

    private Teacher testTeacher;
    private String validEmail;
    private String validPassword;
    private String validToken;

    @BeforeEach
    public void setUp() {
        validEmail = "nome.cognome@avbo.it";
        validPassword = "Password123!";
        validToken = "valid.jwt.token";

        testTeacher = new Teacher(validEmail, new BCryptPasswordEncoder().encode(validPassword));
        testTeacher.setId(1);
    }

    @Test
    public void testCheckIfTeacherExistsByEmail() {
        when(teacherRepository.existsByEmail(validEmail)).thenReturn(true);
        when(teacherRepository.existsByEmail("nonexistent@avbo.it")).thenReturn(false);

        assertTrue(teacherService.checkIfTeacherExistsByEmail(validEmail));
        assertFalse(teacherService.checkIfTeacherExistsByEmail("nonexistent@avbo.it"));
    }

    @Test
    public void testFindTeacherByEmail() {
        when(teacherRepository.findByEmail(validEmail)).thenReturn(testTeacher);
        when(teacherRepository.findByEmail("nonexistent@avbo.it")).thenReturn(null);

        assertEquals(testTeacher, teacherService.findTeacherByEmail(validEmail));
        assertNull(teacherService.findTeacherByEmail("nonexistent@avbo.it"));
    }

    @Test
    public void testIsValidEmail_Valid() {
        // Non dovrebbe lanciare eccezioni
        TeacherService.isValidEmail(validEmail);
    }

    @Test
    public void testIsValidEmail_Invalid() {
        // Dovrebbe lanciare EmailMalformedException
        assertThrows(EmailMalformedException.class, () -> {
            TeacherService.isValidEmail("invalid@gmail.com");
        });
        assertThrows(EmailMalformedException.class, () -> {
            TeacherService.isValidEmail("nome.cognome@avbo.com");
        });
    }

    @Test
    public void testIsValidPassword_Valid() {
        // Non dovrebbe lanciare eccezioni
        TeacherService.isValidPassword(validPassword);
    }

    @Test
    public void testIsValidPassword_Invalid() {
        // Dovrebbe lanciare PasswordMalformedException
        assertThrows(PasswordMalformedException.class, () -> {
            TeacherService.isValidPassword("short1!");
        });
    }

    @Test
    public void testRegisterTeacher_Success() {
        when(teacherRepository.existsByEmail(validEmail)).thenReturn(false);
        when(teacherRepository.save(any(Teacher.class))).thenReturn(testTeacher);

        String result = teacherService.registerTeacher(validEmail, validPassword);
        assertEquals("Registration successful", result);

        verify(teacherRepository, times(1)).existsByEmail(validEmail);
        verify(teacherRepository, times(1)).save(any(Teacher.class));
    }

    @Test
    public void testRegisterTeacher_EmailAlreadyExists() {
        when(teacherRepository.existsByEmail(validEmail)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            teacherService.registerTeacher(validEmail, validPassword);
        });

        verify(teacherRepository, times(1)).existsByEmail(validEmail);
        verify(teacherRepository, never()).save(any(Teacher.class));
    }

    @Test
    public void testLoginTeacher_Success() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(validPassword);
        Teacher teacher = new Teacher(validEmail, encodedPassword);

        when(teacherRepository.existsByEmail(validEmail)).thenReturn(true);
        when(teacherRepository.findByEmail(validEmail)).thenReturn(teacher);
        when(jwtUtil.generateToken(anyString(), anyMap())).thenReturn("jwt.token.string");

        String result = teacherService.loginTeacher(validEmail, validPassword);

        assertTrue(result.contains("token:"));
        verify(teacherRepository, times(1)).existsByEmail(validEmail);
        verify(teacherRepository, times(1)).findByEmail(validEmail);
    }

    @Test
    public void testLoginTeacher_EmailNotFound() {
        when(teacherRepository.existsByEmail(validEmail)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            teacherService.loginTeacher(validEmail, validPassword);
        });

        verify(teacherRepository, times(1)).existsByEmail(validEmail);
        verify(teacherRepository, never()).findByEmail(anyString());
    }

    @Test
    public void testAuthenticateAndGenerateToken_Success() {
        String encodedPassword = new BCryptPasswordEncoder().encode(validPassword);
        Teacher teacher = new Teacher(validEmail, encodedPassword);

        when(teacherRepository.findByEmail(validEmail)).thenReturn(teacher);
        when(jwtUtil.generateToken(anyString(), anyMap())).thenReturn("jwt.token.string");

        String result = teacherService.authenticateAndGenerateToken(validEmail, encodedPassword);

        assertTrue(result.contains("token:"));
        verify(teacherRepository, times(1)).findByEmail(validEmail);
        verify(jwtUtil, times(1)).generateToken(anyString(), anyMap());
    }

    @Test
    public void testAuthenticateAndGenerateToken_EmailNotFound() {
        when(teacherRepository.findByEmail(validEmail)).thenReturn(null);

        assertThrows(BadCredentialsException.class, () -> {
            teacherService.authenticateAndGenerateToken(validEmail, validPassword);
        });

        verify(teacherRepository, times(1)).findByEmail(validEmail);
        verify(jwtUtil, never()).generateToken(anyString(), anyMap());
    }

    @Test
    public void testAuthenticateAndGenerateToken_PasswordMismatch() {
        String encodedPassword = new BCryptPasswordEncoder().encode(validPassword);
        Teacher teacher = new Teacher(validEmail, encodedPassword);

        when(teacherRepository.findByEmail(validEmail)).thenReturn(teacher);

        assertThrows(BadCredentialsException.class, () -> {
            teacherService.authenticateAndGenerateToken(validEmail, "wrongPassword");
        });

        verify(teacherRepository, times(1)).findByEmail(validEmail);
        verify(jwtUtil, never()).generateToken(anyString(), anyMap());
    }

    @Test
    public void testCheckEncodedPassword_Success() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(validPassword);
        Teacher teacher = new Teacher(validEmail, encodedPassword);

        // Non dovrebbe lanciare eccezioni
        teacherService.checkEncodedPassword(encoder, validPassword, teacher);
    }

    @Test
    public void testCheckEncodedPassword_Failure() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(validPassword);
        Teacher teacher = new Teacher(validEmail, encodedPassword);

        assertThrows(BadCredentialsException.class, () -> {
            teacherService.checkEncodedPassword(encoder, "wrongPassword", teacher);
        });
    }

    @Test
    public void testChangePasswordTeacher_Success() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(validPassword);
        Teacher teacher = new Teacher(validEmail, encodedPassword);

        String newPassword = "NewPassword123!";
        String confirmPassword = "NewPassword123!";

        when(jwtUtil.validateToken(validToken)).thenReturn(true);
        when(jwtUtil.getSubjectFromToken(validToken)).thenReturn(validEmail);
        when(teacherRepository.findByEmail(validEmail)).thenReturn(teacher);
        when(teacherRepository.save(any(Teacher.class))).thenReturn(teacher);

        String result = teacherService.changePasswordTeacher(validToken, validPassword, newPassword, confirmPassword);

        assertEquals("Password successfully changed", result);
        verify(jwtUtil, times(1)).validateToken(validToken);
        verify(jwtUtil, times(1)).getSubjectFromToken(validToken);
        verify(teacherRepository, times(1)).findByEmail(validEmail);
        verify(teacherRepository, times(1)).save(any(Teacher.class));
    }

    @Test
    public void testChangePasswordTeacher_InvalidToken() {
        when(jwtUtil.validateToken(validToken)).thenReturn(false);

        assertThrows(TokenInvalid.class, () -> {
            teacherService.changePasswordTeacher(validToken, validPassword, "NewPassword123!", "NewPassword123!");
        });

        verify(jwtUtil, times(1)).validateToken(validToken);
        verify(jwtUtil, never()).getSubjectFromToken(anyString());
        verify(teacherRepository, never()).findByEmail(anyString());
    }

    @Test
    public void testChangePasswordTeacher_PasswordsDoNotMatch() {
        when(jwtUtil.validateToken(validToken)).thenReturn(true);
        when(jwtUtil.getSubjectFromToken(validToken)).thenReturn(validEmail);
        when(teacherRepository.findByEmail(validEmail)).thenReturn(testTeacher);

        assertThrows(PasswordDoNotMatch.class, () -> {
            teacherService.changePasswordTeacher(validToken, validPassword, "NewPassword123!", "DifferentPassword123!");
        });
    }

    @Test
    public void testGetTeacherByToken_Success() {
        when(jwtUtil.validateToken(validToken)).thenReturn(true);
        when(jwtUtil.getSubjectFromToken(validToken)).thenReturn(validEmail);
        when(teacherRepository.findByEmail(validEmail)).thenReturn(testTeacher);

        Teacher result = teacherService.getTeacherByToken(validToken);

        assertEquals(testTeacher, result);
        verify(jwtUtil, times(1)).validateToken(validToken);
        verify(jwtUtil, times(1)).getSubjectFromToken(validToken);
        verify(teacherRepository, times(1)).findByEmail(validEmail);
    }

    @Test
    public void testGetTeacherByToken_InvalidToken() {
        when(jwtUtil.validateToken(validToken)).thenReturn(false);

        assertThrows(TokenInvalid.class, () -> {
            teacherService.getTeacherByToken(validToken);
        });

        verify(jwtUtil, times(1)).validateToken(validToken);
        verify(jwtUtil, never()).getSubjectFromToken(anyString());
        verify(teacherRepository, never()).findByEmail(anyString());
    }
}