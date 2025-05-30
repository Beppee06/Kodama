package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST) // Dice a Spring di restituire 400 se non gestita altrove
public class EmailMalformedException extends RuntimeException {
  public EmailMalformedException(String message) {
    super(message);
  }
}