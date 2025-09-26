package com.beetcode.services.compiler;

import com.beetcode.services.compiler.Dtos.CompilationErrorDto;
import com.beetcode.services.compiler.Dtos.CompilationRequestDto;
import com.beetcode.services.compiler.Dtos.CompilationResponseAbstr;
import com.beetcode.services.compiler.Dtos.CompilationResponseDto;
import io.micronaut.runtime.http.scope.RequestScope;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@RequestScope
public class CompilationService {
    public CompilationResponseAbstr compile(CompilationRequestDto compilationRequestDto){
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(
                "/bin/sh",
                "/app/scripts/compiler-src.sh",
                compilationRequestDto.className(),
                compilationRequestDto.codeB64(),
                compilationRequestDto.executionId().toString()
        );

        Process start;
        try {
            start = builder.start();
        } catch (IOException e) {
            return returnError(compilationRequestDto);
        }
        int exitCode = 137; // sigkill exit code

        try {
            exitCode = start.waitFor();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String byteCodeDirPath = String.format("/app/client-bytecode/%s", compilationRequestDto.executionId());

        if (exitCode != 0 && exitCode != 137){
            return returnError(compilationRequestDto);
        }

        List<String> generatedClassFiles;

        try {
            generatedClassFiles = listFilesUsingFilesList(byteCodeDirPath);
        } catch (IOException e) {
            return returnError(compilationRequestDto);
        }

        Map<String, String> generatedBytecode = new HashMap<>();

        for (String filename : generatedClassFiles) {
            /*
            * direct string formatting on potentially unsafe class file names is fine here
            * since dangerous file names should be caught by the ast generator
            * (i.e. throw an error for illegal identifier or unexpected token).
            * Besides, this is a sandbox.
            * */
            String classFilePath = String.format("%s/%s", byteCodeDirPath, filename);
            byte[] classFileBytes = new byte[0];
            try {
                classFileBytes = readAllBytesFromFile(classFilePath);
            } catch (IOException ignored) {
                // should not happen
            }

            String classFileBytesB64 = Base64.getEncoder().encodeToString(classFileBytes);

            generatedBytecode.put(filename, classFileBytesB64);
        }
        return new CompilationResponseDto(compilationRequestDto.className(), generatedBytecode);
    }

    private byte[] readAllBytesFromFile(String fileName) throws IOException {
        try (RandomAccessFile reader = new RandomAccessFile(fileName, "r")) {
            FileChannel readerChannel = reader.getChannel();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int bufferSize = Math.max(1024, outputStream.size());
            ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
            while (readerChannel.read(buffer) > 0){
                outputStream.write(buffer.array(), 0, buffer.position());
                buffer.clear();
            }
            return outputStream.toByteArray();
        } catch (FileNotFoundException e) {
            return new byte[0];
        }
    }

    private List<String> listFilesUsingFilesList(String dir) throws IOException {
        try (Stream<Path> stream = Files.list(Paths.get(dir))) {
            return stream.filter(file -> !Files.isDirectory(file)).map(Path::getFileName).map(Path::toString).toList();
        }
    }

    private CompilationErrorDto returnError(CompilationRequestDto compilationRequestDto){
        try {
            String errorLogFileName = String.format("/app/error-log/%s/err.log", compilationRequestDto.executionId());
            byte[] errorLogFileRaw = readAllBytesFromFile(errorLogFileName);
            if (errorLogFileRaw.length == 0) throw new IOException();
            String errorLogContents = new String(errorLogFileRaw, StandardCharsets.UTF_8);
            String errorLogContentsSanitized = errorLogContents.replace(String.format("/app/client-src/%s/", compilationRequestDto.executionId()), "");
            return new CompilationErrorDto(errorLogContentsSanitized);
        }
        catch (IOException ex){
            return new CompilationErrorDto("Error during compilation. Log file not found. Abandoning execution.");
        }
    }
}
