package com.beetcode.services.compiler;

import com.beetcode.services.compiler.Dtos.HealthCheckResponseDto;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;

@Controller
public class HealthCheckController {
    private final HealthCheckService healthCheckService;

    public HealthCheckController(HealthCheckService healthCheckService) {
        this.healthCheckService = healthCheckService;
    }

    @Get("/health_check")
    public HttpResponse<HealthCheckResponseDto> CheckCompilerHealth(){
        DebugLogger.log("Health check request received");
        return HttpResponse.ok(healthCheckService.getFileHashes());
    }
}
