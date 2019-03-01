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
import com.hailian.entity.CommonInterfaceExcelSheetContent;
import com.hailian.enums.PublicResultConstant;
import com.hailian.service.ICommonInterfaceExcelSheetContentService;
/**
 *
 * @date 2018-10-11
 * @author zuoqb123
 * @todo Excel导出sheet详情配置路由
 */
@Controller
@Api(value = "Excel导出sheet详情配置",description="Excel导出sheet详情配置 @author zuoqb123")
public class CommonInterfaceExcelSheetContentController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(CommonInterfaceExcelSheetContentController.class);

    @Autowired
    public ICommonInterfaceExcelSheetContentService iCommonInterfaceExcelSheetContentService;

    /**
     * @date   2018-10-11
     * @author zuoqb123
     * @todo   新增Excel导出sheet详情配置
     */
    @ResponseBody
 	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@ApiOperation(value = "新增Excel导出sheet详情配置", notes = "新增Excel导出sheet详情配置", httpMethod = "POST")
	@RequestMapping(value = "/api/v1/commonInterfaceExcelSheetContent/add", method = RequestMethod.POST)
	public PublicResult<CommonInterfaceExcelSheetContent> add(HttpServletRequest request,CommonInterfaceExcelSheetContent entity) {
		try {
			if(StringUtils.isBlank(entity.getId())){
				//新增
				entity.setCreateBy(getLoginUser(request).getId());
				if(iCommonInterfaceExcelSheetContentService.saveOrUpdate(entity)){
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
     * @todo   删除Excel导出sheet详情配置
     */
    @ResponseBody
 	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@ApiOperation(value = "删除Excel导出sheet详情配置", notes = "删除Excel导出sheet详情配置", httpMethod = "DELETE")
    @RequestMapping(value = "/api/v1/commonInterfaceExcelSheetContent/delete/{id}", method = RequestMethod.DELETE)
	public PublicResult<CommonInterfaceExcelSheetContent> delete(HttpServletRequest request,@PathVariable("id") String id) {
		try {
			 if(iCommonInterfaceExcelSheetContentService.deleteLogic(id)){
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
     * @todo   更新Excel导出sheet详情配置
     */
    @ResponseBody
 	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@ApiOperation(value = "更新Excel导出sheet详情配置", notes = "更新Excel导出sheet详情配置", httpMethod = "PUT")
	@RequestMapping(value = "/api/v1/commonInterfaceExcelSheetContent/update", method = RequestMethod.PUT)
	public PublicResult<CommonInterfaceExcelSheetContent> update(HttpServletRequest request,CommonInterfaceExcelSheetContent entity) {
		try {
			if(entity!=null&&StringUtils.isNotBlank(entity.getId())){
				//更新
				entity.setUpdateBy(getLoginUser(request).getId());
				if(iCommonInterfaceExcelSheetContentService.saveOrUpdate(entity)){
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
     * @todo   查询单个Excel导出sheet详情配置
     */
    @ResponseBody
 	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@ApiOperation(value = "查询单个Excel导出sheet详情配置", notes = "查询单个Excel导出sheet详情配置", httpMethod = "GET")
  	@RequestMapping(value = "/api/v1/commonInterfaceExcelSheetContent/get/{id}", method = RequestMethod.GET )
  	public PublicResult<CommonInterfaceExcelSheetContent> get(HttpServletRequest request,@PathVariable("id") String id) {
  		try {
  			return new PublicResult<>(PublicResultConstant.SUCCESS, iCommonInterfaceExcelSheetContentService.getById(id));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new PublicResult<>(PublicResultConstant.FAILED,e.getMessage(), null);
		}
		
  	}
	
    /**
     * @date   2018-10-11
     * @author zuoqb123
     * @todo   分页查询Excel导出sheet详情配置
     */
    @ResponseBody
 	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@ApiOperation(value = "分页查询Excel导出sheet详情配置", notes = "分页查询Excel导出sheet详情配置", httpMethod = "GET")
  	@RequestMapping(value = "/api/v1/commonInterfaceExcelSheetContent/list", method = RequestMethod.GET)
    public PublicResult<PageInfo<CommonInterfaceExcelSheetContent>> list(CommonInterfaceExcelSheetContent entity,@RequestParam(value="pageNum",required = false,defaultValue="1") Integer pageNum,
			@RequestParam(value="pageSize",required = false,defaultValue="10") Integer pageSize,HttpServletRequest request) {
		try {
			PageInfo<CommonInterfaceExcelSheetContent> page=iCommonInterfaceExcelSheetContentService.pageList(this, request, entity, pageNum, pageSize);
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
  	@ApiOperation(value = "根据sheet编码查询Excel导出sheet详情配置", notes = "根据sheet编码查询Excel导出sheet详情配置", httpMethod = "GET")
  	@RequestMapping(value = "/api/v1/commonInterfaceExcelSheetContent/getBySheetId", method = RequestMethod.GET)
    public PublicResult<List<CommonInterfaceExcelSheetContent>> getBySheetId(@RequestParam(value="sheetId",required = true) String sheetId,
			@RequestParam(value="sortBy",required = false,defaultValue="x_ip") String sortBy,@RequestParam(value="isAsc",required = false,defaultValue="true") boolean isAsc) {
		try {
			List<CommonInterfaceExcelSheetContent> page=iCommonInterfaceExcelSheetContentService.getContentBySheetId(sheetId, sortBy, isAsc);
			return new PublicResult<>(PublicResultConstant.SUCCESS, page);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new PublicResult<>(PublicResultConstant.FAILED,e.getMessage(),null);
		}

	}
  
}

