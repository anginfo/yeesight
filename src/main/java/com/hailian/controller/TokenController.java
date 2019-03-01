package com.hailian.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hailian.annotation.AuthPower;
import com.hailian.base.BaseController;
import com.hailian.common.PublicResult;
import com.hailian.common.TokenConstants;
import com.hailian.entity.SysUser;
import com.hailian.enums.PlatformType;
import com.hailian.enums.PublicResultConstant;
import com.hailian.service.ISysUserService;
import com.hailian.utils.JWTUtil;
import com.hailian.utils.MD5Util;

/**
 * @time   2018年9月26日 下午10:43:32
 * @author zuoqb
 * @todo   Token相关
 */
@RestController
@RequestMapping("/api/{version}")
@Api(value = "Token相关",description="Token相关 @author zuoqb")
public class TokenController extends BaseController{
	@Autowired
	public ISysUserService iSysUserService;
    
	/**
	 * @time   2018年9月30日 下午4:47:24
	 * @author zuoqb
	 * @todo   获取登陆token
	 */
    @ApiOperation(value = "获取用户access_token",notes = "获取用户access_token",httpMethod = "POST")
    @AuthPower(avoidLogin = true,avoidPower = true, avoidVersion = true, avoidSign = true)
    @PostMapping("/accessToken")
    public PublicResult<SysUser> accessToken(@RequestParam("loginName")String loginName,@RequestParam("password")String password,
                                  @RequestHeader(TokenConstants.X_PLATFORM)String platform,
                                  HttpServletRequest request,HttpServletResponse response) {
    	if(StringUtils.isBlank(loginName)){
    		return new PublicResult<>(PublicResultConstant.INVALID_PARAM_EMPTY,"用户名不能为空!", null);
    	}
    	if(StringUtils.isBlank(password)){
    		return new PublicResult<>(PublicResultConstant.INVALID_PARAM_EMPTY,"密码不能为空!", null);
    	}
    	EntityWrapper<SysUser> wrapper = new EntityWrapper<SysUser>();
		wrapper.where("del_flag={0}", UN_DEL_FLAG);
		wrapper.eq("login_name", loginName);
		SysUser entity=iSysUserService.selectOne(wrapper);
		if(entity==null){
			return new PublicResult<>(PublicResultConstant.FAILED,"该用户不存在!", null);
		}
		//校验密码
		if(!MD5Util.getMd5(password).equals(entity.getPassword())){
			return new PublicResult<>(PublicResultConstant.INVALID_PARAM_EMPTY,"密码错误!", null);
		}
		//
		String accessToken=JWTUtil.generateToken(entity.getId(),entity.getPassword(), PlatformType.getTypeByPlatform(platform));
		entity.setAccessToken(accessToken);
		setSessionUser(request, response, entity);
		return new PublicResult<>(PublicResultConstant.SUCCESS,entity);
    }
}
