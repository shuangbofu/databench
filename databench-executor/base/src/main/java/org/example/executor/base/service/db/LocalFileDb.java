package org.example.executor.base.service.db;

import org.example.databench.lib.utils.JSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Created by shuangbofu on 2022/4/15 22:19
 */
public class LocalFileDb implements Db {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalFileDb.class);
    private static final String CACHE_DIR = "/tmp/databench/";
    private final String jobId;

    public LocalFileDb(String jobId) {
        this.jobId = jobId;
    }

    @Override
    public <T> Optional<T> get(String key, Class<T> clazz) {
        try {
            String str = Files.readString(Path.of(CACHE_DIR + jobId + "." + key));
            if (clazz != null && String.class.isAssignableFrom(clazz)) {
                return Optional.ofNullable((T) str);
            }
            return Optional.ofNullable(JSONUtils.parseObject(str, clazz));
        } catch (Exception e) {
            LOGGER.error("Get " + key + " error", e);
        }
        return Optional.empty();
    }

    @Override
    public void set(String key, Object value) {
        try {
            String content;
            if (value instanceof String) {
                content = value.toString();
            } else {
                content = JSONUtils.toJSONString(value);
            }
            Files.writeString(Path.of(CACHE_DIR + jobId + "." + key), content);
        } catch (IOException e) {
            LOGGER.error("Write " + key + " error", e);
        }
    }
}
