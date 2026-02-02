package com.jhonataswillian.ticketpulse.infra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class IngressoIndisponivelException extends RuntimeException{
    public IngressoIndisponivelException(String message) {
        super(message);
    }
}
