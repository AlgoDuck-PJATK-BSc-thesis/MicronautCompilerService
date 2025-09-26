#!/bin/bash

CLASS_NAME="$1"
CODE_B64="$2"
EXEC_ID="$3"

echo "Script started with CLASS_NAME=$CLASS_NAME, EXEC_ID=$EXEC_ID" >&2
echo "Current working directory: $(pwd)" >&2
echo "PATH: $PATH" >&2

# Check if javac is available
if command -v javac >/dev/null 2>&1; then
    echo "javac found at: $(which javac)" >&2
    echo "javac version: $(javac -version 2>&1)" >&2
else
    echo "ERROR: javac command not found" >&2
    exit 127
fi

echo "Creating directories..." >&2
mkdir -p "/app/client-src/$EXEC_ID"
mkdir -p "/app/error-log/$EXEC_ID"

echo "Decoding and writing Java source file..." >&2
echo "$CODE_B64" | base64 -d > "/app/client-src/$EXEC_ID/$CLASS_NAME.java"

if [ $? -ne 0 ]; then
    echo "ERROR: Failed to decode base64 or write source file" >&2
    exit 1
fi

echo "Source file created successfully" >&2
echo "Source file contents:" >&2
cat "/app/client-src/$EXEC_ID/$CLASS_NAME.java" >&2

echo "Starting compilation..." >&2
javac -cp "/app/app-lib/gson-2.13.1.jar" -proc:none -d "/app/client-bytecode/$EXEC_ID" "/app/client-src/$EXEC_ID/$CLASS_NAME.java" 2>"/app/error-log/$EXEC_ID/err.log"

COMPILATION_EXIT_CODE=$?
echo "Compilation finished with exit code: $COMPILATION_EXIT_CODE" >&2

if [ $COMPILATION_EXIT_CODE -eq 0 ]; then
    echo "Compilation successful" >&2
    echo "Generated files:" >&2
    ls -la "/app/client-bytecode/$EXEC_ID/" >&2
else
    echo "Compilation failed" >&2
    echo "Error log contents:" >&2
    cat "/app/error-log/$EXEC_ID/err.log" >&2
fi

exit $COMPILATION_EXIT_CODE