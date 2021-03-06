package com.duol.aop;

import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Duolaimon
 * 18-8-17 下午9:38
 */
@Aspect
@Component
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);
    private static ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Pointcut("@annotation(io.swagger.annotations.ApiOperation)")
    public void controllerPoint() {
    }


    @Before("controllerPoint()")
    public void doAfter(JoinPoint joinPoint) {
        executorService.submit(() -> {
            StringBuilder parameters = new StringBuilder();
            String value = getControllerMethodValue(joinPoint,parameters);
            logger.info("调用'{}'方法,{}", value, parameters);
        });
    }


    // 通过反射获取参入的参数
    private String getControllerMethodValue(JoinPoint joinPoint, StringBuilder parameterString) {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = null;
        try {
            targetClass = Class.forName(targetName);
        } catch (ClassNotFoundException e) {
            logger.error("获取目标类失败", e);
        }
        String value = "";

        assert targetClass != null;
        Method[] methods = targetClass.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Parameter[] parameters = method.getParameters();
                if (parameters.length == arguments.length) {
                    for (int i = 0; i < arguments.length; i++) {
                        if (arguments[i].toString().contains("org.apache.catalina"))
                            continue;
                        parameterString
                                .append(parameters[i].getName())
                                .append("=")
                                .append(arguments[i].toString())
                                .append(",");
                    }

                    value = method.getAnnotation(ApiOperation.class).value();
                    break;
                }
            }
        }
        return value;
    }
}
