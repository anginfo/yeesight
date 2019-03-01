package com.hailian.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.hailian.annotation.AuthPower;
import com.hailian.base.BaseController;
import com.hailian.common.PublicResult;
import com.hailian.entity.CommonInterfaceExc;
import com.hailian.enums.PublicResultConstant;
import com.hailian.service.ICommonInterfaceExcService;
/**
 *
 * @date 2018-10-09
 * @author zuoqb123
 * @todo 统一接口路由
 */
@Controller
@Api(value = "统一接口",description="统一接口 @author zuoqb123")
public class CommonInterfaceExcController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(CommonInterfaceExcController.class);

    @Autowired
    public ICommonInterfaceExcService iCommonInterfaceExcService;

    /**
     * @date   2018-10-09
     * @author zuoqb123
     * @todo   新增统一接口
     */
    @ResponseBody
 	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@ApiOperation(value = "新增统一接口", notes = "新增统一接口", httpMethod = "POST")
	@RequestMapping(value = "/api/v1/commonInterfaceExc/add", method = RequestMethod.POST)
	public PublicResult<CommonInterfaceExc> add(HttpServletRequest request,CommonInterfaceExc entity) {
		try {
			if(StringUtils.isBlank(entity.getId())){
				//新增
				entity.setCreateBy(getLoginUser(request).getId());
				if(iCommonInterfaceExcService.saveOrUpdate(entity)){
					return new PublicResult<>(PublicResultConstant.SUCCESS, entity);
				}else{
					return new PublicResult<>(PublicResultConstant.ERROR, null);
				}
			}else{
				return new PublicResult<>(PublicResultConstant.PARAM_ERROR, "新增主键必须为空!",null);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new PublicResult<>(PublicResultConstant.FAILED,e.getMessage(), null);
		}
	}
    
    /**
     * @date   2018-10-09
     * @author zuoqb123
     * @todo   删除统一接口
     */
    @ResponseBody
 	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@ApiOperation(value = "删除统一接口", notes = "删除统一接口", httpMethod = "DELETE")
    @RequestMapping(value = "/api/v1/commonInterfaceExc/delete/{id}", method = RequestMethod.DELETE)
	public PublicResult<CommonInterfaceExc> delete(HttpServletRequest request,@PathVariable("id") String id) {
		try {
			 if(iCommonInterfaceExcService.deleteLogic(id)){
				 return new PublicResult<>(PublicResultConstant.SUCCESS, null);
			 }else{
				 return new PublicResult<>(PublicResultConstant.ERROR, null);
			 }
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new PublicResult<>(PublicResultConstant.FAILED,e.getMessage(), null);
		}
	}
	
	 /**
     * @date   2018-10-09
     * @author zuoqb123
     * @todo   更新统一接口
     */
    @ResponseBody
 	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@ApiOperation(value = "更新统一接口", notes = "更新统一接口", httpMethod = "PUT")
	@RequestMapping(value = "/api/v1/commonInterfaceExc/update", method = RequestMethod.PUT)
	public PublicResult<CommonInterfaceExc> update(HttpServletRequest request,CommonInterfaceExc entity) {
		try {
			if(entity!=null&&StringUtils.isNotBlank(entity.getId())){
				//更新
				entity.setUpdateBy(getLoginUser(request).getId());
				if(iCommonInterfaceExcService.saveOrUpdate(entity)){
					return new PublicResult<>(PublicResultConstant.SUCCESS, entity);
				}else{
					return new PublicResult<>(PublicResultConstant.ERROR, null);
				}
			}else{
				return new PublicResult<>(PublicResultConstant.PARAM_ERROR, "修改主键不能为空!",null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new PublicResult<>(PublicResultConstant.FAILED,e.getMessage(), null);
		}
	}
    
    
    /**
     * @date   2018-10-09
     * @author zuoqb123
     * @todo   查询单个统一接口
     */
    @ResponseBody
 	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@ApiOperation(value = "查询单个统一接口", notes = "查询单个统一接口", httpMethod = "GET")
  	@RequestMapping(value = "/api/v1/commonInterfaceExc/get/{id}", method = RequestMethod.GET )
  	public PublicResult<CommonInterfaceExc> get(HttpServletRequest request,@PathVariable("id") String id) {
  		try {
  			return new PublicResult<>(PublicResultConstant.SUCCESS, iCommonInterfaceExcService.getById(id));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new PublicResult<>(PublicResultConstant.FAILED,e.getMessage(), null);
		}
		
  	}
    /**
     * @date   2018-10-09
     * @author zuoqb123
     * @todo   分页查询统一接口
     */
    @ResponseBody
 	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@ApiOperation(value = "分页查询统一接口", notes = "分页查询统一接口", httpMethod = "GET")
  	@RequestMapping(value = "/api/v1/commonInterfaceExc/list", method = RequestMethod.GET)
    public PublicResult<PageInfo<CommonInterfaceExc>> list(CommonInterfaceExc entity,@RequestParam(value="pageNum",required = false,defaultValue="1") Integer pageNum,
			@RequestParam(value="pageSize",required = false,defaultValue="10") Integer pageSize,HttpServletRequest request) {
		try {
			PageInfo<CommonInterfaceExc> page=iCommonInterfaceExcService.pageList(this, request, entity, pageNum, pageSize);
			return new PublicResult<>(PublicResultConstant.SUCCESS, page);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new PublicResult<>(PublicResultConstant.FAILED,e.getMessage(),null);
		}

	}
  
}

