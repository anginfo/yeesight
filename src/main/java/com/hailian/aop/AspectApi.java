package com.hailian.aop;

import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author zuoqb
 * @since on 2018/5/10.
 */
public interface AspectApi {
     Object doHandlerAspect(Object [] obj , ProceedingJoinPoint pjp, Method method,boolean isAll)throws Throwable;
}
