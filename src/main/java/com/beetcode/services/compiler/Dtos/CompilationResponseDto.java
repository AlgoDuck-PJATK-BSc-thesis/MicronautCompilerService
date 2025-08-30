package com.beetcode.services.compiler.Dtos;

import com.beetcode.services.compiler.Dtos.CompilationResponseAbstr;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Getter;

import java.util.Map;

@Serdeable
@Getter
public class CompilationResponseDto extends CompilationResponseAbstr {
    private final Map<String, String> generatedClassFiles;

    public CompilationResponseDto(Map<String, String> generatedClassFiles) {
        this.generatedClassFiles = generatedClassFiles;
    }
}
