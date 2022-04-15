package org.example.databench.lib.loader;

public class ClassLoaderCallBackMethod {

    public static <M> M callbackAndReset(CallBack<M> callBack, ClassLoader toSetClassLoader, boolean reset) throws Exception {
        ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(toSetClassLoader);
            return callBack.execute();
        } finally {
            if (reset) {
                Thread.currentThread().setContextClassLoader(oldClassLoader);
            }
        }
    }

    @FunctionalInterface
    public interface CallBack<T> {
        T execute() throws Exception;
    }

}
