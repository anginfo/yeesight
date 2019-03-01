package com.hailian.service.impl;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hailian.base.BaseController;
import com.hailian.base.BaseServiceImpl;
import com.hailian.common.UUIDUtils;
import com.hailian.conf.Constant;
import com.hailian.entity.EtaxExcelFile;
import com.hailian.mapper.EtaxExcelFileMapper;
import com.hailian.service.IEtaxExcelFileService;
/**
 * @date 2018-11-18
 * @author zuoqb123
 * 保税处理之后的文件信息表表服务实现类
 */
@Service
@Transactional
public class EtaxExcelFileServiceImpl extends BaseServiceImpl<EtaxExcelFileMapper, EtaxExcelFile> implements IEtaxExcelFileService,Constant {

    @Autowired
    private EtaxExcelFileMapper etaxExcelFileMapper;
    
     /**
     * @date 2018-11-18
     * @author zuoqb123
     * @todo   保税处理之后的文件信息表表新增或者修改
     */
	@Override
    @Transactional(rollbackFor = Exception.class)
	public boolean saveOrUpdate(EtaxExcelFile entity) {
		if(StringUtils.isBlank(entity.getId())){
			//新增
			entity.setId(UUIDUtils.getUuid());
			entity.setCreateDate(new Date());
			return etaxExcelFileMapper.insert(entity)>0;
		}else{
			entity.setUpdateDate(new Date());
			return etaxExcelFileMapper.updateById(entity)>0;
		}
	}
	/**
     * @date 2018-11-18
     * @author zuoqb123
     * @todo   保税处理之后的文件信息表表逻辑删除
     */
	@Override
    @Transactional(rollbackFor = Exception.class)
	public boolean deleteLogic(String id) {
		EtaxExcelFile entity=new EtaxExcelFile();
		entity.setId(id);
		entity.setDelFlag(DEL_FLAG);
		entity.setUpdateDate(new Date());
		return etaxExcelFileMapper.updateById(entity)>0;
	}
	/**
     * @date 2018-11-18
     * @author zuoqb123
     * @todo   保税处理之后的文件信息表表单条数据查询
     */
	@Override
	public EtaxExcelFile getById(String id) {
		EntityWrapper<EtaxExcelFile> wrapper = new EntityWrapper<EtaxExcelFile>();
		wrapper.where("del_flag={0}",UN_DEL_FLAG);
		wrapper.eq("id", id);
		return selectOne(wrapper);
	}
	/**
     * @date   @date 2018-11-18
     * @author zuoqb123
     * @todo   保税处理之后的文件信息表表分页查询
     */
	@Override
	public PageInfo<EtaxExcelFile> pageList(BaseController c, HttpServletRequest request, EtaxExcelFile entity,Integer pageNum,Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		EntityWrapper<EtaxExcelFile> wrapper = searchWrapper(c,request, entity);
		List<EtaxExcelFile> list=etaxExcelFileMapper.selectPage(new RowBounds((pageNum-1)*pageSize, pageSize), wrapper);
		PageInfo<EtaxExcelFile> page = new PageInfo<EtaxExcelFile>();
		page.setList(list);
		page.setTotal(etaxExcelFileMapper.selectCount(wrapper));
		return page;
	}
	
	 /**
     * @date 2018-11-18
     * @author zuoqb123
     * @todo   保税处理之后的文件信息表表构建查询条件-以后扩展
     */
    private EntityWrapper<EtaxExcelFile> searchWrapper(BaseController c,HttpServletRequest request, EtaxExcelFile entity) {
		EntityWrapper<EtaxExcelFile> wrapper = new EntityWrapper<EtaxExcelFile>();
		wrapper.where("del_flag={0}", UN_DEL_FLAG);
		if(c!=null&&request!=null&&c.getLoginUser(request)!=null&&StringUtils.isNotBlank(c.getLoginUser(request).getId())){
			if(!c.isAdmin(request))
			 wrapper.and("create_by", c.getLoginUser(request).getId());
		}
		//根据编码模糊查询
		if(entity.getId()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getId()))){
			wrapper.like("id", String.valueOf(entity.getId()));
		}
		//根据版本 基本使用时间戳模糊查询
		if(entity.getVersion()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getVersion()))){
			wrapper.like("version", String.valueOf(entity.getVersion()));
		}
		//根据原压缩文件类型模糊查询
		if(entity.getOrgCompressType()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getOrgCompressType()))){
			wrapper.like("org_compress_type", String.valueOf(entity.getOrgCompressType()));
		}
		//根据原压缩文件名称模糊查询
		if(entity.getOrgCompressName()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getOrgCompressName()))){
			wrapper.like("org_compress_name", String.valueOf(entity.getOrgCompressName()));
		}
		//根据原压缩文件保存路径模糊查询
		if(entity.getOrgCompressPath()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getOrgCompressPath()))){
			wrapper.like("org_compress_path", String.valueOf(entity.getOrgCompressPath()));
		}
		//根据解压后文件路径模糊查询
		if(entity.getUncompressPath()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getUncompressPath()))){
			wrapper.like("uncompress_path", String.valueOf(entity.getUncompressPath()));
		}
		//根据处理完后文件保存路径模糊查询
		if(entity.getTargetPath()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getTargetPath()))){
			wrapper.like("target_path", String.valueOf(entity.getTargetPath()));
		}
		//根据最终生成目标文件名称模糊查询
		if(entity.getTargetFileName()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getTargetFileName()))){
			wrapper.like("target_file_name", String.valueOf(entity.getTargetFileName()));
		}
		//根据文件上传编码 引用doc_upload_file  可以查看html信息模糊查询
		if(entity.getFileId()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getFileId()))){
			wrapper.like("file_id", String.valueOf(entity.getFileId()));
		}
		if(entity.getStartDate()!=null){
			wrapper.ge("create_date", entity.getStartDate());
		}
		if(entity.getEndDate()!=null){
			wrapper.le("create_date", entity.getEndDate());
		}
		if(StringUtils.isNoneBlank(entity.getOrderBy())){
			wrapper.orderBy(entity.getOrderBy(), entity.isAsc());
		}else{
			wrapper.orderBy("create_date", false);
		}
		//System.out.println(wrapper.originalSql());
		return wrapper;
	}
   
}
