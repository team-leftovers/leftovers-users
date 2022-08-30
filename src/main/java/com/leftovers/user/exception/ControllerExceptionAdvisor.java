package com.leftovers.user.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionAdvisor {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public Map<String, Object> handleNoSuchElementException(NoSuchElementException ex) {
        log.error(ex.getMessage());
        return makeResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateEmailException.class)
    public Map<String, Object> handleDuplicateEmailException(DuplicateEmailException ex) {
        log.error(ex.getMessage());
        return makeResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage());

        var response = makeResponse("Invalid field(s) in request.", HttpStatus.BAD_REQUEST);

        var errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .collect(
                        Collectors.toMap(
                                error -> ((FieldError)error).getField(),
                                error -> {
                                    var msg = ((FieldError)error).getDefaultMessage();
                                    msg = msg == null || msg.isBlank() ? "Unknown validation failure." : msg;

                                    log.debug("Field" + ((FieldError) error).getField() + " Message: " + msg);
                                    return msg;
                                },
                                (s, m) -> {
                                    var first = Character.isUpperCase(m.charAt(0)) ? m : s;
                                    var second = first.equals(s) ? m : s;
                                    return first + ", " + second;
                                }
                        )
                );
        response.put("message", errors);
        return response;
    }

    private Map<String, Object> makeResponse(String message, HttpStatus status) {
        var response = new HashMap<String, Object>();
        response.put("error", message);
        response.put("status", status.value());
        return response;
    }
}
