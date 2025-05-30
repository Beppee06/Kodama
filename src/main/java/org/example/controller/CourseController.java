package org.example.controller;


import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.StudentDTO;
import org.example.models.SimpleCourse;
import org.example.models.SimplerCourse;
import org.example.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Indica che questa classe è un controller REST
@RequestMapping("/api/course") // Prefisso per tutti gli endpoint in questa classe
@Tag(name = "Course Controller", description = "API per il controllo del corso") // Raggruppa gli endpoint in Swagger UI
public class CourseController {
    private final CourseService courseService;

    @Autowired // Constructor injection is generally preferred
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
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
    @PostMapping("/post/createCourse")
    public ResponseEntity<?> createCourse(@RequestBody SimplerCourse simplerCourse)  {
        try {
            return ResponseEntity.ok(courseService.createCourse(simplerCourse));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred: " + e.getMessage());
        }
    }

    //in teoria aiuta per il problema di cors
    @CrossOrigin(origins = "http://localhost:80")
    @GetMapping("/get/getCoursesForTeacher")
    public ResponseEntity<?> getCoursesForTeacher(@RequestHeader String token)  {
        try {
            return ResponseEntity.ok(courseService.getCoursesByTeacher(token));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred: " + e.getMessage());
        }
    }

    //in teoria aiuta per il problema di cors
    @CrossOrigin(origins = "http://localhost:80")
    @GetMapping("/get/getCourses/recupero")
    public ResponseEntity<?> getCoursesForRecupero(@RequestHeader String token)  {
        try {
            return ResponseEntity.ok(courseService.getCoursesForRecupero(token));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred: " + e.getMessage());
        }
    }

    //in teoria aiuta per il problema di cors
    @CrossOrigin(origins = "http://localhost:80")
    @GetMapping("/get/getCourses/potenziamento")
    public ResponseEntity<?> getCoursesForPotenziamento(@RequestHeader String token)  {
        try {
            return ResponseEntity.ok(courseService.getCoursesForPotenziamento(token));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred: " + e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:80")
    @PutMapping("/put/updateCourse")
    public ResponseEntity<?> updateCourse(@RequestBody SimpleCourse simpleCourse)  {
        try {
            return ResponseEntity.ok(courseService.updateCourse(simpleCourse));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred: " + e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:80")
    @DeleteMapping("/delete/deleteCourse/{course_id}")
    public ResponseEntity<?> deleteCourse(@RequestHeader String token,@PathVariable Integer course_id)  {
        try {
            return ResponseEntity.ok(courseService.deleteCourse(course_id, token));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred: " + e.getMessage());
        }
    }
}
