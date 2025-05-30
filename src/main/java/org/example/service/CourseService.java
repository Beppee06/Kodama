package org.example.service;

import org.example.exception.ResourceNotFoundException;
import org.example.models.*;
import org.example.repository.CourseRepository;
import org.example.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final TeacherService teacherService;
    private final JwtUtil jwtUtil;
    private final StudentService studentService;

    @Autowired
    public CourseService(CourseRepository courseRepository, TeacherService teacherService, JwtUtil jwtUtil, StudentService studentService) {
        this.courseRepository = courseRepository;
        this.teacherService = teacherService;
        this.jwtUtil = jwtUtil;
        this.studentService = studentService;
    }

    public String createCourse(SimplerCourse simplerCourse) {
        Course course = createCourseFromSimplerCourse(simplerCourse);

        isNewCourseValid(simplerCourse); // Checks every parameter

        courseRepository.save(course);

        return "Course created successfully";
    }

    private Course createCourseFromSimplerCourse(SimplerCourse simplerCourse) {
        if(simplerCourse.getTeacherToken() == null)
            throw new ResourceNotFoundException("The teacher token is null");

        Teacher teacher = teacherService.getTeacherByToken(simplerCourse.getTeacherToken());

        Course course = new Course(teacher,
                                    simplerCourse.getName(),
                                    simplerCourse.getYear(),
                                    simplerCourse.getSubject(),
                                    simplerCourse.getMaxSubscribed(),
                                    simplerCourse.getClassroom(),
                                    simplerCourse.getStartDate(),
                                    simplerCourse.getFinishDate(),
                                    simplerCourse.getType(),
                                    simplerCourse.getDescription());

        return course;
    }

    private SimplerCourse createSimplerCourseFromSimpleCourse(SimpleCourse simpleCourse) {
        if(simpleCourse.getTeacherToken() == null)
            throw new ResourceNotFoundException("The teacher token is null");

        SimplerCourse simplerCourse = new SimplerCourse(simpleCourse.getTeacherToken(),
                simpleCourse.getName(),
                simpleCourse.getYear(),
                simpleCourse.getSubject(),
                simpleCourse.getMaxSubscribed(),
                simpleCourse.getClassroom(),
                simpleCourse.getStartDate(),
                simpleCourse.getFinishDate(),
                simpleCourse.getType(),
                simpleCourse.getDescription());

        return simplerCourse;
    }

    private void checkToken(String token) {
        if (token == null || token.isEmpty()) // Checks the token
            throw new IllegalArgumentException("The teacher token is null");
    }

    private void checkDescription(String description) {
        if (description.length() < 20)
            throw new IllegalArgumentException("The description is too short");
    }

    private void checkStartDate(LocalDate startDate, LocalDate endDate) {
        if (startDate.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("The start date is before the current date");
    }

    private void checkEndDate(LocalDate endDate, LocalDate startDate) {
        if (endDate.isBefore(startDate))
            throw new IllegalArgumentException("The finish date is before the start date");
    }

    private void checkMaxSubscribed(Integer maxSubscribed ) {
        if (maxSubscribed <= 0)
            throw new IllegalArgumentException("The max number of subscribed has to be more than zero");
    }

    private void checkType(String type) {
        if (type == null || type.isEmpty())
            throw new IllegalArgumentException("The type cannot be null");

        if (!(type.equalsIgnoreCase("potenziamento") || type.equalsIgnoreCase("recupero")))
            throw new IllegalArgumentException("The type is not supported");
    }

    private void checkName(String name) {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("The name cannot be null");
    }

    private void checkYear(Integer year) {
        if (year > 5 || year <= 0)
            throw new IllegalArgumentException("The year is not within the 1 - 5 range");
    }

    private void checkSubject(String subject) {
        if(subject == null || subject.isEmpty())
            throw new IllegalArgumentException("The subject cannot be null");
    }

    private void checkClassroom(Integer classroom) {
        if (classroom <= 0 || classroom > 500)
            throw new IllegalArgumentException("The classroom is not within the 1 - 499 range");
    }

    private void isNewCourseValid(SimplerCourse simplerCourse) {
        // Checks the token
        checkToken(simplerCourse.getTeacherToken());

        // Checks the length of the description
        checkDescription(simplerCourse.getDescription());

        // Checks that the start date is not before the day it is made
        checkStartDate(simplerCourse.getStartDate(), simplerCourse.getFinishDate());
        // Checks that the finish date is after the start date
        checkEndDate(simplerCourse.getFinishDate(), simplerCourse.getStartDate());

        // Checks the max number of subscribed, at least one student for course
        checkMaxSubscribed(simplerCourse.getMaxSubscribed());

        // Checks that the type is not empty and
        checkType(simplerCourse.getType());

        // Checks that the name is not null or empty
        checkName(simplerCourse.getName());

        // Checks that the year within the 1 - 5 range
        checkYear(simplerCourse.getYear());

        // Checks that the subject is not null or empty
        checkSubject(simplerCourse.getSubject());

        // checks that the year within the 1 - 499 range, I'm not sure 499 should be the max
        checkClassroom(simplerCourse.getClassroom());
    }

    private void isCourseValid(Course course) {
        // Checks the length of the description
        checkDescription(course.getDescription());

        // Checks that the start date is not before the day it is made and that the finish date is after the start date
        checkEndDate(course.getFinishDate(), course.getStartDate());

        // Checks the max number of subscribed, at least one student for course
        checkMaxSubscribed(course.getMaxSubscribed());

        // Checks that the type is not empty and
        checkType(course.getType());

        // Checks that the name is not null or empty
        checkName(course.getName());

        // Checks that the year within the 1 - 5 range
        checkYear(course.getYear());

        // Checks that the subject is not null or empty
        checkSubject(course.getSubject());

        // checks that the year within the 1 - 499 range, I'm not sure 499 should be the max
        checkClassroom(course.getClassroom());
    }

    public List<Course> getCoursesByTeacher(String teacherToken) {
        checkToken(teacherToken);
        Teacher teacher = teacherService.getTeacherByToken(teacherToken);
        return courseRepository.findByTeacher(teacher);
    }

    public List<Course> getCoursesForRecupero(String recuperoToken) {
        checkToken(recuperoToken);
        Student student = studentService.getStudentByToken(recuperoToken);
        return courseRepository.findCoursesByType("recupero");
    }

    public List<Course> getCoursesForPotenziamento(String recuperoToken) {
        checkToken(recuperoToken);
        Student student = studentService.getStudentByToken(recuperoToken);
        return courseRepository.findCoursesByType("potenziamento");
    }

    public String updateCourse(SimpleCourse simpleCourse) {
        Integer course_id = simpleCourse.getId();

        if(!courseRepository.existsById(course_id))
            throw new ResourceNotFoundException("The course does not exists");

        SimplerCourse simplerCourse = createSimplerCourseFromSimpleCourse(simpleCourse);

        checkToken(simpleCourse.getTeacherToken());
        Teacher teacher = teacherService.getTeacherByToken(simpleCourse.getTeacherToken());

        Course oldCourse = courseRepository.getCourseByCourseId(course_id);
        Course newCourse = createCourseFromSimplerCourse(simplerCourse);
        newCourse.setCourseId(course_id);

        if(!oldCourse.getTeacher().equals(teacher))
            throw new ResourceNotFoundException("You did not create the course, so you cannot update it");

        isCourseValid(newCourse);

        courseRepository.save(newCourse);

        return "Course updated successfully";
    }

    public String deleteCourse(Integer course_id, String teacherToken){
        checkToken(teacherToken);

        Teacher teacher = teacherService.getTeacherByToken(teacherToken);
        Course course = courseRepository.getCourseByCourseId(course_id);

        if(course==null)
            throw new ResourceNotFoundException("The course does not exists");

        if(!course.getTeacher().equals(teacher))
            throw new ResourceNotFoundException("You did not create the course, so you cannot delete it");

        courseRepository.delete(course);

        return "Successfully deleted the course from the database";
    }
}
