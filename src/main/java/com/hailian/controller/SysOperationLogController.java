package com.hailian.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

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
import com.hailian.entity.SysOperationLog;
import com.hailian.enums.PublicResultConstant;
import com.hailian.service.ISysOperationLogService;
/**
 *
 * @date 2018-10-09
 * @author zuoqb123
 * @todo 操作日志路由
 */
@Controller
@Api(value = "操作日志",description="操作日志 @author zuoqb123")
public class SysOperationLogController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(SysOperationLogController.class);

    @Autowired
    public ISysOperationLogService iSysOperationLogService;

    /**
     * @date   2018-10-09
     * @author zuoqb123
     * @todo   新增操作日志
     */
    @ResponseBody
 	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@ApiOperation(value = "新增操作日志", notes = "新增操作日志", httpMethod = "POST")
	@RequestMapping(value = "/api/v1/sysOperationLog/add", method = RequestMethod.POST)
	public PublicResult<SysOperationLog> add(HttpServletRequest request,SysOperationLog entity) {
		try {
			if(StringUtils.isBlank(entity.getId())){
				//新增
				entity.setCreateBy(getLoginUser(request).getId());
				if(iSysOperationLogService.saveOrUpdate(entity)){
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
     * @todo   删除操作日志
     */
    @ResponseBody
 	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@ApiOperation(value = "删除操作日志", notes = "删除操作日志", httpMethod = "DELETE")
    @RequestMapping(value = "/api/v1/sysOperationLog/delete/{id}", method = RequestMethod.DELETE)
	public PublicResult<SysOperationLog> delete(HttpServletRequest request,@PathVariable("id") String id) {
		try {
			 if(iSysOperationLogService.deleteLogic(id)){
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
     * @todo   更新操作日志
     */
    @ResponseBody
 	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@ApiOperation(value = "更新操作日志", notes = "更新操作日志", httpMethod = "PUT")
	@RequestMapping(value = "/api/v1/sysOperationLog/update", method = RequestMethod.PUT)
	public PublicResult<SysOperationLog> update(HttpServletRequest request,SysOperationLog entity) {
		try {
			if(entity!=null&&StringUtils.isNotBlank(entity.getId())){
				//更新
				entity.setUpdateBy(getLoginUser(request).getId());
				if(iSysOperationLogService.saveOrUpdate(entity)){
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
     * @todo   查询单个操作日志
     */
    @ResponseBody
 	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@ApiOperation(value = "查询单个操作日志", notes = "查询单个操作日志", httpMethod = "GET")
  	@RequestMapping(value = "/api/v1/sysOperationLog/get/{id}", method = RequestMethod.GET )
  	public PublicResult<SysOperationLog> get(HttpServletRequest request,@PathVariable("id") String id) {
  		try {
  			return new PublicResult<>(PublicResultConstant.SUCCESS, iSysOperationLogService.getById(id));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new PublicResult<>(PublicResultConstant.FAILED,e.getMessage(), null);
		}
		
  	}
	
    /**
     * @date   2018-10-09
     * @author zuoqb123
     * @todo   分页查询操作日志
     */
    @ResponseBody
 	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@ApiOperation(value = "分页查询操作日志", notes = "分页查询操作日志", httpMethod = "GET")
  	@RequestMapping(value = "/api/v1/sysOperationLog/list", method = RequestMethod.GET)
    public PublicResult<PageInfo<SysOperationLog>> list(SysOperationLog entity,@RequestParam(value="pageNum",required = false,defaultValue="1") Integer pageNum,
			@RequestParam(value="pageSize",required = false,defaultValue="10") Integer pageSize,HttpServletRequest request) {
		try {
			PageInfo<SysOperationLog> page=iSysOperationLogService.pageList(this, request, entity, pageNum, pageSize);
			return new PublicResult<>(PublicResultConstant.SUCCESS, page);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new PublicResult<>(PublicResultConstant.FAILED,e.getMessage(),null);
		}

	}
    
    
    /**
     * @date   2018-10-09
     * @author zuoqb123
     * @todo   分页查询操作日志
     */
    @ResponseBody
 	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@ApiOperation(value = "分页查询操作日志", notes = "分页查询操作日志", httpMethod = "GET")
  	@RequestMapping(value = "/api/v1/sysOperationLog/list2", method = RequestMethod.GET)
    public PublicResult<List<SysOperationLog>> list2() {
		try {
			List<SysOperationLog> page=iSysOperationLogService.dateList();
			return new PublicResult<>(PublicResultConstant.SUCCESS, page);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new PublicResult<>(PublicResultConstant.FAILED,e.getMessage(),null);
		}

	}
  
}

