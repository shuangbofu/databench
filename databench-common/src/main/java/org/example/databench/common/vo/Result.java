package org.example.databench.common.vo;

import lombok.Data;

/**
 * Created by shuangbofu on 2021/9/11 1:22 下午
 */
@Data
public class Result<T> {
    private T data;
    private boolean success;
    private String msg;
    private int errorCode;

    public Result(T data, boolean success, String msg, int errorCode) {
        this.data = data;
        this.success = success;
        this.msg = msg;
        this.errorCode = errorCode;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(data, true, "", 0);
    }

    public static <T> Result<T> error(String msg, int errorCode) {
        return new Result<>(null, false, msg, errorCode);
    }
}
