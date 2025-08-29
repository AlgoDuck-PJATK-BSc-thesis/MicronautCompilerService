package com.beetcode.services.compiler;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;

@Controller
public class HealthCheckController {
    @Get("/health_check")
    public HttpResponse CheckCompilerHealth(){
        return HttpResponse.noContent();
    }
}
