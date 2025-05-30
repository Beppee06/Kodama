package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.KodamaApplication;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Base class for Spring MVC controller integration tests.
 *
 * This class can be extended by controller test classes when you want to perform
 * integration tests with the full Spring context loaded instead of using mocks.
 *
 * Note: You would need to add @SpringBootTest annotation with your main application class
 * Example: @SpringBootTest(classes = YourApplication.class)
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = KodamaApplication.class) // Sostituisci KodamaApplication con la tua classe principale!
@AutoConfigureMockMvc
public abstract class IntegrationTestSetup {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    // Common helper methods for integration tests can be added here

    /**
     * Converts an object to JSON string
     */
    protected String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}