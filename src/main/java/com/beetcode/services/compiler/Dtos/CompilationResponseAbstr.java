package com.beetcode.services.compiler.Dtos;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Introspected
@Serdeable
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CompilationResponseDto.class, name = "success"),
        @JsonSubTypes.Type(value = CompilationErrorDto.class, name = "error"),
})
public abstract class CompilationResponseAbstr {}
