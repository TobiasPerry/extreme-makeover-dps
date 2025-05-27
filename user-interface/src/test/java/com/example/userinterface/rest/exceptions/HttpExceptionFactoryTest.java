package com.example.userinterface.rest.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class HttpExceptionFactoryTest {

    @Test
    void serverError() {
        // Act
        ResponseEntity<HttpErrorResponse> response = HttpExceptionFactory.serverError("Error", "Message");

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Error", response.getBody().getError());
        assertEquals("Message", response.getBody().getMessage());
    }

    @Test
    void notFound() {
        // Act
        ResponseEntity<HttpErrorResponse> response = HttpExceptionFactory.notFound("Error", "Message");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Error", response.getBody().getError());
        assertEquals("Message", response.getBody().getMessage());
    }

    @Test
    void badRequest() {
        // Act
        ResponseEntity<HttpErrorResponse> response = HttpExceptionFactory.badRequest("Error", "Message");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Error", response.getBody().getError());
        assertEquals("Message", response.getBody().getMessage());
    }

    @Test
    void unauthorized() {
        // Act
        ResponseEntity<HttpErrorResponse> response = HttpExceptionFactory.unauthorized("Error", "Message");

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Error", response.getBody().getError());
        assertEquals("Message", response.getBody().getMessage());
    }
} 