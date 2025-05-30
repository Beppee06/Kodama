package org.example.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @InjectMocks
    private CourseService courseService;

    @BeforeEach
    public void setUp() {
        // Setup necessario per i test
    }

    @Test
    public void testInitialization() {
        // Verifica che il servizio sia stato istanziato correttamente
        assertNotNull(courseService);
    }
}