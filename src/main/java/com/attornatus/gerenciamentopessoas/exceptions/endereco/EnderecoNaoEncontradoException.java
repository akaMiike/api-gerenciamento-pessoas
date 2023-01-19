package com.attornatus.gerenciamentopessoas.exceptions.endereco;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EnderecoNaoEncontradoException extends ResponseStatusException {

    public EnderecoNaoEncontradoException() {
        super(HttpStatus.NOT_FOUND, "Endereço não encontrado.");
    }

    public EnderecoNaoEncontradoException(String reason) {
        super(HttpStatus.NOT_FOUND, reason);
    }
}
