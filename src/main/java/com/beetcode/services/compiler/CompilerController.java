package com.beetcode.services.compiler;

import com.beetcode.services.compiler.Dtos.CompilationRequestDto;
import com.beetcode.services.compiler.Dtos.CompilationResponseAbstr;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;

@Controller
public class CompilerController {
    private final CompilationService compilationService;

    public CompilerController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @Post(value = "/compile", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public HttpResponse<CompilationResponseAbstr> compileCode(@Body final CompilationRequestDto request){
        return HttpResponse.ok(compilationService.compile(request));
    }
}
