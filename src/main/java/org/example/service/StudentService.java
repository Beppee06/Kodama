package org.example.service; // Assicurati che il package sia corretto

import org.example.exception.*;
import org.example.models.Student;
// import org.example.dto.StudentDTO;
import org.example.repository.StudentRepository;
import org.example.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    public StudentService(StudentRepository studentRepository,
                          JwtUtil jwtUtil) {
        this.studentRepository = studentRepository;
        this.jwtUtil = jwtUtil;
    }

    public boolean checkIfStudentExistsByEmail(String email) {
        return studentRepository.existsByEmail(email);
    }

    public Student findStudentByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

    public static void isValidEmail(String email) {
        // Email tipo nome.cognome@avbo.it
        // Solo lettere (no numeri, spazi o simboli) prima della chiocciola
        String regex = "^[a-zA-Z]+\\.[a-zA-Z]+@aldini\\.istruzioneer\\.it$";
        if(!email.matches(regex))
            throw new EmailMalformedException("Invalid email");
    }

    public static void isValidPassword(String password) {
        // Almeno 8 caratteri, una maiuscola, una minuscola, un numero, un simbolo speciale
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w\\s]).{8,}$";
        if(!password.matches(regex))
            throw new PasswordMalformedException("Invalid password");
    }

    /**
     * Registers a new student, saving the password in PLAIN TEXT.
     * WARNING: EXTREMELY INSECURE - FOR TEMPORARY TESTING ONLY.
     * @param email The student's email.
     * @param plainPassword The student's plain text password.
     * @return The token.
     * @throws IllegalArgumentException if email already exists.
     */
    public String registerStudent(String email, String plainPassword) {
        if (studentRepository.existsByEmail(email)){// && ) {
            throw new EmailAlreadyExistException("Email address already in use: " + email);
        }

        String encodedPassword;

        isValidEmail(email); isValidPassword(plainPassword); // Checks both email and password

        PasswordEncoder localEncoder = new BCryptPasswordEncoder();
        encodedPassword = localEncoder.encode(plainPassword);
        Student student = new Student(email, encodedPassword);
        studentRepository.save(student);

        return "Registration successful";
    }

    /**
     * Logs in a student, dealing with the password in PLAIN TEXT.
     * WARNING: EXTREMELY INSECURE - FOR TEMPORARY TESTING ONLY.
     * @param email The student's email.
     * @param plainPassword The student's plain text password.
     * @return The token if it exists.
     * @throws ResourceNotFoundException if it does not find the account.
     */
    public String loginStudent(String email, String plainPassword) {
        PasswordEncoder localEncoder = new BCryptPasswordEncoder();
        String encodedPassword = localEncoder.encode(plainPassword);

        if(!checkIfStudentExistsByEmail(email))
            throw new ResourceNotFoundException("Email address does not exist");

        Student student = findStudentByEmail(email);

        if (!localEncoder.matches(plainPassword, student.getPassword())) {
            throw new ResourceNotFoundException("Account not found");
        }

        return authenticateAndGenerateToken(student.getEmail(), student.getPassword());
    }

    /**
     * Authenticates a student with email and plain password, generates a JWT upon success.
     * WARNING: Compares passwords in PLAIN TEXT - EXTREMELY INSECURE.
     *
     * @param email The email provided by the user.
     * @param plainPassword The plain text password provided by the user.
     * @return The generated JWT string if authentication is successful.
     * @throws BadCredentialsException If the email is not found or the password does not match.
     */
    public String authenticateAndGenerateToken(String email, String plainPassword) {
        Student student;
        try {
            student = findStudentByEmail(email);
        } catch (ResourceNotFoundException e) {
            throw new BadCredentialsException("Invalid credentials");
        }

        if (!plainPassword.equals(student.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        String subject = student.getEmail();
        Map<String, Object> claims = new HashMap<>();
        String token = jwtUtil.generateToken(subject, claims);
        return "token: " + token;
    }

    /*
        email: tizio.caio@aldini.istruzioneer.it
        pswd: Mela06!?
        pswd: Banana06!?
     */

    public String changePasswordStudent(String token, String oldPassword, String newPassword, String newPasswordConfirm) {
        Student student = getStudentByToken(token); // This can throw if token is invalid

        if(!newPassword.equals(newPasswordConfirm)) // Checks that the two new passwords match
            throw new PasswordDoNotMatch("Passwords do not match");

        PasswordEncoder localEncoder = new BCryptPasswordEncoder();

        if (!localEncoder.matches(oldPassword, student.getPassword())) // Checks that the given password and the one in the database match
            throw new BadCredentialsException("Old password does not match");

        isValidPassword(newPassword);

        student.setPassword(localEncoder.encode(newPassword)); // Encodes the new password

        studentRepository.save(student); // Upon saving with the new password but the same id it does not create a new entity but updates the old one

        return "Password succesfully changed";
    }

    public boolean isTokenValid(String token) { return jwtUtil.validateToken(token); }

    /**
     * Trova e restituisce l'entità Student corrispondente al token JWT fornito.
     * Valida il token ed estrae il subject (assunto essere l'email) per cercare lo studente.
     *
     * @param token La stringa JWT (solitamente senza il prefisso "Bearer ").
     * @return L'entità Student trovata.
     * @throws BadCredentialsException Se il token non è valido, è scaduto o malformato.
     */
    public Student getStudentByToken(String token){
        if(!isTokenValid(token))
            throw new TokenInvalid("token invalid");

        // Estrai il subject (email) dal token. Questo valida anche il token.
        String email = jwtUtil.getSubjectFromToken(token);

        Student student = findStudentByEmail(email);

        if(student == null)
            throw new TokenInvalid("token invalid. you are a teacher");
        return student;
    }
}