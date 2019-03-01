package com.hailian.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.github.pagehelper.PageInfo;
import com.hailian.base.BaseController;
import com.hailian.base.BaseService;
import com.hailian.entity.CommonInterfaceExcelSheetContent;


/**
 * @date 2018-10-11
 * @author zuoqb123
 * Excel导出sheet详情配置服务类
 */
public interface ICommonInterfaceExcelSheetContentService extends BaseService<CommonInterfaceExcelSheetContent> {
 	 /**
     * @date   2018-10-11
     * @author zuoqb123
     * @todo   Excel导出sheet详情配置新增或者修改
     */
	boolean saveOrUpdate(CommonInterfaceExcelSheetContent entity);
	/**
     * @date   2018-10-11
     * @author zuoqb123
     * @todo   Excel导出sheet详情配置逻辑删除
     */
	boolean deleteLogic(String id);
	/**
     * @date   2018-10-11
     * @author zuoqb123
     * @todo   Excel导出sheet详情配置单条数据查询
     */
	CommonInterfaceExcelSheetContent getById(String id);
	/**
     * @date   2018-10-11
     * @author zuoqb123
     * @todo   Excel导出sheet详情配置分页查询
     */
	PageInfo<CommonInterfaceExcelSheetContent> pageList(BaseController c,HttpServletRequest request,CommonInterfaceExcelSheetContent entity,Integer pageNum,Integer pageSize);
	
	/**
	 * @time   2018年10月11日 下午9:11:18
	 * @author zuoqb
	 * @todo   根据sheet_id查询内容
	 */
	List<CommonInterfaceExcelSheetContent> getContentBySheetId(String sheetId,String sortBy,boolean isAsc);
}
