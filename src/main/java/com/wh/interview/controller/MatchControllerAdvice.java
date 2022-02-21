package com.wh.interview.controller;

import com.wh.interview.controller.api.ErrorResponse;
import com.wh.interview.exception.MatchNotFoundException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class MatchControllerAdvice {

    public static final String DEFAULT_ERROR_VIEW = "error";

    @ExceptionHandler(value = MatchNotFoundException.class)
    public ResponseEntity<Object> handleMatchNotFoundException(HttpServletRequest req, Exception e) throws Exception {

        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null)
            throw e;

        ErrorResponse errorResponse =
                new ErrorResponse(HttpStatus.NOT_FOUND, e.getMessage(), req.getRequestURI());

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatus());

    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {

        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null)
            throw e;

        ErrorResponse errorResponse =
                new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), req.getRequestURL().toString());

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatus());

    }
}