package com.hailian.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hailian.annotation.AuthPower;
import com.hailian.base.BaseController;
import com.hailian.common.PublicResult;
import com.hailian.entity.DocFileCode;
import com.hailian.enums.PublicResultConstant;
import com.hailian.service.IDocFileCodeService;
/**
 *
 * @author zuoqb123
 * @date 2018-11-12
 * @todo 文件编码表路由
 */
@RestController
@RequestMapping("/api/v1/docFileCode")
@Api(value = "文件编码表",description="文件编码表 @author zuoqb123")
public class DocFileCodeController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(DocFileCodeController.class);

    @Autowired
    public IDocFileCodeService iDocFileCodeService;
    
    

    /**
     * @date   2018-11-12
     * @author zuoqb123
     * @todo   新增文件编码表
     */
  	@ApiOperation(value = "新增文件编码表", notes = "新增文件编码表", httpMethod = "POST")
  	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@RequestMapping(value = "/add", method = RequestMethod.POST,produces = { "application/json;charset=UTF-8" })
	public PublicResult<DocFileCode> add(HttpServletRequest request,DocFileCode entity) {
		try {
			if(entity.getId()==null){
				//新增
				entity.setCreateDate(new Date());
				entity.setCreateBy(getLoginUser(request).getId());
				if(iDocFileCodeService.insert(entity)){
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
     * @date   2018-11-12
     * @author zuoqb123
     * @todo   删除文件编码表
     */
  	@ApiOperation(value = "删除文件编码表", notes = "删除文件编码表", httpMethod = "DELETE")
  	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE,produces = { "application/json;charset=UTF-8" })
	public PublicResult<DocFileCode> delete(HttpServletRequest request,@PathVariable("id") Integer id) {
		try {
			DocFileCode entity=new DocFileCode();
			entity.setId(id);
			entity.setDelFlag(DEL_FLAG);
			entity.setUpdateDate(new Date());
			entity.setUpdateBy(getLoginUser(request).getId());
			 if(iDocFileCodeService.updateById(entity)){
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
     * @date   2018-11-12
     * @author zuoqb123
     * @todo   更新文件编码表
     */
  	@ApiOperation(value = "更新文件编码表", notes = "更新文件编码表", httpMethod = "PUT")
  	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@RequestMapping(value = "/update", method = RequestMethod.PUT,produces = { "application/json;charset=UTF-8" })
	public PublicResult<DocFileCode> update(HttpServletRequest request,DocFileCode entity) {
		try {
			if(entity!=null&&entity.getId()!=null){
				//更新
				entity.setUpdateDate(new Date());
				entity.setUpdateBy(getLoginUser(request).getId());
				if(iDocFileCodeService.updateById(entity)){
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
     * @date   2018-11-12
     * @author zuoqb123
     * @todo   查询单个文件编码表
     */
  	@ApiOperation(value = "查询单个文件编码表", notes = "查询单个文件编码表", httpMethod = "GET")
  	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET ,produces = { "application/json;charset=UTF-8" })
  	public PublicResult<DocFileCode> get(HttpServletRequest request,@PathVariable("id") String id) {
  		DocFileCode entity=null;
  		try {
  			EntityWrapper<DocFileCode> wrapper = new EntityWrapper<DocFileCode>();
  			wrapper.where("del_flag={0}", UN_DEL_FLAG);
  			wrapper.eq("id", id);
  			entity=iDocFileCodeService.selectOne(wrapper);
  			return new PublicResult<>(PublicResultConstant.SUCCESS, entity);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new PublicResult<>(PublicResultConstant.FAILED,e.getMessage(), null);
		}
  	}
	
    /**
     * @date   2018-11-12
     * @author zuoqb123
     * @todo   分页查询文件编码表
     */
  	@ApiOperation(value = "分页查询文件编码表", notes = "分页查询文件编码表", httpMethod = "GET")
  	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@RequestMapping(value = "/list", method = RequestMethod.GET,produces = { "application/json;charset=UTF-8" })
    public PublicResult<PageInfo<DocFileCode>> list(DocFileCode entity,@RequestParam(value="pageNum",required = false,defaultValue="1") Integer pageNum,
			@RequestParam(value="pageSize",required = false,defaultValue="10") Integer pageSize,HttpServletRequest request) {
		try {
			EntityWrapper<DocFileCode> wrapper = searchWrapper(request, entity);
			PageHelper.startPage(pageNum, pageSize);
			List<DocFileCode> list = iDocFileCodeService.selectList(wrapper);
			PageInfo<DocFileCode> page = new PageInfo<DocFileCode>(list);
			return new PublicResult<>(PublicResultConstant.SUCCESS, page);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new PublicResult<>(PublicResultConstant.FAILED,e.getMessage(),null);
		}

	}
    /**
     * @date   2018年9月25日 下午5:36:10
     * @author zuoqb123
     * @todo   构建查询条件-以后扩展
     */
    private EntityWrapper<DocFileCode> searchWrapper(HttpServletRequest request, DocFileCode entity) {
		EntityWrapper<DocFileCode> wrapper = new EntityWrapper<DocFileCode>();
		wrapper.where("del_flag={0}", UN_DEL_FLAG);
		if(getLoginUser(request)!=null&&StringUtils.isNotBlank(getLoginUser(request).getId())){
			if(!isAdmin(request))
			 wrapper.and("create_by", getLoginUser(request).getId());
		}
				//根据编码模糊查询
		if(entity.getId()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getId()))){
			wrapper.like("id", String.valueOf(entity.getId()));
		}
				//根据模糊查询
		if(entity.getSub1()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getSub1()))){
			wrapper.like("sub1", String.valueOf(entity.getSub1()));
		}
				//根据模糊查询
		if(entity.getSub2()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getSub2()))){
			wrapper.like("sub2", String.valueOf(entity.getSub2()));
		}
				//根据模糊查询
		if(entity.getSub3()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getSub3()))){
			wrapper.like("sub3", String.valueOf(entity.getSub3()));
		}
				//根据模糊查询
		if(entity.getNums()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getNums()))){
			wrapper.like("nums", String.valueOf(entity.getNums()));
		}
				//根据模糊查询
		if(entity.getCode()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getCode()))){
			wrapper.like("code", String.valueOf(entity.getCode()));
		}
				//根据备注信息模糊查询
		if(entity.getRemarks()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getRemarks()))){
			wrapper.like("remarks", String.valueOf(entity.getRemarks()));
		}
				//根据创建者模糊查询
		if(entity.getCreateBy()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getCreateBy()))){
			wrapper.like("create_by", String.valueOf(entity.getCreateBy()));
		}
				//根据创建时间模糊查询
		if(entity.getCreateDate()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getCreateDate()))){
			wrapper.like("create_date", String.valueOf(entity.getCreateDate()));
		}
				//根据更新者模糊查询
		if(entity.getUpdateBy()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getUpdateBy()))){
			wrapper.like("update_by", String.valueOf(entity.getUpdateBy()));
		}
				//根据更新时间模糊查询
		if(entity.getUpdateDate()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getUpdateDate()))){
			wrapper.like("update_date", String.valueOf(entity.getUpdateDate()));
		}
				//根据删除标记（0：正常；1：删除）模糊查询
		if(entity.getDelFlag()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getDelFlag()))){
			wrapper.like("del_flag", String.valueOf(entity.getDelFlag()));
		}
				wrapper.orderBy("create_date", true);
		System.out.println(wrapper.originalSql());
		return wrapper;
	}
}

