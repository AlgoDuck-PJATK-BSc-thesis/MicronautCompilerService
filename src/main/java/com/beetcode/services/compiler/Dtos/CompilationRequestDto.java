package com.beetcode.services.compiler.Dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CompilationRequestDto(
        @JsonProperty("SrcCodeB64") String codeB64,
        @JsonProperty("ClassName") String className,
        @JsonProperty("ExecutionId") UUID executionId
        ) { }
