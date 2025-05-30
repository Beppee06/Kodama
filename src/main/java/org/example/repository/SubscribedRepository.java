package org.example.repository;

import org.example.models.Course;
import org.example.models.Student;
import org.example.models.Subscribed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscribedRepository extends JpaRepository<Subscribed, Integer> {

    /**
     * Conta il numero di iscrizioni per un dato corso.
     * Metodo derivato basato sul campo 'course' nell'entità Subscribed.
     *
     * @param course Il corso per cui contare le iscrizioni.
     * @return Il numero di studenti attualmente iscritti al corso.
     */
    long countByCourse(Course course);

    /**
     * Trova tutte le iscrizioni relative a un determinato corso.
     * Metodo derivato basato sul campo 'course'.
     *
     * @param course Il corso di cui trovare le iscrizioni.
     * @return Una lista di entità Subscribed per quel corso.
     */
    List<Subscribed> findByCourse(Course course);

    /**
     * Trova una specifica iscrizione basata sullo studente e sul corso.
     * Metodo derivato basato sui campi 'student' e 'course'.
     *
     * @param student Lo studente da cercare.
     * @param course Il corso da cercare.
     * @return Un Optional contenente l'iscrizione se trovata, altrimenti Optional vuoto.
     */
    Optional<Subscribed> findByStudentAndCourse(Student student, Course course);

    /**
     * Verifica se esiste un'iscrizione per un dato studente e corso.
     * Metodo derivato basato sui campi 'student' e 'course'.
     *
     * @param student Lo studente da cercare.
     * @param course Il corso da cercare.
     * @return true se l'iscrizione esiste, false altrimenti.
     */
    boolean existsByStudentAndCourse(Student student, Course course);

}