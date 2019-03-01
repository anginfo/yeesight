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
import com.hailian.entity.SysPlatInfo;
import com.hailian.mapper.SysPlatInfoMapper;
import com.hailian.service.ISysPlatInfoService;
/**
 * @date 2018-10-09
 * @author zuoqb123
 * 平台信息服务实现类
 */
@Service
@Transactional
public class SysPlatInfoServiceImpl extends BaseServiceImpl<SysPlatInfoMapper, SysPlatInfo> implements ISysPlatInfoService,Constant {

    @Autowired
    private SysPlatInfoMapper sysPlatInfoMapper;
    
     /**
     * @date 2018-10-09
     * @author zuoqb123
     * @todo   平台信息新增或者修改
     */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean saveOrUpdate(SysPlatInfo entity) {
		if(StringUtils.isBlank(entity.getId())){
			//新增
			entity.setId(UUIDUtils.getUuid());
			entity.setCreateDate(new Date());
			return sysPlatInfoMapper.insert(entity)>0;
		}else{
			entity.setUpdateDate(new Date());
			return sysPlatInfoMapper.updateById(entity)>0;
		}
	}
	/**
     * @date 2018-10-09
     * @author zuoqb123
     * @todo   平台信息逻辑删除
     */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteLogic(String id) {
		SysPlatInfo entity=new SysPlatInfo();
		entity.setId(id);
		entity.setDelFlag(DEL_FLAG);
		entity.setUpdateDate(new Date());
		return sysPlatInfoMapper.updateById(entity)>0;
	}
	/**
     * @date 2018-10-09
     * @author zuoqb123
     * @todo   平台信息单条数据查询
     */
	@Override
	public SysPlatInfo getById(String id) {
		EntityWrapper<SysPlatInfo> wrapper = new EntityWrapper<SysPlatInfo>();
		wrapper.where("del_flag={0}",UN_DEL_FLAG);
		wrapper.eq("id", id);
		return selectOne(wrapper);
	}
	/**
     * @date   @date 2018-10-09
     * @author zuoqb123
     * @todo   平台信息分页查询
     */
	@Override
	public PageInfo<SysPlatInfo> pageList(BaseController c, HttpServletRequest request, SysPlatInfo entity,Integer pageNum,Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		EntityWrapper<SysPlatInfo> wrapper = searchWrapper(c,request, entity);
		List<SysPlatInfo> list = sysPlatInfoMapper.selectPage(new RowBounds((pageNum-1)*pageSize, pageSize),wrapper);
		PageInfo<SysPlatInfo> page = new PageInfo<SysPlatInfo>(list);
		page.setTotal(sysPlatInfoMapper.selectCount(wrapper));
		return page;
	}
	
	 /**
     * @date 2018-10-09
     * @author zuoqb123
     * @todo   平台信息构建查询条件-以后扩展
     */
    private EntityWrapper<SysPlatInfo> searchWrapper(BaseController c,HttpServletRequest request, SysPlatInfo entity) {
		EntityWrapper<SysPlatInfo> wrapper = new EntityWrapper<SysPlatInfo>();
		wrapper.where("del_flag={0}", UN_DEL_FLAG);
		if(c!=null&&request!=null&&c.getLoginUser(request)!=null&&StringUtils.isNotBlank(c.getLoginUser(request).getId())){
			if(!c.isAdmin(request))
			 wrapper.and("create_by", c.getLoginUser(request).getId());
		}
		//根据平台编号模糊查询
		if(entity.getId()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getId()))){
			wrapper.like("id", String.valueOf(entity.getId()));
		}
		//根据平台名称模糊查询
		if(entity.getName()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getName()))){
			wrapper.like("name", String.valueOf(entity.getName()));
		}
		//根据平台英文名称模糊查询
		if(entity.getEnname()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getEnname()))){
			wrapper.like("enname", String.valueOf(entity.getEnname()));
		}
		//根据接口版本模糊查询
		if(entity.getVersions()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getVersions()))){
			wrapper.like("versions", String.valueOf(entity.getVersions()));
		}
		//根据平台秘钥 X-Sign模糊查询
		if(entity.getSecretKey()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getSecretKey()))){
			wrapper.like("secret_key", String.valueOf(entity.getSecretKey()));
		}
		//根据平台联系人模糊查询
		if(entity.getContacts()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getContacts()))){
			wrapper.like("contacts", String.valueOf(entity.getContacts()));
		}
		//根据联系人电话模糊查询
		if(entity.getContactsTel()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getContactsTel()))){
			wrapper.like("contacts_tel", String.valueOf(entity.getContactsTel()));
		}
		//根据联系人邮箱模糊查询
		if(entity.getContactsMail()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getContactsMail()))){
			wrapper.like("contacts_mail", String.valueOf(entity.getContactsMail()));
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
