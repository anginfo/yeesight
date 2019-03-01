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
import com.hailian.entity.CommonInterfaceExcelSheet;
import com.hailian.enums.PublicResultConstant;
import com.hailian.service.ICommonInterfaceExcelSheetService;
/**
 *
 * @date 2018-10-11
 * @author zuoqb123
 * @todo Excel导出sheet也配置表路由
 */
@Controller
@Api(value = "Excel导出sheet也配置表",description="Excel导出sheet也配置表 @author zuoqb123")
public class CommonInterfaceExcelSheetController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(CommonInterfaceExcelSheetController.class);

    @Autowired
    public ICommonInterfaceExcelSheetService iCommonInterfaceExcelSheetService;

    /**
     * @date   2018-10-11
     * @author zuoqb123
     * @todo   新增Excel导出sheet也配置表
     */
    @ResponseBody
 	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@ApiOperation(value = "新增Excel导出sheet也配置表", notes = "新增Excel导出sheet也配置表", httpMethod = "POST")
	@RequestMapping(value = "/api/v1/commonInterfaceExcelSheet/add", method = RequestMethod.POST)
	public PublicResult<CommonInterfaceExcelSheet> add(HttpServletRequest request,CommonInterfaceExcelSheet entity) {
		try {
			if(StringUtils.isBlank(entity.getId())){
				//新增
				entity.setCreateBy(getLoginUser(request).getId());
				if(iCommonInterfaceExcelSheetService.saveOrUpdate(entity)){
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
     * @date   2018-10-11
     * @author zuoqb123
     * @todo   删除Excel导出sheet也配置表
     */
    @ResponseBody
 	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@ApiOperation(value = "删除Excel导出sheet也配置表", notes = "删除Excel导出sheet也配置表", httpMethod = "DELETE")
    @RequestMapping(value = "/api/v1/commonInterfaceExcelSheet/delete/{id}", method = RequestMethod.DELETE)
	public PublicResult<CommonInterfaceExcelSheet> delete(HttpServletRequest request,@PathVariable("id") String id) {
		try {
			 if(iCommonInterfaceExcelSheetService.deleteLogic(id)){
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
     * @date   2018-10-11
     * @author zuoqb123
     * @todo   更新Excel导出sheet也配置表
     */
    @ResponseBody
 	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@ApiOperation(value = "更新Excel导出sheet也配置表", notes = "更新Excel导出sheet也配置表", httpMethod = "PUT")
	@RequestMapping(value = "/api/v1/commonInterfaceExcelSheet/update", method = RequestMethod.PUT)
	public PublicResult<CommonInterfaceExcelSheet> update(HttpServletRequest request,CommonInterfaceExcelSheet entity) {
		try {
			if(entity!=null&&StringUtils.isNotBlank(entity.getId())){
				//更新
				entity.setUpdateBy(getLoginUser(request).getId());
				if(iCommonInterfaceExcelSheetService.saveOrUpdate(entity)){
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
     * @date   2018-10-11
     * @author zuoqb123
     * @todo   查询单个Excel导出sheet也配置表
     */
    @ResponseBody
 	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@ApiOperation(value = "查询单个Excel导出sheet也配置表", notes = "查询单个Excel导出sheet也配置表", httpMethod = "GET")
  	@RequestMapping(value = "/api/v1/commonInterfaceExcelSheet/get/{id}", method = RequestMethod.GET )
  	public PublicResult<CommonInterfaceExcelSheet> get(HttpServletRequest request,@PathVariable("id") String id) {
  		try {
  			return new PublicResult<>(PublicResultConstant.SUCCESS, iCommonInterfaceExcelSheetService.getById(id));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new PublicResult<>(PublicResultConstant.FAILED,e.getMessage(), null);
		}
		
  	}
	
    /**
     * @date   2018-10-11
     * @author zuoqb123
     * @todo   分页查询Excel导出sheet也配置表
     */
    @ResponseBody
 	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@ApiOperation(value = "分页查询Excel导出sheet也配置表", notes = "分页查询Excel导出sheet也配置表", httpMethod = "GET")
  	@RequestMapping(value = "/api/v1/commonInterfaceExcelSheet/list", method = RequestMethod.GET)
    public PublicResult<PageInfo<CommonInterfaceExcelSheet>> list(CommonInterfaceExcelSheet entity,@RequestParam(value="pageNum",required = false,defaultValue="1") Integer pageNum,
			@RequestParam(value="pageSize",required = false,defaultValue="10") Integer pageSize,HttpServletRequest request) {
		try {
			PageInfo<CommonInterfaceExcelSheet> page=iCommonInterfaceExcelSheetService.pageList(this, request, entity, pageNum, pageSize);
			return new PublicResult<>(PublicResultConstant.SUCCESS, page);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new PublicResult<>(PublicResultConstant.FAILED,e.getMessage(),null);
		}

	}
    /**
     * @date   2018-10-11
     * @author zuoqb123
     * @todo   分页查询Excel导出sheet详情配置
     */
    @ResponseBody
 	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@ApiOperation(value = "根据Excel编码查询sheet详情配置", notes = "根据Excel编码查询sheet详情配置", httpMethod = "GET")
  	@RequestMapping(value = "/api/v1/commonInterfaceExcelSheet/getSheetByTableId", method = RequestMethod.GET)
    public PublicResult<List<CommonInterfaceExcelSheet>> getSheetByTableId(@RequestParam(value="tableId",required = true) String tableId,
			@RequestParam(value="sortBy",required = false,defaultValue="order_no") String sortBy,@RequestParam(value="isAsc",required = false,defaultValue="true") boolean isAsc) {
		try {
			List<CommonInterfaceExcelSheet> sheets=iCommonInterfaceExcelSheetService.getSheetByTableId(tableId, sortBy, isAsc);
			return new PublicResult<>(PublicResultConstant.SUCCESS, sheets);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new PublicResult<>(PublicResultConstant.FAILED,e.getMessage(),null);
		}

	}
  
}

