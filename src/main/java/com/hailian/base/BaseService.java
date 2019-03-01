package com.hailian.base;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.github.pagehelper.PageInfo;
import com.hailian.entity.EtaxExcelFile;

/**
 * @time   2018年9月26日 下午5:34:21
 * @author zuoqb
 * @todo   公用Service
 */
public interface BaseService<T> extends IService<T> {
	 /**
     * @date   2018-09-27
     * @author zuoqb123
     * @todo   新增或者修改
     */
	boolean saveOrUpdate(T entity);
	/**
     * @date   2018-09-27
     * @author zuoqb123
     * @todo   逻辑删除
     */
	boolean deleteLogic(String id);
	/**
     * @date   2018-09-27
     * @author zuoqb123
     * @todo   单条数据查询
     */
	T getById(String id);
	/**
     * @date   2018-09-27
     * @author zuoqb123
     * @todo   分页查询
     */
	PageInfo<T> pageList(BaseController c,HttpServletRequest request,T entity,Integer pageNum,Integer pageSize);
}
