package org.example.service;

import io.swagger.v3.core.util.Json;
import jakarta.transaction.Transactional;
import org.example.exception.*;
import org.example.models.Course;
import org.example.models.Student;
import org.example.models.Subscribed;
import org.example.models.Teacher;
import org.example.repository.CourseRepository;
import org.example.repository.SubscribedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubscribedService {

    private final SubscribedRepository subscribedRepository;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final CourseRepository courseRepository;

    @Autowired
    public SubscribedService(SubscribedRepository subscribedRepository,
                             StudentService studentService,
                             TeacherService teacherService,
                             CourseRepository courseRepository) {
        this.subscribedRepository = subscribedRepository;
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.courseRepository = courseRepository;
    }

    @Transactional
    public String subscribeStudentToCourse(String studentToken, Integer courseId) {
        //Validare token e ottenere lo studente
        Student student = studentService.getStudentByToken(studentToken);

        //Ottenere il corso
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + courseId));

        //Controllo se il corso è finito
        if (course.getFinishDate() != null && course.getFinishDate().isBefore(LocalDate.now())) {
            throw new TokenInvalid("Cannot subscribe: this course has already finished.");
        }

        //Controllo se sono già iscritto
        if (subscribedRepository.existsByStudentAndCourse(student, course)) {
            throw new EmailAlreadyExistException("Student is already subscribed to this course.");
        }

        //Controllo se ci sono altri posti disponibili
        long currentSubscribedCount = subscribedRepository.countByCourse(course);
        if (currentSubscribedCount >= course.getMaxSubscribed()) {
            throw new TokenInvalid("Cannot subscribe: the course is full.");
        }

        //salva se è tutto ok
        Subscribed subscription = new Subscribed(student, course);
        subscribedRepository.save(subscription);

        return "Subscription successful.";
    }

    @Transactional
    public String unsubscribeStudentFromCourse(String studentToken, Integer courseId) {
        Student student = studentService.getStudentByToken(studentToken);

        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("Course not found with ID: " + courseId);
        }
        Course course = courseRepository.findById(courseId).get();

        Subscribed subscription = subscribedRepository.findByStudentAndCourse(student, course)
                .orElseThrow(() -> new ResourceNotFoundException("Student is not subscribed to this course, cannot unsubscribe."));

        subscribedRepository.delete(subscription);

        return "Unsubscription successful.";
    }

    @Transactional
    public List<Student> getStudentsSubscribedToCourse(String teacherToken, Integer courseId) {
        Teacher requestingTeacher = teacherService.getTeacherByToken(teacherToken);

        Optional<Course> optCourse = courseRepository.findById(courseId);
        if(optCourse == null)
            throw new ResourceNotFoundException("Course not found with ID: " + courseId);

        Course course = optCourse.get();

        // Controllo Autorizzazione
        if (!course.getTeacher().equals(requestingTeacher)) {
            throw new TokenInvalid("You are not authorized to view the subscribers for this course.");
        }

        List<Subscribed> subscriptions = subscribedRepository.findByCourse(course);

        List<Student> subscribedStudents = subscriptions.stream()
                .map(Subscribed::getStudent)
                .collect(Collectors.toList());

        return subscribedStudents;
    }
}