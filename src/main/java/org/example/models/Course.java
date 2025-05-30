package org.example.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate; // LocalDate represents a date (year, month, day) without any time-of-day or timezone information
//import java.util.Objects; // For equals/hashCode implementation

@Entity // Marks this class as a JPA entity (maps to a database table)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "course") // Optional: Specifies the table name (defaults to class name)
public class Course {
    @Id // Marks this field as the primary key
    // Assuming the database will generate the ID (e.g., auto_increment, sequence)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tells JPA to use DB auto-increment
    @Column(name = "course_id", updatable = false, nullable = false)
    private Integer courseId; // Use Integer (wrapper class) instead of primitive int because of its nullable

    @ManyToOne(fetch = FetchType.EAGER) // Or FetchType.EAGER
    @JsonIgnore
    @JoinColumn(
            name = "teacher_id", // Name for the FK column in THIS entity's table
            referencedColumnName = "teacher_id", // Name of the PK column in the Teacher table
            nullable = false // Optional: Make the relationship required (cannot be null)
            // foreignKey = @ForeignKey(name = "fk_course_to_teacher") // Optional: Suggest FK name
    )
    private Teacher teacher; // CORRECTED: Type should be Teacher entity, name is camelCase

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description", nullable = false, unique = true)
    private String description;

    @Column(name = "year", nullable = false)
    private int year; // Changed type to int (assuming it's a year number)

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "max_subscribed", nullable = false) // Optional: Changed column name to snake_case
    private int maxSubscribed; // Changed type to int

    @Column(name = "classroom", nullable = false)
    private Integer classroom;

    @Column(name = "start_date", nullable = false) // Optional: Changed column name to snake_case
    private LocalDate startDate; // Changed type to LocalDate

    @Column(name = "finish_date", nullable = false) // Optional: Changed column name to snake_case
    private LocalDate finishDate; // Changed type to LocalDate

    @Column(name = "type", nullable = false) // Optional: Changed column name to snake_case
    private String type; // Changed type to LocalDate

    // JPA requires a no-argument constructor (can be protected or public)
    public Course() {
    }

    // Example Constructor (without generated ID, and potentially setting Teacher later)
    public Course(Teacher teacher, String name, int year, String subject, int maxSubscribed, Integer classroom, LocalDate startDate, LocalDate finishDate, String type, String description) {
        this.teacher = teacher;
        this.name = name;
        this.year = year;
        this.subject = subject;
        this.maxSubscribed = maxSubscribed;
        this.classroom = classroom;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.type = type;
        this.description = description;
    }

    // --- Getters and Setters ---

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) { this.courseId = courseId; }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getMaxSubscribed() {
        return maxSubscribed;
    }

    public void setMaxSubscribed(int maxSubscribed) {
        this.maxSubscribed = maxSubscribed;
    }

    public Integer getClassroom() {
        return classroom;
    }

    public void setClassroom(Integer classroom) {
        this.classroom = classroom;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDate finishDate) {
        this.finishDate = finishDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // --- equals(), hashCode(), toString() ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        // Use courseId for equality check, assuming it's set and unique once persisted
        // Note: Equality check for non-persisted entities might behave differently
        return courseId == course.courseId && courseId != 0; // Avoid matching unsaved entities (id=0)
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", teacherId=" + (teacher != null ? teacher.getTeacherId() : "null") + // Avoid NullPointerException, get actual ID
                ", name='" + name + '\'' +
                ", year=" + year +
                ", subject='" + subject + '\'' +
                ", maxSubscribed=" + maxSubscribed +
                ", classroom='" + classroom + '\'' +
                ", startDate=" + startDate +
                ", finishDate=" + finishDate + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description +
                '}';
    }
}

