package org.example.databench.executor.script.utils;

import java.io.IOException;

public class ShellException extends IOException {
    private static final long serialVersionUID = -1473387065888310258L;

    public ShellException() {
    }

    public ShellException(String message) {
        super(message);
    }

    public ShellException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShellException(Throwable cause) {
        super(cause);
    }
}
