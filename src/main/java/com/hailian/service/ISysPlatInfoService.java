package com.hailian.service;

import javax.servlet.http.HttpServletRequest;

import com.github.pagehelper.PageInfo;
import com.hailian.base.BaseController;
import com.hailian.base.BaseService;
import com.hailian.entity.SysPlatInfo;


/**
 * @date 2018-10-09
 * @author zuoqb123
 * 平台信息服务类
 */
public interface ISysPlatInfoService extends BaseService<SysPlatInfo> {
 	 /**
     * @date   2018-10-09
     * @author zuoqb123
     * @todo   平台信息新增或者修改
     */
	boolean saveOrUpdate(SysPlatInfo entity);
	/**
     * @date   2018-10-09
     * @author zuoqb123
     * @todo   平台信息逻辑删除
     */
	boolean deleteLogic(String id);
	/**
     * @date   2018-10-09
     * @author zuoqb123
     * @todo   平台信息单条数据查询
     */
	SysPlatInfo getById(String id);
	/**
     * @date   2018-10-09
     * @author zuoqb123
     * @todo   平台信息分页查询
     */
	PageInfo<SysPlatInfo> pageList(BaseController c,HttpServletRequest request,SysPlatInfo entity,Integer pageNum,Integer pageSize);
}
