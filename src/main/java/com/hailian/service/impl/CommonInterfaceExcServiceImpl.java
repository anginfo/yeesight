package com.hailian.service.impl;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hailian.annotation.AuthPower;
import com.hailian.base.BaseController;
import com.hailian.base.BaseServiceImpl;
import com.hailian.common.PublicResult;
import com.hailian.common.UUIDUtils;
import com.hailian.conf.Constant;
import com.hailian.controller.CommonInterfaceController;
import com.hailian.entity.CommonInterfaceExc;
import com.hailian.entity.DbDatasourceConfig;
import com.hailian.entity.SysPlatInfo;
import com.hailian.enums.PublicResultConstant;
import com.hailian.interceptors.AppInterceptors;
import com.hailian.mapper.CommonInterfaceExcMapper;
import com.hailian.service.ICommonInterfaceExcService;
import com.hailian.service.IDbDatasourceConfigService;
import com.hailian.service.ISysPlatInfoService;
import com.hailian.utils.JdbcUtil;
/**
 * @date 2018-10-09
 * @author zuoqb123
 * 统一接口服务实现类
 */
@Service
@Transactional
public class CommonInterfaceExcServiceImpl extends BaseServiceImpl<CommonInterfaceExcMapper, CommonInterfaceExc> implements ICommonInterfaceExcService,Constant {

    @Autowired
    private CommonInterfaceExcMapper commonInterfaceExcMapper;
    @Autowired
	public JdbcTemplate jdbcTemplate;
    @Autowired
    public IDbDatasourceConfigService iDbDatasourceConfigService;
    @Autowired
    public ISysPlatInfoService iSysPlatInfoService;
    
     /**
     * @date 2018-10-09
     * @author zuoqb123
     * @todo   统一接口新增或者修改
     */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean saveOrUpdate(CommonInterfaceExc entity) {
		if(StringUtils.isBlank(entity.getId())){
			//新增
			entity.setId(UUIDUtils.getUuid());
			entity.setCreateDate(new Date());
			return commonInterfaceExcMapper.insert(entity)>0;
		}else{
			entity.setUpdateDate(new Date());
			return commonInterfaceExcMapper.updateById(entity)>0;
		}
	}
	/**
     * @date 2018-10-09
     * @author zuoqb123
     * @todo   统一接口逻辑删除
     */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteLogic(String id) {
		CommonInterfaceExc entity=new CommonInterfaceExc();
		entity.setId(id);
		entity.setDelFlag(DEL_FLAG);
		entity.setUpdateDate(new Date());
		return commonInterfaceExcMapper.updateById(entity)>0;
	}
	/**
     * @date 2018-10-09
     * @author zuoqb123
     * @todo   统一接口单条数据查询
     */
	@Override
	public CommonInterfaceExc getById(String id) {
		EntityWrapper<CommonInterfaceExc> wrapper = new EntityWrapper<CommonInterfaceExc>();
		wrapper.where("del_flag={0}",UN_DEL_FLAG);
		wrapper.eq("id", id);
		return selectOne(wrapper);
	}
	/**
     * @date   @date 2018-10-09
     * @author zuoqb123
     * @todo   统一接口分页查询
     */
	@Override
	public PageInfo<CommonInterfaceExc> pageList(BaseController c, HttpServletRequest request, CommonInterfaceExc entity,Integer pageNum,Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		EntityWrapper<CommonInterfaceExc> wrapper = searchWrapper(c,request, entity);
		List<CommonInterfaceExc> list = commonInterfaceExcMapper.selectPage(new RowBounds((pageNum-1)*pageSize, pageSize),wrapper);
		PageInfo<CommonInterfaceExc> page = new PageInfo<CommonInterfaceExc>(list);
		page.setTotal(commonInterfaceExcMapper.selectCount(wrapper));
		return page;
	}
	 /**
     * @date 2018-10-09
     * @author zuoqb123
     * @todo   统一接口构建查询条件-以后扩展
     */
    private EntityWrapper<CommonInterfaceExc> searchWrapper(BaseController c,HttpServletRequest request, CommonInterfaceExc entity) {
		EntityWrapper<CommonInterfaceExc> wrapper = new EntityWrapper<CommonInterfaceExc>();
		wrapper.where("del_flag={0}", UN_DEL_FLAG);
		if(c!=null&&request!=null&&c.getLoginUser(request)!=null&&StringUtils.isNotBlank(c.getLoginUser(request).getId())){
			if(!c.isAdmin(request))
			 wrapper.and("create_by", c.getLoginUser(request).getId());
		}
		//根据模糊查询
		if(entity.getId()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getId()))){
			wrapper.like("id", String.valueOf(entity.getId()));
		}
		//根据归属数据源 默认当前数据库模糊查询
		if(entity.getDbDatasourceId()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getDbDatasourceId()))){
			wrapper.like("db_datasource_id", String.valueOf(entity.getDbDatasourceId()));
		}
		//根据查询指标标识模糊查询
		if(entity.getDataType()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getDataType()))){
			wrapper.like("data_type", String.valueOf(entity.getDataType()));
		}
		//根据命名空间模糊查询
		if(entity.getDataSpace()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getDataSpace()))){
			wrapper.like("data_space", String.valueOf(entity.getDataSpace()));
		}
		//根据需要执行的sql语句，需要传参的位置使用#{参数名}，动态日期类型date_dt_kpi固定参数名称。模糊查询
		if(entity.getDataSql()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getDataSql()))){
			wrapper.like("data_sql", String.valueOf(entity.getDataSql()));
		}
		//根据横竖数据格式转换，默认纵向排列（V：垂直排列，H水平排列）模糊查询
		if(entity.getTransformData()!=null&&StringUtils.isNotBlank(String.valueOf(entity.getTransformData()))){
			wrapper.like("transform_data", String.valueOf(entity.getTransformData()));
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
	 * @todo   根据dataType、dataSpace查询内容
	 */
	@Override
	public List<CommonInterfaceExc> getInterfaceByDataTypeAndDataSpace(String dataType, String dataSpace) {
		CommonInterfaceExc entity=new CommonInterfaceExc();
		entity.setDataType(dataType);
		if(StringUtils.isNotBlank(dataSpace)){
			entity.setDataSpace(dataSpace);
		}
		entity.setDelFlag(UN_DEL_FLAG);
		EntityWrapper<CommonInterfaceExc> wrapper = searchWrapper(null,null, entity);
		List<CommonInterfaceExc> interfaces=commonInterfaceExcMapper.selectList(wrapper);
		return interfaces;
	}
	/**
     * 
     * @time   2018年9月27日 下午2:38:39
     * @author zuoqb
     * @todo   执行SQL返回执行垂直格式数据结果[{},{},{}]  
     */
    public  PublicResult<?> execeSqlVertical(String sql, String dataSourceId, List<String> matcher,
			PublicResult<Map<String, String>> dealParamsResult,HttpServletRequest request,String methodName,String tranType) {
	    	AuthPower authPower =null;
		try {
			Method[] methods=CommonInterfaceController.class.getMethods();
			for(Method method:methods){
				if(method.getName().equals(methodName)){
					authPower = method.getAnnotation(AuthPower.class);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new PublicResult<>(PublicResultConstant.ERROR,e.getMessage(), null);
		}
		if(StringUtils.isNotBlank(dataSourceId)){
    		DbDatasourceConfig config =iDbDatasourceConfigService.getById(dataSourceId);
    		if(config==null){
    			return new PublicResult<>(PublicResultConstant.PARAM_ERROR,"数据源无效，请配置数据源！", null);
    		}
    		//查询平台信息 校验平台合法性
        	SysPlatInfo sysPlatInfo=iSysPlatInfoService.getById(config.getSysPlatId());
        	if(sysPlatInfo==null){
    			return new PublicResult<>(PublicResultConstant.PARAM_ERROR,"平台无效，请联系管理员！", null);
    		}
        	/**
        	 * 验证平台秘钥 需要以后扩展
        	 */
        	if(authPower!=null&&!authPower.avoidSign()){
				//不免签名认证
        		String signAuth = request.getHeader(AppInterceptors.DEFAULT_AUTH_NAME);//签名
        		if(StringUtils.isNotBlank(signAuth)){
        			if(!signAuth.equals(sysPlatInfo.getSecretKey())){
        				return new PublicResult<>(PublicResultConstant.PARAM_ERROR,"平台签名"+signAuth+"无效，请联系管理员！", null);
        			}
        		}
			}
    		Connection conn = JdbcUtil.getConn(config);
    		if(conn==null){
    			return new PublicResult<>(PublicResultConstant.FAILED,"数据库连接失败！",null);
    		}
    		PreparedStatement pstmt = null;
    		ResultSet rs = null;
    		try {
    			//将where后面的‘与’替换
    			Pattern p=Pattern.compile("where",Pattern.CASE_INSENSITIVE);
    			Matcher m=p.matcher(sql);
    			while(m.find()){
    	            String whereStr=m.group();
    	            String[] splits=sql.split(whereStr);
    	            sql=splits[0];
    	            for(int x=0;x<splits.length;x++){
    	            	if(x>0){
    	            		sql+=whereStr+splits[x].replaceAll("‘", "'").replaceAll("’", "'");
    	            	}
    	            }
    	        }
    			pstmt = conn.prepareStatement(sql);
    			//设置参数 外层循环
    			for(int index=0;index<matcher.size();index++){
    				String matcherKey=matcher.get(index);
    				String variableValue = JdbcUtil.getParamValueByKey(dealParamsResult, matcherKey);
    				if(matcherKey.trim().startsWith("#")){
    					//#替换sql语句中的参数，只能是字符串
    					pstmt.setString(index+1, variableValue);
    				}
    				if(matcherKey.trim().startsWith("$")){
    					//$替换sql语句中的内容，只能是数字
    					pstmt.setInt(index+1, Integer.valueOf(variableValue));
    				}
    			}
    			rs = pstmt.executeQuery();
    			List<Map<String, Object>> verticalData = JdbcUtil.parseResultSet2List(rs);
    			//转成横向数据格式
    	    	Map<String,List<Object>> horizontalData=new HashMap<String, List<Object>>();
    	    	if(VERTICAL_DATA_FORMAT.equals(tranType)){
    	    		return JdbcUtil.verticalToHorizontal(verticalData, horizontalData);
    	    	}else{
    	    		return new PublicResult<>(PublicResultConstant.SUCCESS, verticalData);
    	    	}
    		} catch (Exception e) {
    			e.printStackTrace();
    			return new PublicResult<>(PublicResultConstant.FAILED,e.getMessage(),null);
    		}finally {
    			try {
    				JdbcUtil.close(conn, pstmt, rs);
    			} catch (Exception e) {
    				e.printStackTrace();
    				return new PublicResult<>(PublicResultConstant.FAILED,e.getMessage(),null);
    			}
    		}
    	}else{
    		try {
    			//数据源为空 默认当前数据库
    			/**
            	 * 验证平台秘钥 需要以后扩展
            	 */
    			if(authPower!=null&&!authPower.avoidSign()){
    				//不免签名认证
    				String signAuth = request.getHeader(AppInterceptors.DEFAULT_AUTH_NAME);//签名
    				if(StringUtils.isNotBlank(signAuth)){
    					if(!signAuth.equals(Default_X_SIGN)){
    						return new PublicResult<>(PublicResultConstant.PARAM_ERROR,"平台签名"+signAuth+"无效，请联系管理员！", null);
    					}
    				}
    			}
        		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
        		if(dealParamsResult.getData()!=null&&dealParamsResult.getData().size()>0){
        			Object[] values=new Object[matcher.size()];//这边必须为Object[]
        			//设置参数 外层循环
        			for(int index=0;index<matcher.size();index++){
        				String matcherKey=matcher.get(index);
        				if(matcherKey.trim().startsWith("#")){
        					//#替换sql语句中的参数，只能是字符串
        					values[index]=JdbcUtil.getParamValueByKey(dealParamsResult, matcherKey);
        				}
        				if(matcherKey.trim().startsWith("$")){
        					//$替换sql语句中的内容，只能是数字
        					values[index]=Integer.valueOf(JdbcUtil.getParamValueByKey(dealParamsResult, matcherKey));
        				}
        			}
        			list=jdbcTemplate.queryForList(sql,values);
        		}else{
        			list=jdbcTemplate.queryForList(sql);
        		}
        		//转成横向数据格式
    	    	Map<String,List<Object>> horizontalData=new HashMap<String, List<Object>>();
    	    	if(VERTICAL_DATA_FORMAT.equals(tranType)){
    	    		return JdbcUtil.verticalToHorizontal(list, horizontalData);
    	    	}else{
    	    		return new PublicResult<>(PublicResultConstant.SUCCESS, list);
    	    	}
			} catch (Exception e) {
				e.printStackTrace();
				return new PublicResult<>(PublicResultConstant.FAILED,e.getMessage(),null);
			}
    	}
	}
    
   
}
