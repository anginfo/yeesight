package com.hailian.service;

import javax.servlet.http.HttpServletRequest;

import com.github.pagehelper.PageInfo;
import com.hailian.base.BaseController;
import com.hailian.base.BaseService;
import com.hailian.entity.DbDatasourceConfig;


/**
 * @date 2018-10-09
 * @author zuoqb123
 * 数据源配置服务类
 */
public interface IDbDatasourceConfigService extends BaseService<DbDatasourceConfig> {
 	 /**
     * @date   2018-10-09
     * @author zuoqb123
     * @todo   数据源配置新增或者修改
     */
	boolean saveOrUpdate(DbDatasourceConfig entity);
	/**
     * @date   2018-10-09
     * @author zuoqb123
     * @todo   数据源配置逻辑删除
     */
	boolean deleteLogic(String id);
	/**
     * @date   2018-10-09
     * @author zuoqb123
     * @todo   数据源配置单条数据查询
     */
	DbDatasourceConfig getById(String id);
	/**
     * @date   2018-10-09
     * @author zuoqb123
     * @todo   数据源配置分页查询
     */
	PageInfo<DbDatasourceConfig> pageList(BaseController c,HttpServletRequest request,DbDatasourceConfig entity,Integer pageNum,Integer pageSize);
}
