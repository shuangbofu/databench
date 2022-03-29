package org.example.databench.web.aspect;

import org.example.databench.common.utils.JSONUtils;
import org.example.databench.common.vo.Result;
import org.example.databench.web.annotations.ResultBody;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

@ControllerAdvice
public class ResultBodyAdvice implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return Objects.nonNull(returnType.getMethod()) &&
                (AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ResultBody.class) ||
                        returnType.hasMethodAnnotation(ResultBody.class));
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class selectedConverterType, ServerHttpRequest request,
                                  ServerHttpResponse response) {
        if (body instanceof String) {
            return JSONUtils.toJSONString(Result.success(body));
        }
        return body instanceof Result ? body : Result.success(body);
    }
}
