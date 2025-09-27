package com.beetcode.services.compiler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public abstract class DebugLogger {
    public static void log(String message) {
        try {
            Files.write(Paths.get("/tmp/log.log"),
                    message.getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
