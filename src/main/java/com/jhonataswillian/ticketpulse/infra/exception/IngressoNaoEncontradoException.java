package com.jhonataswillian.ticketpulse.infra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class IngressoNaoEncontradoException extends RuntimeException {
    public IngressoNaoEncontradoException(String message) {
        super(message);
    }
}
