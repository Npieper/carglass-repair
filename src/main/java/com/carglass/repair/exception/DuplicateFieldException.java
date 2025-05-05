package com.carglass.repair.exception;

public class DuplicateFieldException extends RuntimeException {
    public DuplicateFieldException(String fieldName) {
        super("Data Integrity constraint: " + fieldName + " has to be unique");
    }
}