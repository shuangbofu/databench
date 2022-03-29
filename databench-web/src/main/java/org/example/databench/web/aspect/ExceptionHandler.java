package org.example.databench.web.aspect;

import org.example.databench.common.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.OK;

@ControllerAdvice
@ResponseBody
public class ExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandler.class);

    @ResponseStatus(OK)
    @org.springframework.web.bind.annotation.ExceptionHandler(RuntimeException.class)
    public Result<?> runtime(RuntimeException e) {
        LOGGER.warn("Error happened", e);
        return Result.error(e.getMessage(), -1);
    }

    @ResponseStatus(OK)
    @org.springframework.web.bind.annotation.ExceptionHandler(Throwable.class)
    public Result<?> throwable(Throwable e) {
        LOGGER.error("Throwable happened", e);
        return Result.error(e.getMessage(), -1);
    }
}
