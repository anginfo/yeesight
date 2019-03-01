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
import com.hailian.common.UUIDUtils;
import com.hailian.entity.DocFileCode;
import com.hailian.entity.DocFileSub;
import com.hailian.entity.DocUploadFile;
import com.hailian.entity.DocUploadFileTemp;
import com.hailian.enums.PublicResultConstant;
import com.hailian.service.IDocFileCodeService;
import com.hailian.service.IDocFileSubService;
import com.hailian.service.IDocUploadFileService;
import com.hailian.service.IDocUploadFileTempService;
import com.hailian.utils.Pinyin;
/**
 *
 * @author zuoqb123
 * @date 2018-11-10
 * @todo 文件上传目录表路由
 */
@RestController
@RequestMapping("/api/v1/docUploadFileTemp")
@Api(value = "文件上传目录表",description="文件上传目录表 @author zuoqb123")
public class DocUploadFileTempController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(DocUploadFileTempController.class);

    @Autowired
    public IDocUploadFileTempService iDocUploadFileTempService;
    @Autowired
    public IDocUploadFileService iDocUploadFileService;
    @Autowired
    public IDocFileCodeService iDocFileCodeService;
    @Autowired
    public IDocFileSubService iDocFileSubService;
    /**
     * @date   2018-11-10
     * @author zuoqb123
     * @todo   新增文件上传目录表
     */
  	@ApiOperation(value = "新增文件上传目录表", notes = "新增文件上传目录表", httpMethod = "POST")
  	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@RequestMapping(value = "/add", method = RequestMethod.POST,produces = { "application/json;charset=UTF-8" })
	public Object add(HttpServletRequest request,DocUploadFileTemp entity,
			@RequestParam(value="userCode",required = true) String userCode) {
		try {
			if(StringUtils.isBlank(userCode)){
				return new PublicResult<>(PublicResultConstant.ERROR, "操作人不能为空！");
			}
			if(StringUtils.isBlank(entity.getId())){
				if(StringUtils.isBlank(entity.getParentId())){
					entity.setParentId("0");
				}
				if(StringUtils.isBlank(entity.getIsPublic())){
					entity.setIsPublic("0");
				}
				
				//新增
				entity.setId(UUIDUtils.getUuid());
				entity.setCreateDate(new Date());
				entity.setCreateBy(userCode);
				//生成编码
				entity.setCode(createFileCode(entity, "0"));
				if(iDocUploadFileTempService.insert(entity)){
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
  	public String createFileCode(DocUploadFileTemp temp, String type) {
		String code="RRS_DM_";
		String sub2=Pinyin.getPinYinHeadChar(temp.getTempName());
		code+=sub2+"_";
		String sub3="";
		List<DocFileSub> subList=iDocFileSubService.selectList(null);
		for(DocFileSub sub:subList){
			if(temp.getTempName().indexOf(sub.getName())!=-1){
				sub3=sub.getCode();
				break;
			}
		}
		code+=sub3;
		//查询序号
		int maxNums=iDocFileCodeService.getMaxNums(type, "RRS", sub2, sub3)+1;
		//处理序号
		String all="000000000000000000000000000000"+maxNums;
		
		code+=all.substring(all.length()-6, all.length());
		DocFileCode docFileCode=new DocFileCode();
		docFileCode.setType(type);
		docFileCode.setSub1("RRS");
		docFileCode.setSub2(sub2);
		docFileCode.setSub3(sub3);
		docFileCode.setNums(maxNums);
		docFileCode.setCode(code);
		iDocFileCodeService.insert(docFileCode);
		return code;
	}
    /**
     * @date   2018-11-10
     * @author zuoqb123
     * @todo   删除文件上传目录表
     */
  	@ApiOperation(value = "删除文件上传目录表", notes = "删除文件上传目录表", httpMethod = "POST")
  	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST,produces = { "application/json;charset=UTF-8" })
	public Object delete(HttpServletRequest request,@PathVariable("id") String id,
			@RequestParam(value="userCode",required = true) String userCode) {
		try {
			if(StringUtils.isBlank(userCode)){
				return new PublicResult<>(PublicResultConstant.ERROR, "操作人不能为空！");
			}
			DocUploadFileTemp entity=new DocUploadFileTemp();
			entity.setId(id);
			entity.setDelFlag(DEL_FLAG);
			DocUploadFileTemp temp=iDocUploadFileTempService.selectById(id);
			if(temp==null){
				return new PublicResult<>(PublicResultConstant.ERROR, "目录不存在!");
			}
			if(!temp.getCreateBy().equals(userCode)){
				return new PublicResult<>(PublicResultConstant.ERROR, "该目录只能创建人进行操作!");
			}
			EntityWrapper<DocUploadFile> wrapper = new EntityWrapper<DocUploadFile>();
			wrapper.where("del_flag={0}", UN_DEL_FLAG);
			wrapper.eq("temp_id", id);
			List<DocUploadFile> files=iDocUploadFileService.selectList(wrapper);
			if(files!=null&&files.size()>0){
				return new PublicResult<>(PublicResultConstant.ERROR, "目录下面存在文件，无法删除!");
			}
			DocUploadFileTemp check=new DocUploadFileTemp();
			check.setParentId(id);
			List<DocUploadFileTemp> list = iDocUploadFileTempService.findList(check);
			if(list!=null&&list.size()>0){
				return new PublicResult<>(PublicResultConstant.ERROR, "目录下面存在其他目录，无法删除!");
			}
			entity.setUpdateDate(new Date());
			entity.setUpdateBy(userCode);
			 if(iDocUploadFileTempService.updateById(entity)){
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
     * @date   2018-11-10
     * @author zuoqb123
     * @todo   更新文件上传目录表
     */
  	@ApiOperation(value = "更新文件上传目录表", notes = "更新文件上传目录表", httpMethod = "PUT")
  	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@RequestMapping(value = "/update", method = RequestMethod.PUT,produces = { "application/json;charset=UTF-8" })
	public Object update(HttpServletRequest request,DocUploadFileTemp entity,
			@RequestParam(value="userCode",required = true) String userCode) {
		try {
			if(StringUtils.isBlank(userCode)){
				return new PublicResult<>(PublicResultConstant.ERROR, "操作人不能为空！");
			}
			if(entity!=null&&StringUtils.isNotBlank(entity.getId())){
				//更新
				entity.setUpdateDate(new Date());
				entity.setUpdateBy(userCode);
				DocUploadFileTemp temp=iDocUploadFileTempService.selectById(entity.getId());
				if(temp==null){
					return new PublicResult<>(PublicResultConstant.ERROR, "目录不存在!");
				}
				if(!temp.getCreateBy().equals(userCode)){
					return new PublicResult<>(PublicResultConstant.ERROR, "该目录只能创建人进行操作!");
				}
				if(iDocUploadFileTempService.updateById(entity)){
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
     * @date   2018-11-10
     * @author zuoqb123
     * @todo   查询单个文件上传目录表
     */
  	@ApiOperation(value = "查询单个文件上传目录表", notes = "查询单个文件上传目录表", httpMethod = "GET")
  	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET ,produces = { "application/json;charset=UTF-8" })
  	public PublicResult<DocUploadFileTemp> get(HttpServletRequest request,@PathVariable("id") String id) {
  		DocUploadFileTemp entity=null;
  		try {
  			EntityWrapper<DocUploadFileTemp> wrapper = new EntityWrapper<DocUploadFileTemp>();
  			wrapper.where("del_flag={0}", UN_DEL_FLAG);
  			wrapper.eq("id", id);
  			entity=iDocUploadFileTempService.selectOne(wrapper);
  			return new PublicResult<>(PublicResultConstant.SUCCESS, entity);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new PublicResult<>(PublicResultConstant.FAILED,e.getMessage(), null);
		}
  	}
  	 /**
     * @date   2018-11-10
     * @author zuoqb123
     * @todo   分页查询文件上传目录表
     */
  	@ApiOperation(value = "分询文件上传目录表", notes = "分页查询文件上传目录表", httpMethod = "GET")
  	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@RequestMapping(value = "/fileAllTemps", method = RequestMethod.GET,produces = { "application/json;charset=UTF-8" })
    public Object fileAllTemps(DocUploadFileTemp entity,HttpServletRequest request) {
		try {
			if(StringUtils.isBlank(entity.getParentId())){
				entity.setParentId("0");
			}
			List<DocUploadFileTemp> list = iDocUploadFileTempService.findList(entity);
			return new PublicResult<>(PublicResultConstant.SUCCESS, list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new PublicResult<>(PublicResultConstant.FAILED,e.getMessage(),null);
		}

	}
  	 /**
     * @date   2018-11-10
     * @author zuoqb123
     * @todo  文件上传目录表
     */
  	@ApiOperation(value = "文件上传目录表", notes = "分页查询文件上传目录表", httpMethod = "GET")
  	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@RequestMapping(value = "/fileTemps", method = RequestMethod.GET,produces = { "application/json;charset=UTF-8" })
    public Object fileTemps(DocUploadFileTemp entity,HttpServletRequest request) {
		try {
			List<DocUploadFileTemp> list = iDocUploadFileTempService.findAllList(entity);
			return new PublicResult<>(PublicResultConstant.SUCCESS, list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new PublicResult<>(PublicResultConstant.FAILED,e.getMessage(),null);
		}

	}
    /**
     * @date   2018-11-10
     * @author zuoqb123
     * @todo   分页查询文件上传目录表
     */
  	@ApiOperation(value = "分页查询文件上传目录表", notes = "分页查询文件上传目录表", httpMethod = "GET")
  	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
  	@RequestMapping(value = "/list", method = RequestMethod.GET,produces = { "application/json;charset=UTF-8" })
    public PublicResult<PageInfo<DocUploadFileTemp>> list(DocUploadFileTemp entity,@RequestParam(value="pageNum",required = false,defaultValue="1") Integer pageNum,
			@RequestParam(value="pageSize",required = false,defaultValue="10") Integer pageSize,HttpServletRequest request) {
		try {
			EntityWrapper<DocUploadFileTemp> wrapper = searchWrapper(request, entity);
			PageHelper.startPage(pageNum, pageSize);
			List<DocUploadFileTemp> list = iDocUploadFileTempService.selectList(wrapper);
			//查询目录下文件
			for(DocUploadFileTemp temp:list){
				EntityWrapper<DocUploadFile> fileWrapper = new EntityWrapper<DocUploadFile>();
				fileWrapper.where("del_flag={0}", UN_DEL_FLAG);
				fileWrapper.eq("temp_id", temp.getId());
				fileWrapper.orderBy("create_date", false);
				temp.setFiles(iDocUploadFileService.selectList(fileWrapper));
			}
			PageInfo<DocUploadFileTemp> page = new PageInfo<DocUploadFileTemp>(list);
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
    private EntityWrapper<DocUploadFileTemp> searchWrapper(HttpServletRequest request, DocUploadFileTemp entity) {
		EntityWrapper<DocUploadFileTemp> wrapper = new EntityWrapper<DocUploadFileTemp>();
		wrapper.where("del_flag={0}", UN_DEL_FLAG);
		if(getLoginUser(request)!=null&&StringUtils.isNotBlank(getLoginUser(request).getId())){
			if(!isAdmin(request))
			 wrapper.and("create_by", getLoginUser(request).getId());
		}
				//根据编码模糊查询
		if(entity.getId()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getId()))){
			wrapper.like("id", String.valueOf(entity.getId()));
		}
				//根据父级编码模糊查询
		if(entity.getParentId()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getParentId()))){
			wrapper.like("parent_id", String.valueOf(entity.getParentId()));
		}
				//根据目录名称模糊查询
		if(entity.getTempName()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getTempName()))){
			wrapper.like("temp_name", String.valueOf(entity.getTempName()));
		}
				//根据是否公开 0-公开 1-不公开模糊查询
		if(entity.getIsPublic()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getIsPublic()))){
			wrapper.like("is_public", String.valueOf(entity.getIsPublic()));
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

