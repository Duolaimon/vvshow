package com.duol.aop;

import java.lang.annotation.*;

/**
 * @author Duolaimon
 * 18-8-17 下午9:35
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ControllerLog {
    String description() default "";

}
