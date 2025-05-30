package org.example.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPasswordHash("hashedpassword");
        user.setActive(true);

        entityManager.persist(user);
        entityManager.flush();

        assertNotNull(user.getId());
    }

    @Test
    public void testUserProperties() {
        // Test con costruttore predefinito e setter
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPasswordHash("hashedpassword");
        user.setActive(true);

        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("hashedpassword", user.getPasswordHash());
        assertTrue(user.isActive());

        // Test con AllArgsConstructor
        User user2 = new User(2L, "testuser2", "test2@example.com", "hashedpassword2", false);

        assertEquals(2L, user2.getId());
        assertEquals("testuser2", user2.getUsername());
        assertEquals("test2@example.com", user2.getEmail());
        assertEquals("hashedpassword2", user2.getPasswordHash());
        assertFalse(user2.isActive());
    }

    @Test
    @Transactional
    public void testUsernameUniqueness() {
        // Crea primo utente
        User user1 = new User();
        user1.setUsername("sameusername");
        user1.setEmail("user1@example.com");
        user1.setPasswordHash("password1");

        entityManager.persist(user1);
        entityManager.flush();

        // Crea secondo utente con stesso username
        User user2 = new User();
        user2.setUsername("sameusername");
        user2.setEmail("user2@example.com");
        user2.setPasswordHash("password2");

        // Dovrebbe fallire per vincolo di unicità
        assertThrows(DataIntegrityViolationException.class, () -> {
            entityManager.persist(user2);
            entityManager.flush();
        });
    }

    @Test
    @Transactional
    public void testEmailUniqueness() {
        // Crea primo utente
        User user1 = new User();
        user1.setUsername("user1");
        user1.setEmail("same@example.com");
        user1.setPasswordHash("password1");

        entityManager.persist(user1);
        entityManager.flush();

        // Crea secondo utente con stessa email
        User user2 = new User();
        user2.setUsername("user2");
        user2.setEmail("same@example.com");
        user2.setPasswordHash("password2");

        // Dovrebbe fallire per vincolo di unicità
        assertThrows(DataIntegrityViolationException.class, () -> {
            entityManager.persist(user2);
            entityManager.flush();
        });
    }
}
