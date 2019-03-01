package com.hailian.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.github.pagehelper.PageInfo;
import com.hailian.base.BaseController;
import com.hailian.base.BaseService;
import com.hailian.entity.CommonInterfaceExcelSheet;


/**
 * @date 2018-10-11
 * @author zuoqb123
 * Excel导出sheet也配置表服务类
 */
public interface ICommonInterfaceExcelSheetService extends BaseService<CommonInterfaceExcelSheet> {
 	 /**
     * @date   2018-10-11
     * @author zuoqb123
     * @todo   Excel导出sheet也配置表新增或者修改
     */
	boolean saveOrUpdate(CommonInterfaceExcelSheet entity);
	/**
     * @date   2018-10-11
     * @author zuoqb123
     * @todo   Excel导出sheet也配置表逻辑删除
     */
	boolean deleteLogic(String id);
	/**
     * @date   2018-10-11
     * @author zuoqb123
     * @todo   Excel导出sheet也配置表单条数据查询
     */
	CommonInterfaceExcelSheet getById(String id);
	/**
     * @date   2018-10-11
     * @author zuoqb123
     * @todo   Excel导出sheet也配置表分页查询
     */
	PageInfo<CommonInterfaceExcelSheet> pageList(BaseController c,HttpServletRequest request,CommonInterfaceExcelSheet entity,Integer pageNum,Integer pageSize);
	
	/**
	 * @time   2018年10月11日 下午9:11:18
	 * @author zuoqb
	 * @todo   根据table_id查询sheet内容
	 */
	List<CommonInterfaceExcelSheet> getSheetByTableId(String tableId,String sortBy,boolean isAsc);
}
