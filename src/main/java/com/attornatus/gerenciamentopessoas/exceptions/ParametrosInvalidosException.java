package com.attornatus.gerenciamentopessoas.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ParametrosInvalidosException extends ResponseStatusException {

    public ParametrosInvalidosException(String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
    }
}
