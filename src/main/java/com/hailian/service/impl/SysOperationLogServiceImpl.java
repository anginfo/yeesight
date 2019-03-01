package com.hailian.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hailian.entity.EtaxExcelFile;
import com.hailian.entity.SysOperationLog;
import com.hailian.mapper.SysOperationLogMapper;
import com.hailian.service.ISysOperationLogService;
import com.hailian.base.BaseServiceImpl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import com.hailian.common.UUIDUtils;
import com.hailian.conf.Constant;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hailian.base.BaseController;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
/**
 * @date 2018-10-09
 * @author zuoqb123
 * 操作日志服务实现类
 */
@Service
@Transactional
public class SysOperationLogServiceImpl extends BaseServiceImpl<SysOperationLogMapper, SysOperationLog> implements ISysOperationLogService,Constant {

    @Autowired
    private SysOperationLogMapper sysOperationLogMapper;
    
     /**
     * @date 2018-10-09
     * @author zuoqb123
     * @todo   操作日志新增或者修改
     */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean saveOrUpdate(SysOperationLog entity) {
		if(StringUtils.isBlank(entity.getId())){
			//新增
			entity.setId(UUIDUtils.getUuid());
			entity.setCreateDate(new Date());
			return sysOperationLogMapper.insert(entity)>0;
		}else{
			entity.setUpdateDate(new Date());
			return sysOperationLogMapper.updateById(entity)>0;
		}
	}
	/**
     * @date 2018-10-09
     * @author zuoqb123
     * @todo   操作日志逻辑删除
     */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteLogic(String id) {
		SysOperationLog entity=new SysOperationLog();
		entity.setId(id);
		entity.setDelFlag(DEL_FLAG);
		entity.setUpdateDate(new Date());
		return sysOperationLogMapper.updateById(entity)>0;
	}
	/**
     * @date 2018-10-09
     * @author zuoqb123
     * @todo   操作日志单条数据查询
     */
	@Override
	public SysOperationLog getById(String id) {
		EntityWrapper<SysOperationLog> wrapper = new EntityWrapper<SysOperationLog>();
		wrapper.where("del_flag={0}",UN_DEL_FLAG);
		wrapper.eq("id", id);
		return selectOne(wrapper);
	}
	/**
     * @date   @date 2018-10-09
     * @author zuoqb123
     * @todo   操作日志分页查询
     */
	@Override
	public PageInfo<SysOperationLog> pageList(BaseController c, HttpServletRequest request, SysOperationLog entity,Integer pageNum,Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		EntityWrapper<SysOperationLog> wrapper = searchWrapper(c,request, entity);
		List<SysOperationLog> list = sysOperationLogMapper.selectPage(new RowBounds((pageNum-1)*pageSize, pageSize),wrapper);
		PageInfo<SysOperationLog> page = new PageInfo<SysOperationLog>(list);
		page.setTotal(sysOperationLogMapper.selectCount(wrapper));
		return page;
	}
	
	 /**
     * @date 2018-10-09
     * @author zuoqb123
     * @todo   操作日志构建查询条件-以后扩展
     */
    private EntityWrapper<SysOperationLog> searchWrapper(BaseController c,HttpServletRequest request, SysOperationLog entity) {
		EntityWrapper<SysOperationLog> wrapper = new EntityWrapper<SysOperationLog>();
		wrapper.where("del_flag={0}", UN_DEL_FLAG);
		if(c!=null&&request!=null&&c.getLoginUser(request)!=null&&StringUtils.isNotBlank(c.getLoginUser(request).getId())){
			if(!c.isAdmin(request))
			 wrapper.and("create_by", c.getLoginUser(request).getId());
		}
		//根据模糊查询
		if(entity.getId()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getId()))){
			wrapper.like("id", String.valueOf(entity.getId()));
		}
		//根据日志类型模糊查询
		if(entity.getLogDescription()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getLogDescription()))){
			wrapper.like("log_description", String.valueOf(entity.getLogDescription()));
		}
		//根据日志名称模糊查询
		if(entity.getActionArgs()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getActionArgs()))){
			wrapper.like("action_args", String.valueOf(entity.getActionArgs()));
		}
		//根据用户id模糊查询
		if(entity.getUserName()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getUserName()))){
			wrapper.like("user_name", String.valueOf(entity.getUserName()));
		}
		//根据类名称模糊查询
		if(entity.getClassName()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getClassName()))){
			wrapper.like("class_name", String.valueOf(entity.getClassName()));
		}
		//根据方法名称模糊查询
		if(entity.getMethod()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getMethod()))){
			wrapper.like("method", String.valueOf(entity.getMethod()));
		}
		//根据模糊查询
		if(entity.getIp()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getIp()))){
			wrapper.like("ip", String.valueOf(entity.getIp()));
		}
		//根据创建时间模糊查询
		if(entity.getCreateTime()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getCreateTime()))){
			wrapper.like("create_time", String.valueOf(entity.getCreateTime()));
		}
		//根据是否成功模糊查询
		if(entity.getSucceed()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getSucceed()))){
			wrapper.like("succeed", String.valueOf(entity.getSucceed()));
		}
		//根据备注模糊查询
		if(entity.getMessage()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getMessage()))){
			wrapper.like("message", String.valueOf(entity.getMessage()));
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
	@Override
	public List<SysOperationLog> dateList() {
		// TODO Auto-generated method stub
		return sysOperationLogMapper.dateList();
	}
   
}
