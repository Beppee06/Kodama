package org.example.controller;

import org.example.models.PasswordChanger;
import org.example.models.SimpleUser;
import org.example.service.StudentService;
import org.example.dto.StudentDTO;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.*;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

@RestController // Indica che questa classe è un controller REST
@RequestMapping("/api/student") // Prefisso per tutti gli endpoint in questa classe
@Tag(name = "Student Controller", description = "API per il controllo dello studente") // Raggruppa gli endpoint in Swagger UI
public class StudentController {

    private final StudentService studentService;

    @Autowired // Constructor injection is generally preferred
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved orders",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = List.class, subTypes = {StudentDTO.class}))),
        @ApiResponse(responseCode = "400", description = "Bad Request - Invalid user ID format", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated", content = @Content),
        @ApiResponse(responseCode = "403", description = "Forbidden - User authenticated but lacks permission (if more specific auth needed)", content = @Content),
        @ApiResponse(responseCode = "404", description = "Not Found - The resource does not exist", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })

    @CrossOrigin(origins = "http://localhost:80")
    @PostMapping("/post/register")
    public ResponseEntity<?> registerStudent(@RequestBody SimpleUser simpleUser) {
        try {
            return ResponseEntity.ok(studentService.registerStudent(simpleUser.getEmail(),simpleUser.getPassword()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred: " + e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:80")
    @PostMapping("/post/login")
    public ResponseEntity<?> loginStudent(@RequestBody SimpleUser simpleUser) {
        try {
            return ResponseEntity.ok(studentService.loginStudent(simpleUser.getEmail(),simpleUser.getPassword()));
        } catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:80")
    @PutMapping("/put/changePassword")
    public ResponseEntity<?> changePassword(@RequestHeader String token, @RequestBody PasswordChanger passwordChanger) {
        try {
            return ResponseEntity.ok(studentService.changePasswordStudent(token, passwordChanger.getOldPassword(), passwordChanger.getNewPassword(), passwordChanger.getNewPasswordConfirm()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred: " + e.getMessage());
        }
    }
}