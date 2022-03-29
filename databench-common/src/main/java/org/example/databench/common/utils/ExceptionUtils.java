package org.example.databench.common.utils;


import org.example.databench.common.utils.function.Runnable;
import org.example.databench.common.utils.function.Supplier;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtils {

    public static void roundEx(Runnable runnable, String msg) {
        try {
            runnable.run();
        } catch (Exception e) {
            throw new RuntimeException(msg, e);
        }
    }

    public static <T> T roundExRt(Supplier<T> supplier, String msg) {
        return roundExRt(supplier, msg, () -> {
        });
    }

    public static <T> T roundExRtNull(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T roundExRt(Supplier<T> supplier, String msg, Runnable runnable) {
        try {
            return supplier.get();
        } catch (Exception e) {
            throw new RuntimeException(msg, e);
        } finally {
            try {
                if (runnable != null) {
                    runnable.run();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();

        try (PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            return sw.toString();
        }
    }

}
