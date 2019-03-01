package com.hailian.base;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
/**
 * @time   2018年9月26日 下午5:40:11
 * @author zuoqb
 * @todo   公用ServiceImpl
 */
public class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T>{

}
