package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE) // Dice a Spring di restituire 406 se non gestita altrove
public class PasswordDoNotMatch extends RuntimeException {
    public PasswordDoNotMatch(String message) {
        super(message);
    }
}
