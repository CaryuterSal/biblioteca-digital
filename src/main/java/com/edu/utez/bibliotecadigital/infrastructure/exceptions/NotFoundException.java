package com.edu.utez.bibliotecadigital.infrastructure.exceptions;

import java.io.Serializable;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
    public NotFoundException(Class<?> clazz, Serializable id) {
        super("Could not find entity with id " + id + " for model " + clazz.getSimpleName());
    }
}
