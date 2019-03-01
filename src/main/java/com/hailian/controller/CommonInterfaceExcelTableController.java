package com.hailian.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.hailian.entity.CommonInterfaceExcelTable;
import com.hailian.enums.PublicResultConstant;
import com.hailian.service.ICommonInterfaceExcelTableService;
/**
 *
 * @date 2018-10-11
 * @author zuoqb123
 * @todo excel导出模板配置表路由
 */
@Controller
@Api(value = "excel导出模板配置表",description="excel导出模板配置表 @author zuoqb123")
public class CommonInterfaceExcelTableController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(CommonInterfaceExcelTableController.class);

    @Autowired
    public ICommonInterfaceExcelTableService iCommonInterfaceExcelTableService;

    /**
     * @date   2018-10-11
     * @author zuoqb123
     * @todo   新增excel导出模板配置表
     */
    @ResponseBody
 	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@ApiOperation(value = "新增excel导出模板配置表", notes = "新增excel导出模板配置表", httpMethod = "POST")
	@RequestMapping(value = "/api/v1/commonInterfaceExcelTable/add", method = RequestMethod.POST)
	public PublicResult<CommonInterfaceExcelTable> add(HttpServletRequest request,CommonInterfaceExcelTable entity) {
		try {
			if(StringUtils.isBlank(entity.getId())){
				//新增
				entity.setCreateBy(getLoginUser(request).getId());
				if(iCommonInterfaceExcelTableService.saveOrUpdate(entity)){
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
     * @todo   删除excel导出模板配置表
     */
    @ResponseBody
 	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@ApiOperation(value = "删除excel导出模板配置表", notes = "删除excel导出模板配置表", httpMethod = "DELETE")
    @RequestMapping(value = "/api/v1/commonInterfaceExcelTable/delete/{id}", method = RequestMethod.DELETE)
	public PublicResult<CommonInterfaceExcelTable> delete(HttpServletRequest request,@PathVariable("id") String id) {
		try {
			 if(iCommonInterfaceExcelTableService.deleteLogic(id)){
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
     * @todo   更新excel导出模板配置表
     */
    @ResponseBody
 	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@ApiOperation(value = "更新excel导出模板配置表", notes = "更新excel导出模板配置表", httpMethod = "PUT")
	@RequestMapping(value = "/api/v1/commonInterfaceExcelTable/update", method = RequestMethod.PUT)
	public PublicResult<CommonInterfaceExcelTable> update(HttpServletRequest request,CommonInterfaceExcelTable entity) {
		try {
			if(entity!=null&&StringUtils.isNotBlank(entity.getId())){
				//更新
				entity.setUpdateBy(getLoginUser(request).getId());
				if(iCommonInterfaceExcelTableService.saveOrUpdate(entity)){
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
     * @todo   查询单个excel导出模板配置表
     */
    @ResponseBody
 	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@ApiOperation(value = "查询单个excel导出模板配置表", notes = "查询单个excel导出模板配置表", httpMethod = "GET")
  	@RequestMapping(value = "/api/v1/commonInterfaceExcelTable/get/{id}", method = RequestMethod.GET )
  	public PublicResult<CommonInterfaceExcelTable> get(HttpServletRequest request,@PathVariable("id") String id) {
  		try {
  			return new PublicResult<>(PublicResultConstant.SUCCESS, iCommonInterfaceExcelTableService.getById(id));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new PublicResult<>(PublicResultConstant.FAILED,e.getMessage(), null);
		}
		
  	}
	
    /**
     * @date   2018-10-11
     * @author zuoqb123
     * @todo   分页查询excel导出模板配置表
     */
    @ResponseBody
 	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@ApiOperation(value = "分页查询excel导出模板配置表", notes = "分页查询excel导出模板配置表", httpMethod = "GET")
  	@RequestMapping(value = "/api/v1/commonInterfaceExcelTable/list", method = RequestMethod.GET)
    public PublicResult<PageInfo<CommonInterfaceExcelTable>> list(CommonInterfaceExcelTable entity,@RequestParam(value="pageNum",required = false,defaultValue="1") Integer pageNum,
			@RequestParam(value="pageSize",required = false,defaultValue="10") Integer pageSize,HttpServletRequest request) {
		try {
			PageInfo<CommonInterfaceExcelTable> page=iCommonInterfaceExcelTableService.pageList(this, request, entity, pageNum, pageSize);
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
     * @todo   根据配置导出Excel
     */
	@SuppressWarnings("unchecked")
	@ResponseBody
 	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@ApiOperation(value = "根据配置导出Excel", notes = "根据配置导出Excel", httpMethod = "GET")
  	@RequestMapping(value = "/api/v1/commonExcelExport/get/{id}", method = RequestMethod.GET )
  	public void exportExcel(HttpServletRequest request,HttpServletResponse response,@PathVariable("id") String id,
  			@RequestParam(value="params",required = false) String params,
  			@RequestParam(value="dataSpace",required = false) String dataSpaceStr) {
		PublicResult<?> result=iCommonInterfaceExcelTableService.exportExcel(request, response, id, params, dataSpaceStr);
		if(PublicResultConstant.SUCCESS.getResult().equals(result.getResult())){
			//成功
			try {
				Map<String, Object> data=(Map<String, Object>) result.getData();
				byte[] buff = (byte[]) data.get("excelObject");
				String tableName=data.get("tableName")+"";
				response.setHeader("Content-disposition",
						"attachment; filename=" + new String(tableName.getBytes(), "iso-8859-1")
								+ new SimpleDateFormat("yyyyMMdd").format(new Date()).toString() + ".xlsx");
				response.setContentType("application/msexcel");
				OutputStream os = response.getOutputStream();
				os.write(buff, 0, buff.length);
				os.close();
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		}
		
  	}
  
}

