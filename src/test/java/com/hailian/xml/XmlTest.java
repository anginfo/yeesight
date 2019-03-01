package com.hailian.xml;

import com.hailian.SpringbootexampleApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hailian.xml.HelloService;

/**
 * @Package com.hailian.xml.XmlTest
 * @Copyright: Copyright (c) 2016
 * Author lv bin
 * @date 2017/5/18 15:40
 * version V1.0.0
 */
public class XmlTest extends SpringbootexampleApplicationTests {

    @Autowired
    private HelloService helloService;

    @Test
    public void testSay(){
        helloService.say("你好吗？");
    }
}
