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
import com.hailian.entity.SysUser;
import com.hailian.mapper.SysUserMapper;
import com.hailian.service.ISysUserService;
/**
 * @date 2018-10-15
 * @author zuoqb123
 * 用户表服务实现类
 */
@Service
@Transactional
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUser> implements ISysUserService,Constant {

    @Autowired
    private SysUserMapper sysUserMapper;
    
     /**
     * @date 2018-10-15
     * @author zuoqb123
     * @todo   用户表新增或者修改
     */
	@Override
    @Transactional(rollbackFor = Exception.class)
	public boolean saveOrUpdate(SysUser entity) {
		if(StringUtils.isBlank(entity.getId())){
			//新增
			entity.setId(UUIDUtils.getUuid());
			entity.setCreateDate(new Date());
			return sysUserMapper.insert(entity)>0;
		}else{
			entity.setUpdateDate(new Date());
			return sysUserMapper.updateById(entity)>0;
		}
	}
	/**
     * @date 2018-10-15
     * @author zuoqb123
     * @todo   用户表逻辑删除
     */
	@Override
    @Transactional(rollbackFor = Exception.class)
	public boolean deleteLogic(String id) {
		SysUser entity=new SysUser();
		entity.setId(id);
		entity.setDelFlag(DEL_FLAG);
		entity.setUpdateDate(new Date());
		return sysUserMapper.updateById(entity)>0;
	}
	/**
     * @date 2018-10-15
     * @author zuoqb123
     * @todo   用户表单条数据查询
     */
	@Override
	public SysUser getById(String id) {
		EntityWrapper<SysUser> wrapper = new EntityWrapper<SysUser>();
		wrapper.where("del_flag={0}",UN_DEL_FLAG);
		wrapper.eq("id", id);
		return selectOne(wrapper);
	}
	/**
     * @date   @date 2018-10-15
     * @author zuoqb123
     * @todo   用户表分页查询
     */
	@Override
	public PageInfo<SysUser> pageList(BaseController c, HttpServletRequest request, SysUser entity,Integer pageNum,Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		EntityWrapper<SysUser> wrapper = searchWrapper(c,request, entity);
		List<SysUser> list = sysUserMapper.selectPage(new RowBounds((pageNum-1)*pageSize, pageSize),wrapper);
		PageInfo<SysUser> page = new PageInfo<SysUser>(list);
		page.setTotal(sysUserMapper.selectCount(wrapper));
		return page;
	}
	
	 /**
     * @date 2018-10-15
     * @author zuoqb123
     * @todo   用户表构建查询条件-以后扩展
     */
    private EntityWrapper<SysUser> searchWrapper(BaseController c,HttpServletRequest request, SysUser entity) {
		EntityWrapper<SysUser> wrapper = new EntityWrapper<SysUser>();
		wrapper.where("del_flag={0}", UN_DEL_FLAG);
		if(c!=null&&request!=null&&c.getLoginUser(request)!=null&&StringUtils.isNotBlank(c.getLoginUser(request).getId())){
			if(!c.isAdmin(request))
			 wrapper.and("create_by", c.getLoginUser(request).getId());
		}
		//根据编号模糊查询
		if(entity.getId()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getId()))){
			wrapper.like("id", String.valueOf(entity.getId()));
		}
		//根据登录名模糊查询
		if(entity.getLoginName()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getLoginName()))){
			wrapper.like("login_name", String.valueOf(entity.getLoginName()));
		}
		//根据密码模糊查询
		if(entity.getPassword()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getPassword()))){
			wrapper.like("password", String.valueOf(entity.getPassword()));
		}
		//根据工号模糊查询
		if(entity.getNo()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getNo()))){
			wrapper.like("no", String.valueOf(entity.getNo()));
		}
		//根据姓名模糊查询
		if(entity.getName()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getName()))){
			wrapper.like("name", String.valueOf(entity.getName()));
		}
		//根据邮箱模糊查询
		if(entity.getEmail()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getEmail()))){
			wrapper.like("email", String.valueOf(entity.getEmail()));
		}
		//根据电话模糊查询
		if(entity.getPhone()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getPhone()))){
			wrapper.like("phone", String.valueOf(entity.getPhone()));
		}
		//根据手机模糊查询
		if(entity.getMobile()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getMobile()))){
			wrapper.like("mobile", String.valueOf(entity.getMobile()));
		}
		//根据用户类型模糊查询
		if(entity.getUserType()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getUserType()))){
			wrapper.like("user_type", String.valueOf(entity.getUserType()));
		}
		//根据用户头像模糊查询
		if(entity.getPhoto()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getPhoto()))){
			wrapper.like("photo", String.valueOf(entity.getPhoto()));
		}
		//根据最后登陆IP模糊查询
		if(entity.getLoginIp()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getLoginIp()))){
			wrapper.like("login_ip", String.valueOf(entity.getLoginIp()));
		}
		//根据最后登陆时间模糊查询
		if(entity.getLoginDate()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getLoginDate()))){
			wrapper.like("login_date", String.valueOf(entity.getLoginDate()));
		}
		//根据是否可登录模糊查询
		if(entity.getLoginFlag()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getLoginFlag()))){
			wrapper.like("login_flag", String.valueOf(entity.getLoginFlag()));
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
