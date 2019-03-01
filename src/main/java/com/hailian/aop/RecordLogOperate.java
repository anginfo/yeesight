package com.hailian.aop;

/**
 * @author zuoqb
 * @since on 2018/5/10.
 */
public class RecordLogOperate extends AspectHandler{
    @Override
    protected RecordLog factoryMethod() {
        return new RecordLog();
    }
}
