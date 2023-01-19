package com.attornatus.gerenciamentopessoas.exceptions.pessoa;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PessoaNaoEncontradaException extends ResponseStatusException {

    public PessoaNaoEncontradaException() {
        super(HttpStatus.NOT_FOUND, "Pessoa não encontrada.");
    }
}
