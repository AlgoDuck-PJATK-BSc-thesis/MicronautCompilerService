package com.beetcode.services.compiler;

import com.beetcode.services.compiler.Dtos.HealthCheckResponseDto;
import io.micronaut.runtime.http.scope.RequestScope;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@RequestScope
public class HealthCheckService {
    private final String[] filesPathsToCheck = {
            "/usr/bin/java",
            "/usr/bin/javac",
            "/app/compiler.jar",
            "/app/lib/gson-2.13.1.jar",
            "/app/RequestHandler.class",
            "/app/scripts/compiler-src.sh",
            "/app/process-input.sh",
            "/app/proxy.sh",
            "/etc/init.d/entrypoint",
            "/etc/init.d/proxy",
            "/etc/resolv.conf",
            "/etc/apk/repositories"
    };

    public HealthCheckResponseDto getFileHashes() {
        Map<String, String> fileHashes = new HashMap<>();
        for (String path : filesPathsToCheck) {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(
              "/bin/sh",
              "/app/scripts/get-file-hash.sh",
              path
            );
            Process process;
            try {
                process = builder.start();
            } catch (IOException e) {
                fileHashes.put(path, ""); // guaranteed healthcheck fail
                break;
            }

            int exitCode = 0;
            try {
                exitCode = process.waitFor();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            if (exitCode == 0) {
                StringBuilder processOutput = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        processOutput.append(line);
                    }
                    fileHashes.put(path, processOutput.toString());
                    continue;
                } catch (IOException ignored) {} // not actually ignored. The handling is just delegated to the lines below which also cover a not existent file. Script exits with 1 if file not found
            }
            fileHashes.put(path, ""); // guaranteed healthcheck fail
            break;
        }
        return new HealthCheckResponseDto(fileHashes);
    }
}
