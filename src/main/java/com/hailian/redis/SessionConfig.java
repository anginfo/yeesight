package com.hailian.redis;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @Package com.hailian.redis.SessionConfig
 * @Copyright: Copyright (c) 2016
 * Author lv bin
 * @date 2017/5/18 14:14
 * version V1.0.0
 */
//使用Redis把下面注解放开
/*@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 86400*30)*/
public class SessionConfig {
    // maxInactiveIntervalInSeconds: 设置Session失效时间，使用Redis Session之后，原Boot的server.session.timeout属性不再生效
}
