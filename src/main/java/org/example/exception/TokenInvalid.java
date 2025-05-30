package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN) // Dice a Spring di restituire 403 se non gestita altrove
public class TokenInvalid extends RuntimeException {
    public TokenInvalid(String message) {
        super(message);
    }
}
