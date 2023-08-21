package org.temkarus0070.efmsocialmedia.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.temkarus0070.efmsocialmedia.security.dto.ErrorDto;

import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerErrorsAdvice {

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<ErrorDto> entityNotFound(EntityNotFoundException entityNotFoundException) {
        return new ResponseEntity<>(new ErrorDto(entityNotFoundException.getLocalizedMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ErrorDto> illegalArgument(IllegalArgumentException illegalArgumentException) {
        return new ResponseEntity<>(new ErrorDto(illegalArgumentException.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> validation(MethodArgumentNotValidException exception) {
        return new ResponseEntity<>(new ErrorDto(exception.getFieldErrors()
                                                          .stream()
                                                          .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                                          .collect(Collectors.joining("; "))), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseEntity<ErrorDto> auth(AuthenticationException exception) {
        return new ResponseEntity<>(new ErrorDto(exception.getLocalizedMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ErrorDto> accessDenied(AccessDeniedException exception) {
        return new ResponseEntity<>(new ErrorDto(exception.getLocalizedMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = Throwable.class)
    public ResponseEntity<ErrorDto> others(Throwable exception) {
        return new ResponseEntity<>(new ErrorDto(exception.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
