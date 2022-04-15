package org.example.databench.executor.script.executor;

/**
 * Command line execute result
 */
public class ExecResult {
    private final int exitCode;
    private final Exception exception;
    private final Code code;

    private ExecResult(int exitCode, Code code, Exception e) {
        this.exitCode = exitCode;
        exception = e;
        this.code = code;
    }

    public static ExecResult success(int exitCode) {
        return new ExecResult(exitCode, Code.SUCCESS, null);
    }

    public static ExecResult error(int exitCode, Exception e) {
        return new ExecResult(exitCode, Code.ERROR, e);
    }

    public static ExecResult interrupt(int exitCode, Exception e) {
        return new ExecResult(exitCode, Code.INTERRUPT, e);
    }

    public int getExitCode() {
        return exitCode;
    }

    public boolean isSuccess() {
        return Code.SUCCESS.equals(code);
    }

    public Exception getException() {
        return exception;
    }

    public Code getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "ExecResult{" +
                "exitCode=" + exitCode +
                ", exception=" + exception +
                ", code=" + code +
                '}';
    }

    enum Code {
        SUCCESS, INTERRUPT, ERROR
    }
}
