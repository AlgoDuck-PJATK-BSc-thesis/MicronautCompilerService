package com.beetcode.services.compiler.Dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import lombok.Getter;


@Introspected
@Getter
public class CompilationErrorDto extends CompilationResponseAbstr{
    private final String errorMessage;

    public CompilationErrorDto(@JsonProperty("ErrorMsg") String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

