package com.hailian.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.hailian.entity.SysOperationLog;
import com.hailian.base.BaseService;
import com.baomidou.mybatisplus.plugins.Page;
import com.hailian.base.BaseController;

import javax.servlet.http.HttpServletRequest;


/**
 * @date 2018-10-09
 * @author zuoqb123
 * 操作日志服务类
 */
public interface ISysOperationLogService extends BaseService<SysOperationLog> {
 	 /**
     * @date   2018-10-09
     * @author zuoqb123
     * @todo   操作日志新增或者修改
     */
	boolean saveOrUpdate(SysOperationLog entity);
	/**
     * @date   2018-10-09
     * @author zuoqb123
     * @todo   操作日志逻辑删除
     */
	boolean deleteLogic(String id);
	/**
     * @date   2018-10-09
     * @author zuoqb123
     * @todo   操作日志单条数据查询
     */
	SysOperationLog getById(String id);
	/**
     * @date   2018-10-09
     * @author zuoqb123
     * @todo   操作日志分页查询
     */
	PageInfo<SysOperationLog> pageList(BaseController c,HttpServletRequest request,SysOperationLog entity,Integer pageNum,Integer pageSize);
	List<SysOperationLog> dateList();
}
