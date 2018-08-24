package com.duol.exception;

import com.duol.common.ServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一异常处理
 *
 * @author Duolaimon
 * 18-8-18 下午1:19
 */
@RestControllerAdvice("com.duol.controller")
public class WebExceptionHandle {
    private static final Logger logger = LoggerFactory.getLogger(WebExceptionHandle.class);


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ServerResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException e){
        logger.error("参数解析失败", e);
        return ServerResponse.createByErrorMessage("could_not_read_json");
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ServerResponse handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        logger.error("不支持当前请求方法", e);
        return ServerResponse.createByErrorMessage("request_method_not_supported");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ServerResponse handlerException(Exception e) {
        //todo 加自定义异常
        logger.error("服务器异常",e);
        return ServerResponse.createByErrorMessage("server_error");
    }
}
