/*

package org.example.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testPublicEndpointsAccessible() throws Exception {
        // Test accesso a Swagger UI
        mockMvc.perform(get("/swagger-ui.html"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk());

        // Test accesso all'endpoint di autenticazione
        mockMvc.perform(get("/api/auth/login"))
                .andExpect(status().isOk());
    }

    @Test
    void testProtectedEndpointsRequireAuthentication() throws Exception {
        // Test che un endpoint protetto richieda autenticazione
        mockMvc.perform(get("/api/protected"))
                .andExpect(status().isUnauthorized());
    }

    @Test

    @WithMockUser
    void testProtectedEndpointsAccessibleWithAuth() throws Exception {
        // Test che un endpoint protetto sia accessibile con autenticazione
        mockMvc.perform(get("/api/protected"))
                .andExpect(status().isOk());
    }
}

     */