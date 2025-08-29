package com.beetcode.services.compiler.Dtos;

import io.micronaut.core.annotation.Introspected;
import lombok.Getter;

import java.util.Map;

@Introspected
@Getter
public class CompilationResponseDto extends CompilationResponseAbstr {
    private final Map<String, String> generatedClassFiles;

    public CompilationResponseDto(Map<String, String> generatedClassFiles) {
        this.generatedClassFiles = generatedClassFiles;
    }
}
