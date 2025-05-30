package org.example.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(HelloController.class)
public class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testSayHello() throws Exception {
        // Test using MockMvc
        mockMvc.perform(MockMvcRequestBuilders.get("/hello"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.message").value("Ciao dal Kodama API!"));
    }

    @Test
    void testSayHelloDirectly() {
        // Create controller instance and test directly
        HelloController controller = new HelloController();
        Map<String, String> result = controller.sayHello();

        assertEquals("Ciao dal Kodama API!", result.get("message"));
    }
}