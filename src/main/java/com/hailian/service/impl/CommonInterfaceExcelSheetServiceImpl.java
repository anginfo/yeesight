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
import com.hailian.entity.CommonInterfaceExcelSheet;
import com.hailian.mapper.CommonInterfaceExcelSheetMapper;
import com.hailian.service.ICommonInterfaceExcelSheetContentService;
import com.hailian.service.ICommonInterfaceExcelSheetService;
/**
 * @date 2018-10-11
 * @author zuoqb123
 * Excel导出sheet也配置表服务实现类
 */
@Service
@Transactional
public class CommonInterfaceExcelSheetServiceImpl extends BaseServiceImpl<CommonInterfaceExcelSheetMapper, CommonInterfaceExcelSheet> implements ICommonInterfaceExcelSheetService,Constant {

    @Autowired
    private CommonInterfaceExcelSheetMapper commonInterfaceExcelSheetMapper;
    @Autowired
    public ICommonInterfaceExcelSheetContentService iCommonInterfaceExcelSheetContentService;
    
    
     /**
     * @date 2018-10-11
     * @author zuoqb123
     * @todo   Excel导出sheet也配置表新增或者修改
     */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean saveOrUpdate(CommonInterfaceExcelSheet entity) {
		if(StringUtils.isBlank(entity.getId())){
			//新增
			entity.setId(UUIDUtils.getUuid());
			entity.setCreateDate(new Date());
			return commonInterfaceExcelSheetMapper.insert(entity)>0;
		}else{
			entity.setUpdateDate(new Date());
			return commonInterfaceExcelSheetMapper.updateById(entity)>0;
		}
	}
	/**
     * @date 2018-10-11
     * @author zuoqb123
     * @todo   Excel导出sheet也配置表逻辑删除
     */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteLogic(String id) {
		CommonInterfaceExcelSheet entity=new CommonInterfaceExcelSheet();
		entity.setId(id);
		entity.setDelFlag(DEL_FLAG);
		entity.setUpdateDate(new Date());
		return commonInterfaceExcelSheetMapper.updateById(entity)>0;
	}
	/**
     * @date 2018-10-11
     * @author zuoqb123
     * @todo   Excel导出sheet也配置表单条数据查询
     */
	@Override
	public CommonInterfaceExcelSheet getById(String id) {
		EntityWrapper<CommonInterfaceExcelSheet> wrapper = new EntityWrapper<CommonInterfaceExcelSheet>();
		wrapper.where("del_flag={0}",UN_DEL_FLAG);
		wrapper.eq("id", id);
		CommonInterfaceExcelSheet sheet=selectOne(wrapper);
		if(sheet!=null){
			//查询sheet内容
			sheet.setSheetContens(iCommonInterfaceExcelSheetContentService.getContentBySheetId(sheet.getId(), null, true));
		}
		return sheet;
	}
	/**
     * @date   @date 2018-10-11
     * @author zuoqb123
     * @todo   Excel导出sheet也配置表分页查询
     */
	@Override
	public PageInfo<CommonInterfaceExcelSheet> pageList(BaseController c, HttpServletRequest request, CommonInterfaceExcelSheet entity,Integer pageNum,Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		EntityWrapper<CommonInterfaceExcelSheet> wrapper = searchWrapper(c,request, entity);
		List<CommonInterfaceExcelSheet> list = commonInterfaceExcelSheetMapper.selectPage(new RowBounds((pageNum-1)*pageSize, pageSize),wrapper);
		PageInfo<CommonInterfaceExcelSheet> page = new PageInfo<CommonInterfaceExcelSheet>(list);
		page.setTotal(commonInterfaceExcelSheetMapper.selectCount(wrapper));
		return page;
	}
	
	 /**
     * @date 2018-10-11
     * @author zuoqb123
     * @todo   Excel导出sheet也配置表构建查询条件-以后扩展
     */
    private EntityWrapper<CommonInterfaceExcelSheet> searchWrapper(BaseController c,HttpServletRequest request, CommonInterfaceExcelSheet entity) {
		EntityWrapper<CommonInterfaceExcelSheet> wrapper = new EntityWrapper<CommonInterfaceExcelSheet>();
		wrapper.where("del_flag={0}", UN_DEL_FLAG);
		if(c!=null&&request!=null&&c.getLoginUser(request)!=null&&StringUtils.isNotBlank(c.getLoginUser(request).getId())){
			if(!c.isAdmin(request))
			 wrapper.and("create_by", c.getLoginUser(request).getId());
		}
		//根据编码模糊查询
		if(entity.getId()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getId()))){
			wrapper.like("id", String.valueOf(entity.getId()));
		}
		//根据标签名称模糊查询
		if(entity.getName()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getName()))){
			wrapper.like("name", String.valueOf(entity.getName()));
		}
		//根据对应表ID模糊查询
		if(entity.getTableId()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getTableId()))){
			wrapper.like("table_id", String.valueOf(entity.getTableId()));
		}
		//根据命名空间模糊查询
		if(entity.getDataSpace()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getDataSpace()))){
			wrapper.like("data_space", String.valueOf(entity.getDataSpace()));
		}
		//根据排序模糊查询
		if(entity.getOrderNo()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getOrderNo()))){
			wrapper.like("order_no", String.valueOf(entity.getOrderNo()));
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
	 * @todo   根据table_id查询sheet内容
	 */
	@Override
	public List<CommonInterfaceExcelSheet> getSheetByTableId(String tableId, String sortBy, boolean isAsc) {
		CommonInterfaceExcelSheet entity=new CommonInterfaceExcelSheet();
		entity.setTableId(tableId);
		if(StringUtils.isBlank(sortBy)){
			sortBy="order_no";
		}
		entity.setOrderBy(sortBy);
		entity.setAsc(isAsc);
		EntityWrapper<CommonInterfaceExcelSheet> wrapper = searchWrapper(null,null, entity);
		List<CommonInterfaceExcelSheet> sheets=commonInterfaceExcelSheetMapper.selectList(wrapper);
		for(CommonInterfaceExcelSheet sheet:sheets){
			sheet.setSheetContens(iCommonInterfaceExcelSheetContentService.getContentBySheetId(sheet.getId(), null, true));
		}
		return sheets;
	}
   
}
