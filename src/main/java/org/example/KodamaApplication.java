package org.example; // Usa il tuo package

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // Abilita la configurazione automatica di Spring Boot
public class KodamaApplication { // Nome della classe principale

    public static void main(String[] args) {
        SpringApplication.run(KodamaApplication.class, args);
        //quando lo fai partire ti da login da fare
        // username = user e password = [te lo da durante il debug (Using generated security password:)]
    }
}