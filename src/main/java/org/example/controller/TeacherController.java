package org.example.controller;

import org.example.models.PasswordChanger;
import org.example.models.SimpleUser;
import org.example.service.TeacherService;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.TeacherDTO;
import org.example.exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.*;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

//every status code I'll need:
/*

@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Successfully retrieved orders",
         content = @Content(mediaType = "application/json",
         schema = @Schema(implementation = List.class, subTypes = {OrderSummaryDTO.class}))),
    @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated", content = @Content),
    @ApiResponse(responseCode = "403", description = "Forbidden - User authenticated but lacks permission (if more specific auth needed)", content = @Content),
    @ApiResponse(responseCode = "400", description = "Bad Request - Invalid user ID format", content = @Content),
    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
})

 */


//add this to one of the configuration classes
/*
@SpringBootApplication
@EnableMethodSecurity
 */


@RestController // Indica che questa classe è un controller REST
@RequestMapping("/api/teacher") // Prefisso per tutti gli endpoint in questa classe
@Tag(name = "Teacher Controller", description = "API per il controllo dello docente") // Raggruppa gli endpoint in Swagger UI
public class TeacherController {

    private final TeacherService teacherService;

    @Autowired // Constructor injection is generally preferred
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved orders",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class, subTypes = {TeacherDTO.class}))),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid user ID format", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User authenticated but lacks permission (if more specific auth needed)", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found - The resource does not exist", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })

    @CrossOrigin(origins = "http://localhost:80")
    @PostMapping("/post/register")
    public ResponseEntity<?> registerTeacher(@RequestBody SimpleUser user) {
        try {
            return ResponseEntity.ok(teacherService.registerTeacher(user.getEmail(), user.getPassword()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred: " + e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:80")
    @PostMapping("/post/login")
    public ResponseEntity<?> loginTeacher(@RequestBody SimpleUser user) {
        try {
            return ResponseEntity.ok(teacherService.loginTeacher(user.getEmail(), user.getPassword()));
        } catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:80")
    @PutMapping("/put/changePassword")
    public ResponseEntity<?> changePassword(@RequestHeader String token, @RequestBody PasswordChanger pswdChanger) {
        try {
            return ResponseEntity.ok(teacherService.changePasswordTeacher(token, pswdChanger.getOldPassword(), pswdChanger.getNewPassword(), pswdChanger.getNewPasswordConfirm()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred: " + e.getMessage());
        }
    }
}