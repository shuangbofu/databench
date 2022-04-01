package org.example.databench.executor.script.executor;

import org.apache.commons.exec.TimeoutObserver;
import org.apache.commons.exec.Watchdog;
import org.apache.commons.exec.util.DebugUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * copy from apache commons exec
 * 1.fix a bug --> Line 101
 * 2.add timeout flag --> Line 93
 * 3.When using sudo, use command to kill --> Line 98
 */
public class ExecuteWatchdog extends org.apache.commons.exec.ExecuteWatchdog implements TimeoutObserver {
    public static final long INFINITE_TIMEOUT = -1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecuteWatchdog.class);
    private final boolean hasWatchdog;
    private final Watchdog watchdog;
    private final String sudoUser;
    private Process process;
    private boolean watch = false;
    private Exception caught;
    private boolean killedProcess = false;
    private boolean timeout = false;
    private volatile boolean processStarted;

    public ExecuteWatchdog(long timeout, String sudoUser) {
        super(timeout);
        hasWatchdog = timeout != -1L;
        this.sudoUser = sudoUser;
        processStarted = false;
        if (hasWatchdog) {
            watchdog = new Watchdog(timeout);
            watchdog.addTimeoutObserver(this);
        } else {
            watchdog = null;
        }

    }

    @Override
    public synchronized void start(Process processToMonitor) {
        if (processToMonitor == null) {
            throw new NullPointerException("process is null.");
        } else if (process != null) {
            throw new IllegalStateException("Already running.");
        } else {
            caught = null;
            killedProcess = false;
            watch = true;
            process = processToMonitor;
            processStarted = true;
            notifyAll();
            if (hasWatchdog) {
                watchdog.start();
            }

        }
    }

    @Override
    public synchronized void stop() {
        if (hasWatchdog) {
            watchdog.stop();
        }

        watch = false;
        process = null;
    }

    @Override
    public synchronized void destroyProcess() {
        ensureStarted();
        timeoutOccured(null);
        stop();
    }

    @Override
    public synchronized void timeoutOccured(Watchdog w) {
        try {
            try {
                if (process != null) {
                    process.exitValue();
                }
            } catch (IllegalThreadStateException var7) {
                if (watch) {
                    killedProcess = true;
                    // Add
                    if (w != null) {
                        // Add timeout flag
                        timeout = true;
                    }
                    ProcessHandle processHandle = process.toHandle();
                    if (sudoUser != null && !sudoUser.isEmpty()) {
                        killWithCommandByPid(processHandle.pid());
                        processHandle.children().map(ProcessHandle::pid).forEach(this::killWithCommandByPid);
                    } else {
                        // Original code :
                        // this.process.destroy();

                        // Now:
                        /*
                            After executing this method process.destroy(), there may be two cases in which executor does not throw an exception on some machines:
                                1.kill does not take effect, and the process is still running in the background, such as when executing spark-sql.
                                2.kill takes effect, but the exit code is 0, such as when executing ssh
                            The reason has not been found yet, so destroyForcibly method is required.
                        */
                        processHandle.destroyForcibly();
                        processHandle.children().forEach(ProcessHandle::destroyForcibly);
                    }
                }
            }
        } catch (Exception var8) {
            caught = var8;
            DebugUtils.handleException("Getting the exit value of the process failed", var8);
        } finally {
            cleanUp();
        }

    }

    private void killWithCommandByPid(long pid) {
        try {
            LOGGER.info("User {} kill process, pid {}", sudoUser, pid);
            Runtime.getRuntime().exec(String.format("sudo -iu %s kill -9 %s", sudoUser, pid));
        } catch (IOException ignored) {
        }
    }

    @Override
    public synchronized void checkException() throws Exception {
        if (caught != null) {
            throw caught;
        }
    }

    @Override
    public synchronized boolean isWatching() {
        ensureStarted();
        return watch;
    }

    public synchronized boolean isTimeout() {
        return timeout;
    }

    @Override
    public synchronized boolean killedProcess() {
        return killedProcess;
    }

    @Override
    protected synchronized void cleanUp() {
        watch = false;
        process = null;
    }

    void setProcessNotStarted() {
        processStarted = false;
    }

    private void ensureStarted() {
        while (!processStarted) {
            try {
                wait();
            } catch (InterruptedException var2) {
                throw new RuntimeException(var2.getMessage());
            }
        }
    }
}
