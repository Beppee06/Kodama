package org.example.service;

import org.example.exception.EmailAlreadyExistException;
import org.example.exception.EmailMalformedException;
import org.example.exception.PasswordDoNotMatch;
import org.example.exception.PasswordMalformedException;
import org.example.exception.ResourceNotFoundException;
import org.example.exception.TokenInvalid;
import org.example.models.Student;
import org.example.repository.StudentRepository;
import org.example.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private StudentService studentService;

    private Student testStudent;
    private String validEmail;
    private String validPassword;
    private String validToken;

    @BeforeEach
    public void setUp() {
        validEmail = "nome.cognome@avbo.it";
        validPassword = "Password123!";
        validToken = "valid.jwt.token";

        testStudent = new Student(validEmail, new BCryptPasswordEncoder().encode(validPassword));
        testStudent.setId(1);
    }

    @Test
    public void testCheckIfStudentExistsByEmail() {
        when(studentRepository.existsByEmail(validEmail)).thenReturn(true);
        when(studentRepository.existsByEmail("nonexistent@avbo.it")).thenReturn(false);

        assertTrue(studentService.checkIfStudentExistsByEmail(validEmail));
        assertFalse(studentService.checkIfStudentExistsByEmail("nonexistent@avbo.it"));
    }

    @Test
    public void testFindStudentByEmail() {
        when(studentRepository.findByEmail(validEmail)).thenReturn(testStudent);
        when(studentRepository.findByEmail("nonexistent@avbo.it")).thenReturn(null);

        assertEquals(testStudent, studentService.findStudentByEmail(validEmail));
        assertNull(studentService.findStudentByEmail("nonexistent@avbo.it"));
    }

    @Test
    public void testIsValidEmail_Valid() {
        // Non dovrebbe lanciare eccezioni
        StudentService.isValidEmail(validEmail);
    }

    @Test
    public void testIsValidEmail_Invalid() {
        // Dovrebbe lanciare EmailMalformedException
        assertThrows(EmailMalformedException.class, () -> {
            StudentService.isValidEmail("invalid@gmail.com");
        });
        assertThrows(EmailMalformedException.class, () -> {
            StudentService.isValidEmail("nome.cognome@avbo.com");
        });
        assertThrows(EmailMalformedException.class, () -> {
            StudentService.isValidEmail("nome@avbo.it");
        });
        assertThrows(EmailMalformedException.class, () -> {
            StudentService.isValidEmail("nome.123@avbo.it");
        });
    }

    @Test
    public void testIsValidPassword_Valid() {
        // Non dovrebbe lanciare eccezioni
        StudentService.isValidPassword(validPassword);
        StudentService.isValidPassword("Abcde123!");
        StudentService.isValidPassword("Password@123");
    }

    @Test
    public void testIsValidPassword_Invalid() {
        // Dovrebbe lanciare PasswordMalformedException
        assertThrows(PasswordMalformedException.class, () -> {
            StudentService.isValidPassword("short1!");
        });
        assertThrows(PasswordMalformedException.class, () -> {
            StudentService.isValidPassword("nouppercase123!");
        });
        assertThrows(PasswordMalformedException.class, () -> {
            StudentService.isValidPassword("NOLOWERCASE123!");
        });
        assertThrows(PasswordMalformedException.class, () -> {
            StudentService.isValidPassword("NoDigits!");
        });
        assertThrows(PasswordMalformedException.class, () -> {
            StudentService.isValidPassword("NoSpecialChars123");
        });
    }

    @Test
    public void testRegisterStudent_Success() {
        when(studentRepository.existsByEmail(validEmail)).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        String result = studentService.registerStudent(validEmail, validPassword);
        assertEquals("Registration successful", result);

        verify(studentRepository, times(1)).existsByEmail(validEmail);
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    public void testRegisterStudent_EmailAlreadyExists() {
        when(studentRepository.existsByEmail(validEmail)).thenReturn(true);

        assertThrows(EmailAlreadyExistException.class, () -> {
            studentService.registerStudent(validEmail, validPassword);
        });

        verify(studentRepository, times(1)).existsByEmail(validEmail);
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    public void testRegisterStudent_InvalidEmail() {
        when(studentRepository.existsByEmail(anyString())).thenReturn(false);

        assertThrows(EmailMalformedException.class, () -> {
            studentService.registerStudent("invalid@gmail.com", validPassword);
        });

        verify(studentRepository, times(1)).existsByEmail(anyString());
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    public void testRegisterStudent_InvalidPassword() {
        when(studentRepository.existsByEmail(validEmail)).thenReturn(false);

        assertThrows(PasswordMalformedException.class, () -> {
            studentService.registerStudent(validEmail, "invalid");
        });

        verify(studentRepository, times(1)).existsByEmail(validEmail);
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    public void testLoginStudent_Success() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(validPassword);
        Student student = new Student(validEmail, encodedPassword);

        when(studentRepository.existsByEmail(validEmail)).thenReturn(true);
        when(studentRepository.findByEmail(validEmail)).thenReturn(student);
        when(jwtUtil.generateToken(anyString(), anyMap())).thenReturn("jwt.token.string");

        String result = studentService.loginStudent(validEmail, validPassword);

        assertTrue(result.contains("token:"));
        verify(studentRepository, times(1)).existsByEmail(validEmail);
        verify(studentRepository, times(1)).findByEmail(validEmail);
    }

    @Test
    public void testLoginStudent_EmailNotFound() {
        when(studentRepository.existsByEmail(validEmail)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            studentService.loginStudent(validEmail, validPassword);
        });

        verify(studentRepository, times(1)).existsByEmail(validEmail);
        verify(studentRepository, never()).findByEmail(anyString());
    }

    @Test
    public void testLoginStudent_PasswordMismatch() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode("differentPassword");
        Student student = new Student(validEmail, encodedPassword);

        when(studentRepository.existsByEmail(validEmail)).thenReturn(true);
        when(studentRepository.findByEmail(validEmail)).thenReturn(student);

        assertThrows(ResourceNotFoundException.class, () -> {
            studentService.loginStudent(validEmail, validPassword);
        });

        verify(studentRepository, times(1)).existsByEmail(validEmail);
        verify(studentRepository, times(1)).findByEmail(validEmail);
    }

    @Test
    public void testChangePasswordStudent_Success() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(validPassword);
        Student student = new Student(validEmail, encodedPassword);

        String newPassword = "NewPassword123!";
        String confirmPassword = "NewPassword123!";

        when(jwtUtil.validateToken(validToken)).thenReturn(true);
        when(jwtUtil.getSubjectFromToken(validToken)).thenReturn(validEmail);
        when(studentRepository.findByEmail(validEmail)).thenReturn(student);
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        String result = studentService.changePasswordStudent(validToken, validPassword, newPassword, confirmPassword);

        assertEquals("Password succesfully changed", result);
        verify(jwtUtil, times(1)).validateToken(validToken);
        verify(jwtUtil, times(1)).getSubjectFromToken(validToken);
        verify(studentRepository, times(1)).findByEmail(validEmail);
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    public void testChangePasswordStudent_InvalidToken() {
        when(jwtUtil.validateToken(validToken)).thenReturn(false);

        assertThrows(TokenInvalid.class, () -> {
            studentService.changePasswordStudent(validToken, validPassword, "NewPassword123!", "NewPassword123!");
        });

        verify(jwtUtil, times(1)).validateToken(validToken);
        verify(jwtUtil, never()).getSubjectFromToken(anyString());
        verify(studentRepository, never()).findByEmail(anyString());
    }

    @Test
    public void testChangePasswordStudent_PasswordsDoNotMatch() {
        when(jwtUtil.validateToken(validToken)).thenReturn(true);
        when(jwtUtil.getSubjectFromToken(validToken)).thenReturn(validEmail);
        when(studentRepository.findByEmail(validEmail)).thenReturn(testStudent);

        assertThrows(PasswordDoNotMatch.class, () -> {
            studentService.changePasswordStudent(validToken, validPassword, "NewPassword123!", "DifferentPassword123!");
        });
    }

    @Test
    public void testChangePasswordStudent_OldPasswordIncorrect() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode("actualPassword");
        Student student = new Student(validEmail, encodedPassword);

        when(jwtUtil.validateToken(validToken)).thenReturn(true);
        when(jwtUtil.getSubjectFromToken(validToken)).thenReturn(validEmail);
        when(studentRepository.findByEmail(validEmail)).thenReturn(student);

        assertThrows(BadCredentialsException.class, () -> {
            studentService.changePasswordStudent(validToken, "wrongPassword", "NewPassword123!", "NewPassword123!");
        });
    }

    @Test
    public void testChangePasswordStudent_NewPasswordInvalid() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(validPassword);
        Student student = new Student(validEmail, encodedPassword);

        when(jwtUtil.validateToken(validToken)).thenReturn(true);
        when(jwtUtil.getSubjectFromToken(validToken)).thenReturn(validEmail);
        when(studentRepository.findByEmail(validEmail)).thenReturn(student);

        assertThrows(PasswordMalformedException.class, () -> {
            studentService.changePasswordStudent(validToken, validPassword, "weak", "weak");
        });
    }

    @Test
    public void testGetStudentByToken_Success() {
        when(jwtUtil.validateToken(validToken)).thenReturn(true);
        when(jwtUtil.getSubjectFromToken(validToken)).thenReturn(validEmail);
        when(studentRepository.findByEmail(validEmail)).thenReturn(testStudent);

        Student result = studentService.getStudentByToken(validToken);

        assertEquals(testStudent, result);
        verify(jwtUtil, times(1)).validateToken(validToken);
        verify(jwtUtil, times(1)).getSubjectFromToken(validToken);
        verify(studentRepository, times(1)).findByEmail(validEmail);
    }

    @Test
    public void testGetStudentByToken_InvalidToken() {
        when(jwtUtil.validateToken(validToken)).thenReturn(false);

        assertThrows(TokenInvalid.class, () -> {
            studentService.getStudentByToken(validToken);
        });

        verify(jwtUtil, times(1)).validateToken(validToken);
        verify(jwtUtil, never()).getSubjectFromToken(anyString());
        verify(studentRepository, never()).findByEmail(anyString());
    }
}