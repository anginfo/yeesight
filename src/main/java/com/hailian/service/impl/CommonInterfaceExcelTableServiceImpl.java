package com.hailian.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hailian.base.BaseController;
import com.hailian.base.BaseServiceImpl;
import com.hailian.common.PublicResult;
import com.hailian.common.UUIDUtils;
import com.hailian.conf.Constant;
import com.hailian.entity.CommonInterfaceExc;
import com.hailian.entity.CommonInterfaceExcelSheet;
import com.hailian.entity.CommonInterfaceExcelSheetContent;
import com.hailian.entity.CommonInterfaceExcelTable;
import com.hailian.enums.PublicResultConstant;
import com.hailian.mapper.CommonInterfaceExcelTableMapper;
import com.hailian.service.ICommonInterfaceExcService;
import com.hailian.service.ICommonInterfaceExcelSheetService;
import com.hailian.service.ICommonInterfaceExcelTableService;
import com.hailian.utils.JdbcUtil;
import com.hailian.utils.RexUtils;
/**
 * @date 2018-10-11
 * @author zuoqb123
 * excel导出模板配置表服务实现类
 */
@Service
@Transactional
public class CommonInterfaceExcelTableServiceImpl extends BaseServiceImpl<CommonInterfaceExcelTableMapper, CommonInterfaceExcelTable> implements ICommonInterfaceExcelTableService,Constant {

    @Autowired
    private CommonInterfaceExcelTableMapper commonInterfaceExcelTableMapper;
    @Autowired
    public ICommonInterfaceExcelSheetService iCommonInterfaceExcelSheetService;
    @Autowired
    public ICommonInterfaceExcService iCommonInterfaceExcService;
     /**
     * @date 2018-10-11
     * @author zuoqb123
     * @todo   excel导出模板配置表新增或者修改
     */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean saveOrUpdate(CommonInterfaceExcelTable entity) {
		if(StringUtils.isBlank(entity.getId())){
			//新增
			entity.setId(UUIDUtils.getUuid());
			entity.setCreateDate(new Date());
			return commonInterfaceExcelTableMapper.insert(entity)>0;
		}else{
			entity.setUpdateDate(new Date());
			return commonInterfaceExcelTableMapper.updateById(entity)>0;
		}
	}
	/**
     * @date 2018-10-11
     * @author zuoqb123
     * @todo   excel导出模板配置表逻辑删除
     */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteLogic(String id) {
		CommonInterfaceExcelTable entity=new CommonInterfaceExcelTable();
		entity.setId(id);
		entity.setDelFlag(DEL_FLAG);
		entity.setUpdateDate(new Date());
		return commonInterfaceExcelTableMapper.updateById(entity)>0;
	}
	/**
     * @date 2018-10-11
     * @author zuoqb123
     * @todo   excel导出模板配置表单条数据查询
     */
	@Override
	public CommonInterfaceExcelTable getById(String id) {
		EntityWrapper<CommonInterfaceExcelTable> wrapper = new EntityWrapper<CommonInterfaceExcelTable>();
		wrapper.where("del_flag={0}",UN_DEL_FLAG);
		wrapper.eq("id", id);
		CommonInterfaceExcelTable table=selectOne(wrapper);
		if(table!=null){
			table.setSheets(iCommonInterfaceExcelSheetService.getSheetByTableId(table.getId(), null, true));
		}
		return table;
	}
	/**
     * @date   @date 2018-10-11
     * @author zuoqb123
     * @todo   excel导出模板配置表分页查询
     */
	@Override
	public PageInfo<CommonInterfaceExcelTable> pageList(BaseController c, HttpServletRequest request, CommonInterfaceExcelTable entity,Integer pageNum,Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		EntityWrapper<CommonInterfaceExcelTable> wrapper = searchWrapper(c,request, entity);
		List<CommonInterfaceExcelTable> list = commonInterfaceExcelTableMapper.selectPage(new RowBounds((pageNum-1)*pageSize, pageSize),wrapper);
		PageInfo<CommonInterfaceExcelTable> page = new PageInfo<CommonInterfaceExcelTable>(list);
		page.setTotal(commonInterfaceExcelTableMapper.selectCount(wrapper));
		return page;
	}
	
	 /**
     * @date 2018-10-11
     * @author zuoqb123
     * @todo   excel导出模板配置表构建查询条件-以后扩展
     */
    private EntityWrapper<CommonInterfaceExcelTable> searchWrapper(BaseController c,HttpServletRequest request, CommonInterfaceExcelTable entity) {
		EntityWrapper<CommonInterfaceExcelTable> wrapper = new EntityWrapper<CommonInterfaceExcelTable>();
		wrapper.where("del_flag={0}", UN_DEL_FLAG);
		if(c!=null&&request!=null&&c.getLoginUser(request)!=null&&StringUtils.isNotBlank(c.getLoginUser(request).getId())){
			if(!c.isAdmin(request))
			 wrapper.and("create_by", c.getLoginUser(request).getId());
		}
		//根据唯一标识模糊查询
		if(entity.getId()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getId()))){
			wrapper.like("id", String.valueOf(entity.getId()));
		}
		//根据表名称模糊查询
		if(entity.getName()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getName()))){
			wrapper.like("name", String.valueOf(entity.getName()));
		}
		//根据excel模板系统路径模糊查询
		if(entity.getTempletPath()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getTempletPath()))){
			wrapper.like("templet_path", String.valueOf(entity.getTempletPath()));
		}
		//根据查询指标标识模糊查询
		if(entity.getDataType()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getDataType()))){
			wrapper.like("data_type", String.valueOf(entity.getDataType()));
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
	 * 
	 * @time   2018年10月12日 下午3:53:36
	 * @author zuoqb
	 * @todo   导出excel
	 * @return_type   PublicResult<?>
	 */
	@SuppressWarnings({ "unchecked", "resource" })
	@Override
	public PublicResult<?> exportExcel(HttpServletRequest request, HttpServletResponse response, String id,
			String params, String dataSpaceStr) {
		try {
  			CommonInterfaceExcelTable table=getById(id);
  			ByteArrayOutputStream stream = new ByteArrayOutputStream();
  			FileInputStream fi = null;
  			XSSFWorkbook wb = null;
  			XSSFRow row = null;
  			XSSFCell cell = null;
  			byte[] buff = null;
  			List<String> dataSpaces=new ArrayList<String>();//要导出的dataSpace
  			if(StringUtils.isNotBlank(dataSpaceStr)){
  				dataSpaces=Arrays.asList(dataSpaceStr.split(","));
  			}
  			if(table==null){
  				return new PublicResult<>(PublicResultConstant.NO_DATA,"没有Excel配置", null);
  			}
  			if(StringUtils.isBlank(table.getTempletPath())){
  				return new PublicResult<>(PublicResultConstant.NO_DATA,"无配置模板", null);
  			}
  			if(StringUtils.isBlank(table.getDataType())){
  				return new PublicResult<>(PublicResultConstant.NO_DATA,"无dataType配置信息", null);
  			}
  			fi = new FileInputStream(table.getTempletPath().trim());
			if(fi.getChannel().size() == 0){
				return new PublicResult<>(PublicResultConstant.NO_DATA,"模板文件未找到", null);
			}
			wb = new XSSFWorkbook(fi);//xslx
			//最终要导出的sheets页
			List<CommonInterfaceExcelSheet> sheets=new ArrayList<CommonInterfaceExcelSheet>();
			//如果没指定dataSpace，默认导出所有的配置
			if(dataSpaces == null || dataSpaces.size() == 0){
				for(CommonInterfaceExcelSheet sheet:table.getSheets()){
					dataSpaces.add(sheet.getDataSpace());
				}
			}
			//只要固定的dataSpace
			for(String dataSpace:dataSpaces){
				for(CommonInterfaceExcelSheet sheet:table.getSheets()){
					if(dataSpace.equals(sheet.getDataSpace())){
						sheets.add(sheet);
					}
				}
			}
			if(dataSpaces==null||dataSpaces.size()==0||sheets.size()==0){
				return new PublicResult<>(PublicResultConstant.ERROR,"Excel配置信息不全", null);
			}
			List<CommonInterfaceExc> interfaces = iCommonInterfaceExcService.getInterfaceByDataTypeAndDataSpace(table.getDataType(), null);
			if(interfaces==null||interfaces.size()==0){
				return new PublicResult<>(PublicResultConstant.ERROR,"统一接口配置错误", null);
			}
			for(int s=0; s<dataSpaces.size(); s++){
				CommonInterfaceExcelSheet st = sheets.get(s);
				CommonInterfaceExc commonInterfaceExc=null;
				//获取当前dataSpace统一接口配置信息
				for(CommonInterfaceExc inter:interfaces){
					if(dataSpaces.get(s).equals(inter.getDataSpace())){
						commonInterfaceExc=inter;
						break;
					}
				}
				if(commonInterfaceExc==null){
					return new PublicResult<>(PublicResultConstant.ERROR,"统一接口配置错误!", null);
				}
				//根据统一接口查询数据
				String sql=commonInterfaceExc.getDataSql();
	        	//解析SQL中的${}与#{}
	        	//匹配所有${}与匹配所有#{}正则表达式
	        	String rexg="\\#\\{([^\\}]+)\\}|\\$\\{([^\\}]+)\\}";
	        	//按照顺序查找出所有# $ 比如  [#{time}, #{cbkCode}, ${startIndex}, ${pageSize}]
	        	List<String> matcher=RexUtils.getString(sql, rexg);
	        	PublicResult<Map<String,String>> dealParamsResult=JdbcUtil.dealParams(params);
	        	PublicResult<Map<String, List<Map<String, Object>>>> formatSqlResult= JdbcUtil.formatSql(sql, matcher, dealParamsResult);
	        	if(!PublicResultConstant.SUCCESS.msg.equals(formatSqlResult.getMsg())){
	        		return formatSqlResult;
	        	}
	        	//将SQL中所有${}与所有#{}替换为？
	        	sql=RexUtils.getReplace(sql, rexg,"?");
	        	/**
	        	 * 处理SQL结束
	        	 */
	        	String dataSourceId=commonInterfaceExc.getDbDatasourceId();
	        	PublicResult<?> execeResult=iCommonInterfaceExcService.execeSqlVertical(sql, dataSourceId, matcher, dealParamsResult,request,null,"H");
	        	if(!PublicResultConstant.SUCCESS.msg.equals(execeResult.getMsg())){
	        		return new PublicResult<>(PublicResultConstant.PARAM_ERROR,execeResult.getErrorMsg(), null);
	        	}
	        	Map<String, List<Object>> horizontalData=(Map<String, List<Object>>) execeResult.getData();//当前sheet数据
	        	
	        	// 生成一个表格
				XSSFSheet sheet = wb.getSheet(st.getName());
	        	List<CommonInterfaceExcelSheetContent> contents = st.getSheetContens();
	        	for(int c =0; c<contents.size(); c++){
	        		CommonInterfaceExcelSheetContent content = contents.get(c);
					List<Object> vals = horizontalData.get(contents.get(c).getColumnName());
					for(int v =0; v<vals.size(); v++){    //放值
						//填写内容的坐标
						row = sheet.getRow(content.getYIp() + v);
						if(row == null){
							row = sheet.createRow(content.getYIp() + v);
						}
						cell = row.getCell(content.getXIp());
						if(cell == null){
							cell = row.createCell(content.getXIp());
						}
						cell.setCellValue(String.valueOf(vals.get(v)));
					}
				}
	        	
			}
			try {
				wb.write(stream);//将excel写入流
				buff = stream.toByteArray();
				stream.flush();
				stream.close();
				fi.close();
			} catch (Exception e) {
				e.printStackTrace();
				wb = null;
				stream = null;
				fi = null;
			}
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("excelObject",buff);
			result.put("tableName",table.getName());
  			return new PublicResult<>(PublicResultConstant.SUCCESS, result);
		} catch (Exception e) {
			e.printStackTrace();
			return new PublicResult<>(PublicResultConstant.FAILED,e.getMessage(), null);
		}
	}
   
}
