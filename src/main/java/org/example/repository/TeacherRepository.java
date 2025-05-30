package org.example.repository;

import org.example.models.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
    /**
     * Metodo derivato alternativo: controlla l'esistenza basata sull'email.
     * @param email L'email da cercare.
     * @return true se esiste un utente con quell'email, false altrimenti.
     */
    boolean existsByEmail(String email);

    /**
     * Metodo derivato alternativo: restituisce lo studente in base alla email.
     * @param email L'email da cercare.
     * @return student.
     */
    Teacher findByEmail(String email);

    /**
     * Metodo derivato alternativo: controlla se le infromazioni sull'utente corrispondono.
     * @param email l'email da cercare.
     * @param password la password da cercare.
     * @return true se esiste un utente con quell'email, false altrimenti.
     */
    //unused at the moment
    //Boolean existsByEmailAndPassword(String email, String password);
}