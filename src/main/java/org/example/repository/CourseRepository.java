package org.example.repository;

import org.example.models.Course;
import org.example.models.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    /**
     * Metodo derivato alternativo: controlla l'esistenza basata sul courseId.
     * @param courseId L'Id da cercare.
     * @return true se esiste un corso con quell'id, false altrimenti.
     */
    boolean existsById(Integer courseId);

    /**
     * Metodo derivato alternativo: restituisce il corso basandosi sul courseId.
     * @param courseId L'Id da cercare.
     * @return il corso se esiste un corso con quell'id.
     */
    Course getCourseByCourseId(Integer courseId);

    /**
     * Trova tutti i corsi associati a un insegnante specifico.
     * @param teacher L'entità Teacher per cui cercare i corsi.
     * @return Una lista di entità Course associate all'insegnante, o una lista vuota se non ne vengono trovati.
     */
    List<Course> findByTeacher(Teacher teacher);

    List<Course> findCoursesByType(String tipo);
}
