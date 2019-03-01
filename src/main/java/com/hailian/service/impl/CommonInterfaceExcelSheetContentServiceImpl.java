package com.hailian.service.impl;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
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
import com.hailian.entity.CommonInterfaceExcelSheetContent;
import com.hailian.mapper.CommonInterfaceExcelSheetContentMapper;
import com.hailian.service.ICommonInterfaceExcelSheetContentService;
/**
 * @date 2018-10-11
 * @author zuoqb123
 * Excel导出sheet详情配置服务实现类
 */
@Service
@Transactional
public class CommonInterfaceExcelSheetContentServiceImpl extends BaseServiceImpl<CommonInterfaceExcelSheetContentMapper, CommonInterfaceExcelSheetContent> implements ICommonInterfaceExcelSheetContentService,Constant {

    @Autowired
    private CommonInterfaceExcelSheetContentMapper commonInterfaceExcelSheetContentMapper;
    
     /**
     * @date 2018-10-11
     * @author zuoqb123
     * @todo   Excel导出sheet详情配置新增或者修改
     */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean saveOrUpdate(CommonInterfaceExcelSheetContent entity) {
		if(StringUtils.isBlank(entity.getId())){
			//新增
			entity.setId(UUIDUtils.getUuid());
			entity.setCreateDate(new Date());
			return commonInterfaceExcelSheetContentMapper.insert(entity)>0;
		}else{
			entity.setUpdateDate(new Date());
			return commonInterfaceExcelSheetContentMapper.updateById(entity)>0;
		}
	}
	/**
     * @date 2018-10-11
     * @author zuoqb123
     * @todo   Excel导出sheet详情配置逻辑删除
     */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteLogic(String id) {
		CommonInterfaceExcelSheetContent entity=new CommonInterfaceExcelSheetContent();
		entity.setId(id);
		entity.setDelFlag(DEL_FLAG);
		entity.setUpdateDate(new Date());
		return commonInterfaceExcelSheetContentMapper.updateById(entity)>0;
	}
	/**
     * @date 2018-10-11
     * @author zuoqb123
     * @todo   Excel导出sheet详情配置单条数据查询
     */
	@Override
	public CommonInterfaceExcelSheetContent getById(String id) {
		EntityWrapper<CommonInterfaceExcelSheetContent> wrapper = new EntityWrapper<CommonInterfaceExcelSheetContent>();
		wrapper.where("del_flag={0}",UN_DEL_FLAG);
		wrapper.eq("id", id);
		return selectOne(wrapper);
	}
	/**
     * @date   @date 2018-10-11
     * @author zuoqb123
     * @todo   Excel导出sheet详情配置分页查询
     */
	@Override
	public PageInfo<CommonInterfaceExcelSheetContent> pageList(BaseController c, HttpServletRequest request, CommonInterfaceExcelSheetContent entity,Integer pageNum,Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		EntityWrapper<CommonInterfaceExcelSheetContent> wrapper = searchWrapper(c,request, entity);
		List<CommonInterfaceExcelSheetContent> list = commonInterfaceExcelSheetContentMapper.selectPage(new RowBounds((pageNum-1)*pageSize, pageSize),wrapper);
		PageInfo<CommonInterfaceExcelSheetContent> page = new PageInfo<CommonInterfaceExcelSheetContent>(list);
		page.setTotal(commonInterfaceExcelSheetContentMapper.selectCount(wrapper));
		return page;
	}
	
	 /**
     * @date 2018-10-11
     * @author zuoqb123
     * @todo   Excel导出sheet详情配置构建查询条件-以后扩展
     */
    private EntityWrapper<CommonInterfaceExcelSheetContent> searchWrapper(BaseController c,HttpServletRequest request, CommonInterfaceExcelSheetContent entity) {
		EntityWrapper<CommonInterfaceExcelSheetContent> wrapper = new EntityWrapper<CommonInterfaceExcelSheetContent>();
		wrapper.where("del_flag={0}", UN_DEL_FLAG);
		if(c!=null&&request!=null&&c.getLoginUser(request)!=null&&StringUtils.isNotBlank(c.getLoginUser(request).getId())){
			if(!c.isAdmin(request))
			 wrapper.and("create_by", c.getLoginUser(request).getId());
		}
		//根据编码模糊查询
		if(entity.getId()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getId()))){
			wrapper.like("id", String.valueOf(entity.getId()));
		}
		//根据行位置模糊查询
		if(entity.getXIp()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getXIp()))){
			wrapper.like("x_ip", String.valueOf(entity.getXIp()));
		}
		//根据列位置模糊查询
		if(entity.getYIp()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getYIp()))){
			wrapper.like("y_ip", String.valueOf(entity.getYIp()));
		}
		//根据对应sheet表ID模糊查询
		if(entity.getSheetId()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getSheetId()))){
			wrapper.like("sheet_id", String.valueOf(entity.getSheetId()));
		}
		//根据对应列名称模糊查询
		if(entity.getColumnName()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getColumnName()))){
			wrapper.like("column_name", String.valueOf(entity.getColumnName()));
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
    /**
	 * @time   2018年10月11日 下午9:11:18
	 * @author zuoqb
	 * @todo   根据sheet_id查询内容
	 */
	@Override
	public List<CommonInterfaceExcelSheetContent> getContentBySheetId(String sheetId,String sortBy,boolean isAsc) {
		CommonInterfaceExcelSheetContent entity=new CommonInterfaceExcelSheetContent();
		entity.setSheetId(sheetId);
		if(StringUtils.isBlank(sortBy)){
			sortBy="x_ip";
		}
		entity.setOrderBy(sortBy);
		entity.setAsc(isAsc);
		EntityWrapper<CommonInterfaceExcelSheetContent> wrapper = searchWrapper(null,null, entity);
		return commonInterfaceExcelSheetContentMapper.selectList(wrapper);
	}
   
}
