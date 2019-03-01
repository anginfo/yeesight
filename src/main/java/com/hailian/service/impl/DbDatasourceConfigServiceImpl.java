package com.hailian.service.impl;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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
import com.hailian.entity.DbDatasourceConfig;
import com.hailian.mapper.DbDatasourceConfigMapper;
import com.hailian.service.IDbDatasourceConfigService;
/**
 * @date 2018-10-09
 * @author zuoqb123
 * 数据源配置服务实现类
 */
@Service
@Transactional
public class DbDatasourceConfigServiceImpl extends BaseServiceImpl<DbDatasourceConfigMapper, DbDatasourceConfig> implements IDbDatasourceConfigService,Constant {

    @Autowired
    private DbDatasourceConfigMapper dbDatasourceConfigMapper;
    
     /**
     * @date 2018-10-09
     * @author zuoqb123
     * @todo   数据源配置新增或者修改
     */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean saveOrUpdate(DbDatasourceConfig entity) {
		if(StringUtils.isBlank(entity.getId())){
			//新增
			entity.setId(UUIDUtils.getUuid());
			entity.setCreateDate(new Date());
			return dbDatasourceConfigMapper.insert(entity)>0;
		}else{
			entity.setUpdateDate(new Date());
			return dbDatasourceConfigMapper.updateById(entity)>0;
		}
	}
	/**
     * @date 2018-10-09
     * @author zuoqb123
     * @todo   数据源配置逻辑删除
     */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteLogic(String id) {
		DbDatasourceConfig entity=new DbDatasourceConfig();
		entity.setId(id);
		entity.setDelFlag(DEL_FLAG);
		entity.setUpdateDate(new Date());
		return dbDatasourceConfigMapper.updateById(entity)>0;
	}
	/**
     * @date 2018-10-09
     * @author zuoqb123
     * @todo   数据源配置单条数据查询
     */
	@Override
	public DbDatasourceConfig getById(String id) {
		EntityWrapper<DbDatasourceConfig> wrapper = new EntityWrapper<DbDatasourceConfig>();
		wrapper.where("del_flag={0}",UN_DEL_FLAG);
		wrapper.eq("id", id);
		return selectOne(wrapper);
	}
	/**
     * @date   @date 2018-10-09
     * @author zuoqb123
     * @todo   数据源配置分页查询
     */
	@Override
	public PageInfo<DbDatasourceConfig> pageList(BaseController c, HttpServletRequest request, DbDatasourceConfig entity,Integer pageNum,Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		EntityWrapper<DbDatasourceConfig> wrapper = searchWrapper(c,request, entity);
		List<DbDatasourceConfig> list = dbDatasourceConfigMapper.selectList(wrapper);
		PageInfo<DbDatasourceConfig> page = new PageInfo<DbDatasourceConfig>(list);
		return page;
	}
	
	 /**
     * @date 2018-10-09
     * @author zuoqb123
     * @todo   数据源配置构建查询条件-以后扩展
     */
    private EntityWrapper<DbDatasourceConfig> searchWrapper(BaseController c,HttpServletRequest request, DbDatasourceConfig entity) {
		EntityWrapper<DbDatasourceConfig> wrapper = new EntityWrapper<DbDatasourceConfig>();
		wrapper.where("del_flag={0}", UN_DEL_FLAG);
		if(c!=null&&request!=null&&c.getLoginUser(request)!=null&&StringUtils.isNotBlank(c.getLoginUser(request).getId())){
			if(!c.isAdmin(request))
			 wrapper.and("create_by", c.getLoginUser(request).getId());
		}
		//根据编号模糊查询
		if(entity.getId()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getId()))){
			wrapper.like("id", String.valueOf(entity.getId()));
		}
		//根据归属平台模糊查询
		if(entity.getSysPlatId()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getSysPlatId()))){
			wrapper.like("sys_plat_id", String.valueOf(entity.getSysPlatId()));
		}
		//根据数据源名称模糊查询
		if(entity.getName()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getName()))){
			wrapper.like("name", String.valueOf(entity.getName()));
		}
		//根据数据源英文名称模糊查询
		if(entity.getEnname()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getEnname()))){
			wrapper.like("enname", String.valueOf(entity.getEnname()));
		}
		//根据数据源类型模糊查询
		if(entity.getDbType()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getDbType()))){
			wrapper.like("db_type", String.valueOf(entity.getDbType()));
		}
		//根据数据源驱动模糊查询
		if(entity.getDbDiver()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getDbDiver()))){
			wrapper.like("db_diver", String.valueOf(entity.getDbDiver()));
		}
		//根据数据连接地址模糊查询
		if(entity.getDbUrl()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getDbUrl()))){
			wrapper.like("db_url", String.valueOf(entity.getDbUrl()));
		}
		//根据用户名模糊查询
		if(entity.getDbName()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getDbName()))){
			wrapper.like("db_name", String.valueOf(entity.getDbName()));
		}
		//根据连接密码模糊查询
		if(entity.getDbPassword()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getDbPassword()))){
			wrapper.like("db_password", String.valueOf(entity.getDbPassword()));
		}
		//根据数据库版本模糊查询
		if(entity.getDbVersion()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getDbVersion()))){
			wrapper.like("db_version", String.valueOf(entity.getDbVersion()));
		}
		//根据最大连接数模糊查询
		if(entity.getMaxNum()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getMaxNum()))){
			wrapper.like("max_num", String.valueOf(entity.getMaxNum()));
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
