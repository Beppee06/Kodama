package org.example.service; // Assicurati che il package sia corretto

import jakarta.transaction.Transactional;
import org.example.exception.*;
import org.example.models.Student;
import org.example.models.Teacher;
// import org.example.dto.TeacherDTO;
import org.example.repository.TeacherRepository;
import org.example.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@Service
public class TeacherService {

    private static final Logger log = LoggerFactory.getLogger(TeacherService.class);

    private final TeacherRepository teacherRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    public TeacherService(TeacherRepository teacherRepository,
                          JwtUtil jwtUtil) {
        this.teacherRepository = teacherRepository;
        this.jwtUtil = jwtUtil;
    }

    // ... checkIfTeacherExistsByEmail è ok ...
    public boolean checkIfTeacherExistsByEmail(String email) {
        return teacherRepository.existsByEmail(email);
    }

    // ... findTeacherByEmail è ok ...
    public Teacher findTeacherByEmail(String email) {
        return teacherRepository.findByEmail(email);
    }

    public static void isValidEmail(String email) {
        // Email tipo nome.cognome@avbo.it
        // Solo lettere (no numeri, spazi o simboli) prima della chiocciola
        String regex = "^[a-zA-Z]+\\.[a-zA-Z]+@avbo\\.it$";
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
     * Registers a new Teacher, saving the password in PLAIN TEXT.
     * WARNING: EXTREMELY INSECURE - FOR TEMPORARY TESTING ONLY.
     * @param email The Teacher's email.
     * @param plainPassword The Teacher's plain text password.
     * @return The token.
     * @throws IllegalArgumentException if email already exists.
     */
    public String registerTeacher(String email, String plainPassword) {
        if (teacherRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email address already in use: " + email);
        }

        String encodedPassword;

        isValidEmail(email); isValidPassword(plainPassword); // Checks both email and password

        PasswordEncoder localEncoder = new BCryptPasswordEncoder();
        encodedPassword = localEncoder.encode(plainPassword);
        Teacher teacher = new Teacher(email, encodedPassword);
        teacherRepository.save(teacher);

        return "Registration successful";
    }

    /**
     * Logs in a Teacher, dealing with the password in PLAIN TEXT.
     * WARNING: EXTREMELY INSECURE - FOR TEMPORARY TESTING ONLY.
     * @param email The Teacher's email.
     * @param plainPassword The Teacher's plain text password.
     * @return The token if it exists.
     * @throws ResourceNotFoundException if it does not find the account.
     */
    public String loginTeacher(String email, String plainPassword) {
        PasswordEncoder localEncoder = new BCryptPasswordEncoder();
        String encodedPassword = localEncoder.encode(plainPassword);

        if(!checkIfTeacherExistsByEmail(email))
            throw new ResourceNotFoundException("Email address does not exist");

        Teacher teacher = findTeacherByEmail(email);

        if (!localEncoder.matches(plainPassword, teacher.getPassword())) {
            throw new ResourceNotFoundException("Account not found: ");
        }

        //return "esfvv";
        return authenticateAndGenerateToken(teacher.getEmail(), teacher.getPassword());

    }


    /**
     * Authenticates a Teacher with email and plain password, generates a JWT upon success.
     * WARNING: Compares passwords in PLAIN TEXT - EXTREMELY INSECURE.
     *
     * @param email The email provided by the user.
     * @param encodedPassword The plain text password provided by the user.
     * @return The generated JWT string if authentication is successful.
     * @throws BadCredentialsException If the email is not found or the password does not match.
     */
    @Transactional
    public String authenticateAndGenerateToken(String email, String encodedPassword) {

        Teacher teacher;
        try {
            teacher =  findTeacherByEmail(email);
        } catch (ResourceNotFoundException e) {
            throw new BadCredentialsException("Invalid credentials");
        }

        if (!encodedPassword.equals(teacher.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        String subject = teacher.getEmail();
        Map<String, Object> claims = new HashMap<>();
        String token = jwtUtil.generateToken(subject, claims);
        return "token: " + token;
    }

    public void checkEncodedPassword(PasswordEncoder localEncoder, String oldPassword, Teacher teacher) {
        if (!localEncoder.matches(oldPassword, teacher.getPassword()))
            throw new BadCredentialsException("Old password does not match");
    }

    /*
        email: tizio.caio@avbo.it
        pswd: Mela06!?
        pswd: Banana06!?
     */

    public String changePasswordTeacher(String token, String oldPassword, String newPassword, String newPasswordConfirm) {
        Teacher teacher = getTeacherByToken(token); // Checks can throw an error if the token is invalid

        if(!newPassword.equals(newPasswordConfirm))  // Checks that the two new passwords have the same content
            throw new PasswordDoNotMatch("Passwords do not match");

        PasswordEncoder localEncoder = new BCryptPasswordEncoder();

        checkEncodedPassword(localEncoder, oldPassword, teacher);

        isValidPassword(newPassword); // Checks that the password is up to standard

        teacher.setPassword(localEncoder.encode(newPassword));

        teacherRepository.save(teacher);

        return "Password successfully changed";
    }

    public boolean isTokenValid(String token) { return jwtUtil.validateToken(token); }

    /**
     * Trova e restituisce l'entità Teacher corrispondente al token JWT fornito.
     * Valida il token ed estrae il subject (assunto essere l'email) per cercare il docente.
     *
     * @param token La stringa JWT (solitamente senza il prefisso "Bearer ").
     * @return L'entità Teacher trovata.
     * @throws BadCredentialsException Se il token non è valido, è scaduto o malformato.
     */
    public Teacher getTeacherByToken(String token){
        if(!isTokenValid(token))
            throw new TokenInvalid("token invalid");

        // Estrai il subject (email) dal token. Questo valida anche il token.
        String email = jwtUtil.getSubjectFromToken(token);

        Teacher teacher = findTeacherByEmail(email);

        if(teacher == null)
            throw new TokenInvalid("token invalid. you are a student");
        return teacher;
    }
}