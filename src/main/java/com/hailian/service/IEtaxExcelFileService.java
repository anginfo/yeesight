package com.hailian.service;

import com.hailian.entity.EtaxExcelFile;
import com.hailian.base.BaseService;
import com.hailian.base.BaseController;
import javax.servlet.http.HttpServletRequest;
import com.github.pagehelper.PageInfo;

/**
 * @date 2018-11-18
 * @author zuoqb123
 * 保税处理之后的文件信息表表服务类
 */
public interface IEtaxExcelFileService extends BaseService<EtaxExcelFile> {
 	 /**
     * @date   2018-11-18
     * @author zuoqb123
     * @todo   保税处理之后的文件信息表表新增或者修改
     */
	boolean saveOrUpdate(EtaxExcelFile entity);
	/**
     * @date   2018-11-18
     * @author zuoqb123
     * @todo   保税处理之后的文件信息表表逻辑删除
     */
	boolean deleteLogic(String id);
	/**
     * @date   2018-11-18
     * @author zuoqb123
     * @todo   保税处理之后的文件信息表表单条数据查询
     */
	EtaxExcelFile getById(String id);
	/**
     * @date   2018-11-18
     * @author zuoqb123
     * @todo   保税处理之后的文件信息表表分页查询
     */
	PageInfo<EtaxExcelFile> pageList(BaseController c,HttpServletRequest request,EtaxExcelFile entity,Integer pageNum,Integer pageSize);
}
