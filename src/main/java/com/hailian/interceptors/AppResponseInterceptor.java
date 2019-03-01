package com.hailian.interceptors;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.alibaba.fastjson.JSON;
import com.hailian.result.ResultDO;

/**
 * @Description: 统一响应结果处理
 * Author lv bin
 * @date 2017/3/17 10:45
 * version V1.0.0
 */
@ControllerAdvice
public class AppResponseInterceptor implements ResponseBodyAdvice {

    private Logger logger = LoggerFactory.getLogger(AppResponseInterceptor.class);

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {

        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass,
                                  ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        // 响应结果执行
        if(mediaType != null && o != null
                && mediaType.includes(MediaType.APPLICATION_JSON)
                && o instanceof ResultDO){

            if(serverHttpRequest instanceof ServletServerHttpRequest){

                ServletServerHttpRequest request = (ServletServerHttpRequest)serverHttpRequest;

                HttpServletRequest httpServletRequest = request.getServletRequest();

               /* Date requestTime = (Date) httpServletRequest.getAttribute(AppInterceptors.REQUEST_TIME);

                long useTime = System.currentTimeMillis() - requestTime.getTime();*/

                Method method = methodParameter.getMethod();

                logger.debug("request controller:" + method.getDeclaringClass() + " request method:" + method.getName());

                logger.debug("request link:" + serverHttpRequest.getURI() );
            }


            logger.debug("response content:" + JSON.toJSONString(o));
        }

        return o;
    }
}
