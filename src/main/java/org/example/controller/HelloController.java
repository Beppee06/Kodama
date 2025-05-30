package org.example.controller; // Crea un package per i controller

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController // Indica che questa classe è un controller REST
@RequestMapping("/hello") // Prefisso per tutti gli endpoint in questa classe
public class HelloController
{
    @CrossOrigin(origins = "http://localhost:80")
    @GetMapping // Mappa le richieste HTTP GET a questo metodo
    public Map<String, String> sayHello() {
        // Restituisce un oggetto Java, Spring Boot lo convertirà automaticamente in JSON
        return Map.of("message", "Ciao dal Kodama API!");
    }
}
