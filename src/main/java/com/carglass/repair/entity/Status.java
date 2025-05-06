package com.carglass.repair.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Status {
    OFFEN,
    IN_BEARBEITUNG,
    ABGESCHLOSSEN;

    @JsonCreator
    public static Status fromString(String value) {
        return Status.valueOf(value.toUpperCase());
    }
}
