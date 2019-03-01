package com.hailian.controller;

import io.swagger.annotations.Api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.hailian.annotation.AuthPower;
import com.hailian.base.BaseController;
import com.hailian.domain.User;
import com.hailian.result.ResultDO;

/**
 * @time   2018年9月26日 下午10:43:18
 * @author zuoqb
 * @todo   权限相关测试
 */
@RestController
@RequestMapping("/api/stream/{version}")
@Api(value = "权限相关测试",description="权限相关测试 @author zuoqb")
public class StreamController extends BaseController{

    /**
     * 免登陆
     */
    @AuthPower(avoidLogin = true)
    @PostMapping("/login")
    public ResultDO<String> login(@RequestBody String name){
        ResultDO<String> resultDO = new ResultDO<>();
        resultDO.setData(name);
        return resultDO;
    }

    /**
     * 免版本号校验
     * @param id
     * @return
     */
    @AuthPower(avoidVersion = true)
    @GetMapping("/get/{id}")
    public ResultDO<String> get(@PathVariable Long id){
        ResultDO<String> resultDO = new ResultDO<>();
        resultDO.setData(String.valueOf(id));
        return resultDO;
    }


    /**
     * 免参数加密校验
     * @param user
     * @return
     */
    @AuthPower(avoidSign = true)
    @PostMapping("/save")
    public ResultDO<String> save(@RequestBody User user){

        System.out.println("===============" + JSON.toJSONString(user));
        ResultDO<String> resultDO = new ResultDO<>();
        resultDO.setData(JSON.toJSONString(user));
        return resultDO;
    }

    /**
     * 需要校验用户是否有权访问此接口
     * @param user
     * @return
     */
    @AuthPower(avoidVersion = true,avoidPower = true,avoidSign = true,avoidLogin = true)
    @RequestMapping(value ="/update", method = RequestMethod.GET)
    public ResultDO<String> power(){
        ResultDO<String> resultDO = new ResultDO<>();
        resultDO.setData(JSON.toJSONString(null));
        return resultDO;
    }

    /**
     * 测试Token
     * @param id
     * @return
     */
    @AuthPower(avoidVersion = true,avoidPower = true,avoidSign = true,avoidLogin = false)
    @RequestMapping(value = "delete.do",method = RequestMethod.POST)
    public ResultDO<String> detail(Long id){
        ResultDO<String> resultDO = new ResultDO<>();
        resultDO.setSuccess(true);

        return resultDO;
    }

}
