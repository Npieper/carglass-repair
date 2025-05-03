package com.carglass.repair.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(Resource resource, Long id) {
        super(resource.name() + " with id " + id + " not found");
    }
}