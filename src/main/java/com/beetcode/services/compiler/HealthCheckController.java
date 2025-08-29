package com.beetcode.services.compiler;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;

@Controller
public class HealthCheckController {
    @Post(value = "/record", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public HttpResponse CheckCompilerHealth(){
        return HttpResponse.noContent();
    }
}
