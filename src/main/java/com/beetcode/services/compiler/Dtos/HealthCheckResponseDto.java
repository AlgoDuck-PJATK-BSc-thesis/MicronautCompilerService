package com.beetcode.services.compiler.Dtos;

import io.micronaut.serde.annotation.Serdeable;
import lombok.Getter;

import java.util.Map;

@Serdeable
@Getter
public class HealthCheckResponseDto {
    private final Map<String, String> fileHashes;

    public HealthCheckResponseDto(Map<String, String> fileHashes) {
        this.fileHashes = fileHashes;
    }
}
