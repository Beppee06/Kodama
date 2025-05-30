/*
test generico per course integration
package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.dto.CourseDTO;
import org.example.models.SimpleCourse;
import org.example.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CourseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    private ObjectMapper objectMapper;
    private SimpleCourse simpleCourse;
    private CourseDTO courseDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        simpleCourse = new SimpleCourse();
        simpleCourse.setTeacherToken("test-token");
        simpleCourse.setName("Test Course");
        simpleCourse.setYear(2024);
        simpleCourse.setSubject("Test Subject");
        simpleCourse.setMaxSubscribed(30);
        simpleCourse.setClassroom("Test Room");
        simpleCourse.setStartDate(LocalDate.now());
        simpleCourse.setFinishDate(LocalDate.now().plusMonths(3));
        simpleCourse.setType("Test Type");

        courseDTO = new CourseDTO(
                1,
                1,
                "Test Course",
                2024,
                "Test Subject",
                30,
                "Test Room",
                LocalDate.now(),
                LocalDate.now().plusMonths(3)
        );
    }

    @Test
    @WithMockUser
    void createCourse_Success() throws Exception {
        when(courseService.createCourse(any(SimpleCourse.class))).thenReturn(courseDTO);

        mockMvc.perform(post("/api/course/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(simpleCourse)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseId").value(courseDTO.getCourseId()))
                .andExpect(jsonPath("$.name").value(courseDTO.getName()));
    }

    @Test
    @WithMockUser
    void getCourse_Success() throws Exception {
        when(courseService.getCourseById(1)).thenReturn(courseDTO);

        mockMvc.perform(get("/api/course/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseId").value(courseDTO.getCourseId()))
                .andExpect(jsonPath("$.name").value(courseDTO.getName()));
    }

    @Test
    @WithMockUser
    void getAllCourses_Success() throws Exception {
        when(courseService.getAllCourses()).thenReturn(Arrays.asList(courseDTO));

        mockMvc.perform(get("/api/course/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courseId").value(courseDTO.getCourseId()))
                .andExpect(jsonPath("$[0].name").value(courseDTO.getName()));
    }

    @Test
    @WithMockUser
    void updateCourse_Success() throws Exception {
        when(courseService.updateCourse(eq(1), any(SimpleCourse.class))).thenReturn(courseDTO);

        mockMvc.perform(put("/api/course/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(simpleCourse)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseId").value(courseDTO.getCourseId()))
                .andExpect(jsonPath("$.name").value(courseDTO.getName()));
    }

    @Test
    @WithMockUser
    void deleteCourse_Success() throws Exception {
        mockMvc.perform(delete("/api/course/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Course deleted successfully"));
    }
}

 */