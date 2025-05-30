package org.example.controller;

import org.example.exception.EmailAlreadyExistException;
import org.example.exception.ResourceNotFoundException;
import org.example.exception.TokenInvalid;
import org.example.models.CourseId;
import org.example.models.Student;
import org.example.service.SubscribedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/subscribed")
@Tag(name = "Subscription Controller", description = "API per la gestione delle iscrizioni ai corsi")
public class SubscribedController {

    private final SubscribedService subscribedService;

    @Autowired
    public SubscribedController(SubscribedService subscribedService) {
        this.subscribedService = subscribedService;
    }

    // --- Endpoint per l'iscrizione dello studente ---
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription successful",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - Student already subscribed",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - Invalid token, course full, or course finished",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Not Found - Course not found",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })

    @CrossOrigin(origins = "http://localhost:80")
    @PostMapping("/post/subscribeStudent")
    public ResponseEntity<?> subscribeStudent(
            @RequestHeader String token,
            @RequestBody CourseId courseId) {
        try {
            String result = subscribedService.subscribeStudentToCourse(token, courseId.getCourse_id());
            return ResponseEntity.ok(result);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (TokenInvalid e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (EmailAlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An unexpected error occurred during subscription: " + e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:80")
    @DeleteMapping("/delete/unsubscribe/{courseId}")
    public ResponseEntity<?> unsubscribeStudent(
            @RequestHeader String token,
            @PathVariable Integer courseId) {
        try {
            String result = subscribedService.unsubscribeStudentFromCourse(token, courseId);
            return ResponseEntity.ok(result);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (TokenInvalid e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An unexpected error occurred during unsubscription: " + e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:80")
    @GetMapping("/get/subscribedList/{courseId}")
    public ResponseEntity<?> getSubscribedStudents(
            @RequestHeader String token,
            @PathVariable Integer courseId) {
        try {
            List<Student> students = subscribedService.getStudentsSubscribedToCourse(token, courseId);
            return ResponseEntity.ok(students);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (TokenInvalid e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}