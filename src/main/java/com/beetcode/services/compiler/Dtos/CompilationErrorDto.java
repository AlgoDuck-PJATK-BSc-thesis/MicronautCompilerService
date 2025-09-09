package com.beetcode.services.compiler.Dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Getter;


@Serdeable
@Getter
public class CompilationErrorDto extends CompilationResponseAbstr{
    private final String errorMessage;

    public CompilationErrorDto(@JsonProperty("ErrorMsg") String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

