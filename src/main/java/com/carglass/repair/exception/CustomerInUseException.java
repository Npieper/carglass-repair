package com.carglass.repair.exception;

public class CustomerInUseException extends RuntimeException {
    public CustomerInUseException(Long id) {
        super("Customer with ID " + id + " could not be deleted because it is still in use");
    }
}
