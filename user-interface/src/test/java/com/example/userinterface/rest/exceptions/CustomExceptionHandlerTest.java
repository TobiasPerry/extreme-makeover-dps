package com.example.userinterface.rest.exceptions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomExceptionHandlerTest {

    private CustomExceptionHandler exceptionHandler;
    private WebRequest webRequest;
    private HttpHeaders headers;

    @BeforeEach
    void setUp() {
        exceptionHandler = new CustomExceptionHandler();
        webRequest = mock(WebRequest.class);
        headers = new HttpHeaders();
    }

    @Test
    void handleBadRequestException() {
        // Arrange
        Exception exception = new BadRequestException("Invalid request", "entity", "param");

        // Act
        ResponseEntity<HttpErrorResponse> response = exceptionHandler.handleBadRequestException(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Bad request", response.getBody().getError());
        assertEquals("Invalid request entity param", response.getBody().getMessage());
    }

    @Test
    void handleNotFoundException() {
        // Arrange
        Exception exception = new EntityNotFoundException("entity", 1L);

        // Act
        ResponseEntity<HttpErrorResponse> response = exceptionHandler.handleNotFoundException(exception);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Element not found", response.getBody().getError());
        assertEquals("Could not find entity with id: 1", response.getBody().getMessage());
    }

    @Test
    void handleMethodArgumentNotValid() {
        // Arrange
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("object", "field", "default message");

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(Collections.singletonList(fieldError));

        // Act
        ResponseEntity<Object> response = exceptionHandler.handleMethodArgumentNotValid(
            exception, headers, HttpStatus.BAD_REQUEST, webRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);
        Map<String, String> errors = (Map<String, String>) response.getBody();
        assertEquals("default message", errors.get("field"));
    }

    @Test
    void handleException() {
        // Arrange
        Exception exception = new RuntimeException("Internal server error");

        // Act
        ResponseEntity<HttpErrorResponse> response = exceptionHandler.handleException(exception);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("An error occurred", response.getBody().getError());
        assertEquals("Internal server error", response.getBody().getMessage());
    }
} 