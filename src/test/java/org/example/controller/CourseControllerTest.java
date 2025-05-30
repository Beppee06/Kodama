package org.example.controller;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class CourseControllerTest {/*

    @Mock
    private CourseService courseService;

    @InjectMocks
    private CourseController courseController;

    private SimpleCourse testSimpleCourse;

    @BeforeEach
    void setUp() {
        testSimpleCourse = new SimpleCourse();
        testSimpleCourse.setTeacherToken("valid-token");
        testSimpleCourse.setName("Test Course");
        testSimpleCourse.setYear(2024);
        testSimpleCourse.setSubject("Test Subject");
        testSimpleCourse.setMaxSubscribed(30);
        testSimpleCourse.setClassroom(220);
        testSimpleCourse.setStartDate(LocalDate.now());
        testSimpleCourse.setFinishDate(LocalDate.now().plusMonths(3));
        testSimpleCourse.setType("Test Type");
    }

    @Test
    void createCourse_WithValidData_ShouldReturnOk() {
        when(courseService.createCourse(any(SimpleCourse.class)))
            .thenReturn("Course created successfully");

        ResponseEntity<?> response = courseController.createCourse(testSimpleCourse);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Course created successfully", response.getBody());
    }

    @Test
    void createCourse_WithError_ShouldReturnBadRequest() {
        when(courseService.createCourse(any(SimpleCourse.class)))
            .thenThrow(new IllegalArgumentException("Invalid course data"));

        ResponseEntity<?> response = courseController.createCourse(testSimpleCourse);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("An error occurred"));
    }*/
}