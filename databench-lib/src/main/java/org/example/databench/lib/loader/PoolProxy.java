package org.example.databench.lib.loader;


import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by shuangbofu on 2022/4/14 21:17
 */
public class PoolProxy<T> {

    private final T api;
    private final ThreadPoolExecutor pool;
    private int timeout;

    public PoolProxy(T api) {
        this.api = api;
        pool = new ThreadPoolExecutor(10, 10, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                new ThreadFactoryBuilder()
                        .setNameFormat(api.getClass().getSimpleName() + "_" + getClass().getSimpleName() + "%d").build());
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public <R> R invokeRt(Function<T, R> executeMethod) {
        try {
            return CompletableFuture.supplyAsync(() -> {
                try {
                    return ClassLoaderCallBackMethod.callbackAndReset(() -> executeMethod.apply(api),
                            api.getClass().getClassLoader(), true);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, pool).get(timeout, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    public void invoke(Consumer<T> consumer) {
        invokeRt(i -> {
            consumer.accept(i);
            return true;
        });
    }
}
