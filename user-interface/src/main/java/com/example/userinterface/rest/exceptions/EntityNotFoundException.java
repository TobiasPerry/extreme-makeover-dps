package com.example.userinterface.rest.exceptions;


public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String entity, Long id) {
        super("Could not find " + entity + " with id: " + id);
    }
}
