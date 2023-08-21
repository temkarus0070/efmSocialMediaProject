package org.temkarus0070.efmsocialmedia.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.temkarus0070.efmsocialmedia.security.dto.ErrorDto;

@ControllerAdvice
public class ControllerErrorsAdvice {

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<ErrorDto> entityNotFound(EntityNotFoundException entityNotFoundException) {
        return new ResponseEntity<>(new ErrorDto(entityNotFoundException.getLocalizedMessage()), HttpStatus.NOT_FOUND);
    }

}
