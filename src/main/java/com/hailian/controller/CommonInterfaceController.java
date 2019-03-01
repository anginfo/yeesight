package com.hailian.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hailian.annotation.AuthPower;
import com.hailian.annotation.Log;
import com.hailian.base.BaseController;
import com.hailian.common.PublicResult;
import com.hailian.entity.CommonInterfaceExc;
import com.hailian.entity.DbDatasourceConfig;
import com.hailian.entity.SysPlatInfo;
import com.hailian.enums.PublicResultConstant;
import com.hailian.service.ICommonInterfaceExcService;
import com.hailian.service.IDbDatasourceConfigService;
import com.hailian.service.ISysPlatInfoService;
import com.hailian.utils.JdbcUtil;
import com.hailian.utils.RexUtils;
/**
 *
 * @author zuoqb123
 * @date 2018-09-26
 * @todo 统一接口API
 */
@Controller
@RequestMapping("/api/v1/common/interface")
@Api(value = "统一接口API",description="统一接口API @author zuoqb")
public class CommonInterfaceController extends BaseController {

    @Autowired
    public ICommonInterfaceExcService iCommonInterfaceExcService;
    @Autowired
    public IDbDatasourceConfigService iDbDatasourceConfigService;
    @Autowired
    public ISysPlatInfoService iSysPlatInfoService;

	/**
	 * 
	 * @time   2018年9月13日 上午10:39:24
	 * @author zuoqb
	 * @todo   根据数据源与SQL查询数据 或者查询当前库中SQL结果
	 * isVertical-数据格式，true-垂直 false-横向数据
	 */
    @ResponseBody
	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
	@ApiOperation(value = "根据数据源与SQL查询数据", notes = "根据数据源与SQL查询数据", httpMethod = "GET")
	@RequestMapping(value = "/api/{version}/common/interface/getBySql", method = RequestMethod.GET)
    @ApiImplicitParams({
    	@ApiImplicitParam(name="sql",value="执行的SQL语句",dataType="String",paramType="query",required = true),
    	@ApiImplicitParam(name="dataSourceId",value="SQL要查询的数据库编码，默认为当前库",dataType="String",paramType="query",required = false),
    	@ApiImplicitParam(name="params",value="动态参数 格式 time::20180731;;cbkCode::BD1011001",dataType="String",paramType="query",required = false),
    	@ApiImplicitParam(name="isVertical",value="数据格式，true-垂直 false-横向数据",dataType="boolean",paramType="query",required = false,defaultValue="true")})
	public PublicResult<?> getBySql(@RequestParam(value="sql",required = true) String sql,@RequestParam(value="dataSourceId",required = false) String dataSourceId,
			@RequestParam(value="params",required = false) String params,@RequestParam(value="isVertical",required = false,defaultValue="true") boolean isVertical,
			HttpServletRequest request) {
		/**
    	 * 开始处理SQL
    	 */
    	//解析SQL中的${}与#{}
    	//匹配所有${}与匹配所有#{}正则表达式
    	String rexg="\\#\\{([^\\}]+)\\}|\\$\\{([^\\}]+)\\}";
    	//按照顺序查找出所有# $ 比如  [#{time}, #{cbkCode}, ${startIndex}, ${pageSize}]
    	List<String> matcher=RexUtils.getString(sql, rexg);
    	PublicResult<Map<String,String>> dealParamsResult=JdbcUtil.dealParams(params);
    	PublicResult<Map<String, List<Map<String, Object>>>> formatSqlResult= JdbcUtil.formatSql(sql, matcher, dealParamsResult);
    	if(!PublicResultConstant.SUCCESS.msg.equals(formatSqlResult.getMsg())){
    		return new PublicResult<>(PublicResultConstant.INVALID_PARAM_EMPTY,formatSqlResult.getErrorMsg(), null);
    	}
    	//将SQL中所有${}与所有#{}替换为？
    	sql=RexUtils.getReplace(sql, rexg,"?");
    	/**
    	 * 处理SQL结束
    	 */
    	if(!isVertical){
    		return iCommonInterfaceExcService.execeSqlVertical(sql, dataSourceId, matcher, dealParamsResult,request,"getBySql",VERTICAL_DATA_FORMAT);
    	}
    	return iCommonInterfaceExcService.execeSqlVertical(sql, dataSourceId, matcher, dealParamsResult,request,"getBySql",null);
	}

	/**
	 * @time   2018年9月27日 上午9:13:27
	 * @author zuoqb
	 * @todo   统一接口查询数据
	 * 平台-数据源-接口
	 * @param dataType-接口类型  params-动态参数 格式 time::20180731;;cbkCode::BD1011001,,startIndex::1;;pageSize::10
	 * SQL中不要带“;” 否则会报错
	 */
    @ResponseBody
   	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
   	@ApiOperation(value = "统一接口查询数据", notes = "统一接口查询数据", httpMethod = "GET")
   	@RequestMapping(value = "/getByDataType", method = RequestMethod.GET)
    @ApiImplicitParams({
    	@ApiImplicitParam(name="dataType",value="查询指标标识",dataType="String",paramType="query",required = true),
    	@ApiImplicitParam(name="params",value="动态参数 格式 time::20180731;;cbkCode::BD1011001",dataType="String",paramType="query",required = false)})
    @Log(description = "API接口:/api/{version}/common/interface/getByDataType")
    public PublicResult<?>  getByDataType(@RequestParam(value="dataType",required = true) String dataType,@RequestParam(value="params",required = false) String params
    		,HttpServletRequest request,HttpServletResponse response) {
    	if(StringUtils.isBlank(dataType)){
			return new PublicResult<>(PublicResultConstant.INVALID_PARAM_EMPTY,"dataType参数不能为空!", null);
		}
    	//根据dataType查询接口配置信息
    	EntityWrapper<CommonInterfaceExc> inteWrapper = new EntityWrapper<CommonInterfaceExc>();
    	inteWrapper.where("del_flag={0}", UN_DEL_FLAG);
    	inteWrapper.eq("data_type", dataType);
    	Map<String,Object> result=new HashMap<String,Object>();
    	List<CommonInterfaceExc> excList=iCommonInterfaceExcService.selectList(inteWrapper);
    	for(CommonInterfaceExc entity:excList){
    		/*CommonInterfaceExc entity=iCommonInterfaceExcService.selectOne(inteWrapper);
    		if(entity==null){
    			return new PublicResult<>(PublicResultConstant.PARAM_ERROR,dataType+"无效，请配置接口信息！", null);
    		}*/
    		/**
        	 * 开始处理SQL
        	 */
        	String sql=entity.getDataSql();
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
        	String dataSourceId=entity.getDbDatasourceId();
        	PublicResult<?> execeResult=iCommonInterfaceExcService.execeSqlVertical(sql, dataSourceId, matcher, dealParamsResult,request,"getByDataType",entity.getTransformData());
        	if(!PublicResultConstant.SUCCESS.msg.equals(execeResult.getMsg())){
        		return new PublicResult<>(PublicResultConstant.PARAM_ERROR,execeResult.getErrorMsg(), null);
        	}
        	if(execeResult.getData() instanceof List){
        		List<Map<String, Object>> verticalData=(List<Map<String, Object>>) execeResult.getData();
        		result.put(entity.getDataSpace(),verticalData);
        	}else{
        		Map<String,List<Object>> horizontalData= (Map<String, List<Object>>) execeResult.getData();
        		result.put(entity.getDataSpace(),horizontalData);
        	}
    	}
    	return new PublicResult<>(PublicResultConstant.SUCCESS, result);
   	}
    
    /**
	 * @time   2018年9月27日 上午9:13:27
	 * @author zuoqb
	 * @todo   统一接口插入数据
	 * 平台-数据源-接口
	 * @param dataType-接口类型  params-动态参数 格式 time::20180731;;cbkCode::BD1011001,,startIndex::1;;pageSize::10
	 * SQL中不要带“;” 否则会报错
	 */
    @ResponseBody
   	@AuthPower(avoidVersion = false, avoidPower = true, avoidSign = true, avoidLogin = true, avoidPlatform = true)
   	@ApiOperation(value = "统一接口查询数据", notes = "统一接口查询数据", httpMethod = "POST")
   	@RequestMapping(value = "/insertDate", method = RequestMethod.POST)
    @ApiImplicitParams({
    	@ApiImplicitParam(name="dataType",value="查询指标标识",dataType="String",paramType="query",required = true),
    	@ApiImplicitParam(name="params",value="动态参数 格式 time::20180731;;cbkCode::BD1011001",dataType="String",paramType="query",required = false)})
    @Log(description = "API接口:/api/{version}/common/interface/insertDate")
    public PublicResult<?>  insertDate(@RequestParam(value="dataType",required = true) String dataType,@RequestParam(value="params",required = false) String params
    		,HttpServletRequest request,HttpServletResponse response) {
    	if(StringUtils.isBlank(dataType)){
			return new PublicResult<>(PublicResultConstant.INVALID_PARAM_EMPTY,"dataType参数不能为空!", null);
		}
    	//根据dataType查询接口配置信息
    	EntityWrapper<CommonInterfaceExc> inteWrapper = new EntityWrapper<CommonInterfaceExc>();
    	inteWrapper.where("del_flag={0}", UN_DEL_FLAG);
    	inteWrapper.eq("data_type", dataType);
    	Map<String,Object> result=new HashMap<String,Object>();
    	List<CommonInterfaceExc> excList=iCommonInterfaceExcService.selectList(inteWrapper);
    	for(CommonInterfaceExc entity:excList){
    		/**
        	 * 开始处理SQL
        	 */
        	String sql=entity.getDataSql();
        	//解析SQL中的${}与#{}
        	//匹配所有${}与匹配所有#{}正则表达式
        	String rexg="\\#\\{([^\\}]+)\\}|\\$\\{([^\\}]+)\\}";
        	//按照顺序查找出所有# $ 比如  [#{time}, #{cbkCode}, ${startIndex}, ${pageSize}]
        	List<String> matcher=RexUtils.getString(sql, rexg);
        	PublicResult<Map<String,String>> dealParamsResult=JdbcUtil.dealParams(params);
        	PublicResult<Map<String, List<Map<String, Object>>>> formatSqlResult= JdbcUtil.formatInsertSql(sql, matcher, dealParamsResult);
        	if(!PublicResultConstant.SUCCESS.msg.equals(formatSqlResult.getMsg())){
        		return formatSqlResult;
        	}
        	//将SQL中所有${}与所有#{}替换为？
        	sql=RexUtils.getReplace(sql, rexg,"?");
        	/**
        	 * 处理SQL结束
        	 */
        	String dataSourceId=entity.getDbDatasourceId();
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
        			pstmt.execute();
        			return new PublicResult<>(PublicResultConstant.SUCCESS,null);
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
        	}
    	}
    	return new PublicResult<>(PublicResultConstant.SUCCESS, result);
   	}
    
}

