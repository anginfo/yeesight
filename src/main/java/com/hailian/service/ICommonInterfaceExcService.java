package com.hailian.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.github.pagehelper.PageInfo;
import com.hailian.base.BaseController;
import com.hailian.base.BaseService;
import com.hailian.common.PublicResult;
import com.hailian.entity.CommonInterfaceExc;


/**
 * @date 2018-10-09
 * @author zuoqb123
 * 统一接口服务类
 */
public interface ICommonInterfaceExcService extends BaseService<CommonInterfaceExc> {
 	 /**
     * @date   2018-10-09
     * @author zuoqb123
     * @todo   统一接口新增或者修改
     */
	boolean saveOrUpdate(CommonInterfaceExc entity);
	/**
     * @date   2018-10-09
     * @author zuoqb123
     * @todo   统一接口逻辑删除
     */
	boolean deleteLogic(String id);
	/**
     * @date   2018-10-09
     * @author zuoqb123
     * @todo   统一接口单条数据查询
     */
	CommonInterfaceExc getById(String id);
	/**
     * @date   2018-10-09
     * @author zuoqb123
     * @todo   统一接口分页查询
     */
	PageInfo<CommonInterfaceExc> pageList(BaseController c,HttpServletRequest request,CommonInterfaceExc entity,Integer pageNum,Integer pageSize);
	/**
	 * @time   2018年10月11日 下午9:11:18
	 * @author zuoqb
	 * @todo   根据dataType、dataSpace查询内容
	 */
	List<CommonInterfaceExc> getInterfaceByDataTypeAndDataSpace(String dataType,String dataSpace);
	PublicResult<?> execeSqlVertical(String sql, String dataSourceId, List<String> matcher,
			PublicResult<Map<String, String>> dealParamsResult,HttpServletRequest request,String methodName,String tranType);
}
