package com.hailian.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.pagehelper.PageInfo;
import com.hailian.base.BaseController;
import com.hailian.base.BaseService;
import com.hailian.common.PublicResult;
import com.hailian.entity.CommonInterfaceExcelTable;


/**
 * @date 2018-10-11
 * @author zuoqb123
 * excel导出模板配置表服务类
 */
public interface ICommonInterfaceExcelTableService extends BaseService<CommonInterfaceExcelTable> {
 	 /**
     * @date   2018-10-11
     * @author zuoqb123
     * @todo   excel导出模板配置表新增或者修改
     */
	boolean saveOrUpdate(CommonInterfaceExcelTable entity);
	/**
     * @date   2018-10-11
     * @author zuoqb123
     * @todo   excel导出模板配置表逻辑删除
     */
	boolean deleteLogic(String id);
	/**
     * @date   2018-10-11
     * @author zuoqb123
     * @todo   excel导出模板配置表单条数据查询
     */
	CommonInterfaceExcelTable getById(String id);
	/**
     * @date   2018-10-11
     * @author zuoqb123
     * @todo   excel导出模板配置表分页查询
     */
	PageInfo<CommonInterfaceExcelTable> pageList(BaseController c,HttpServletRequest request,CommonInterfaceExcelTable entity,Integer pageNum,Integer pageSize);
	/**
	 * 
	 * @time   2018年10月12日 下午3:53:36
	 * @author zuoqb
	 * @todo   导出excel
	 * @return_type   PublicResult<?>
	 */
	PublicResult<?> exportExcel(HttpServletRequest request, HttpServletResponse response, String id,
			String params, String dataSpaceStr);
}
