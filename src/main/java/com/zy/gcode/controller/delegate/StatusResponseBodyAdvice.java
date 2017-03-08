package com.zy.gcode.controller.delegate;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * Created by admin5 on 17/3/8.
 */
//@ControllerAdvice
public class StatusResponseBodyAdvice implements ResponseBodyAdvice {
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        System.out.println("returnType:"+returnType);
        System.out.println("converterType:"+converterType);
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        System.out.println("body:"+body);
        System.out.println("returnType:"+returnType);
        System.out.println("selectedContentType:"+selectedContentType);
        System.out.println("selectedConverterType:"+selectedConverterType);
        System.out.println("request:"+request);
        return body;
    }
}
