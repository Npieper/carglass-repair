package com.carglass.repair.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum GlassType {
    FRONTSCHEIBE,
    SEITENSCHEIBE;

    @JsonCreator
    public static GlassType fromString(String value) {
        return GlassType.valueOf(value.toUpperCase());
    }
}
