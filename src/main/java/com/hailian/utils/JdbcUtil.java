package com.hailian.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.druid.util.JdbcConstants;
import com.hailian.common.PublicResult;
import com.hailian.conf.Constant;
import com.hailian.entity.DbDatasourceConfig;
import com.hailian.enums.PublicResultConstant;

/**
 * @time   2018年9月13日 上午10:38:25
 * @author zuoqb
 * @todo   原始JDBC连接工具
 */

public class JdbcUtil implements Constant{
	private static Connection conn = null;
	/*static String HIVE_JDBC_DRIVER = "org.apache.hive.jdbc.HiveDriver";
	static String MYSQL_JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static String ORACLE_JDBC_DRIVER = "oracle.jdbc.OracleDriver";
	static String IMPALA_JDBC_DRIVER = "com.cloudera.impala.jdbc41.Driver";
	static String POSTGRESQL_JDBC_DRIVER="org.postgresql.Driver";*/
	/**
	 * 统一接口jdbc连接配置
	 */
	public static Connection getConn(DbDatasourceConfig config) {
		if(config==null){
			return null;
		}
		if(StringUtils.isEmpty(config.getDbUrl())||StringUtils.isEmpty(config.getDbName())
				||StringUtils.isEmpty(config.getDbDiver())||StringUtils.isEmpty(config.getDbType())){
			return null;
		}
		try {
			String dbType = config.getDbType().toLowerCase();
			String connectionUrl = config.getDbUrl();
			switch (dbType) {
			case JdbcConstants.MYSQL:
				connectionUrl=connectionUrl.split("\\?")[0];
				connectionUrl+="?useUnicode=true&characterEncoding=utf8&useSSL=false";
				break;
			/*case "impala":
				connectionUrl+=";auth=noSasl";
				break;*/
			default:
				break;
			}
			System.out.println(connectionUrl);
			Connection conn = null;
			Class.forName(config.getDbDiver());
			conn = DriverManager.getConnection(connectionUrl, config.getDbName(), config.getDbPassword());
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	
	/**
	 * 
	 * @time   2018年9月13日 上午11:09:37
	 * @author zuoqb
	 * @todo   关闭
	 */
	public static void close(Connection conn,Statement stat,ResultSet rs){
		 try {
			 if (rs!=null) {
				rs.close();
			 }
			 if (stat!=null) {
				stat.close();
			 }
			 if (conn!=null&&!conn.isClosed()) {
				 //保证从池当中拿出的连接都是自动提交的
				//conn.setAutoCommit(true);
				conn.close();
			 }
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	 /**
     * 
     * @time   2018年9月27日 上午12:17:18
     * @author zuoqb
     * @todo   将结果集转成List<Map<String,Object>>
     */
	public static List<Map<String, Object>> parseResultSet2List(ResultSet rs) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		String[] colNames = new String[columnCount];//所有列名
		for(int bo = 0; bo < colNames.length; ++bo) {
		    colNames[bo] = rsmd.getColumnLabel(bo + 1);
		    if(colNames[bo] != null) {
		        colNames[bo] = colNames[bo];
		    }
		}
		while(rs.next()) {
		    Map<String, Object> mapOfColValues = new HashMap<String, Object>();
		    for(int i = 1; i <= columnCount; ++i) {
		    	mapOfColValues.put(colNames[i - 1], rs.getObject(i)+"");
		    }
		    list.add(mapOfColValues);
		}
		return list;
	}
	
	
	/**
     * 
     * @time   2018年9月27日 下午5:28:55
     * @author zuoqb
     * @todo   纵向数据转横向
     * @return_type   PublicResult<?>
     */
	public static PublicResult<?> verticalToHorizontal(List<Map<String, Object>> verticalData,
			Map<String, List<Object>> horizontalData) {
		if(verticalData.size()>0){
			Iterator<String> iter = verticalData.get(0).keySet().iterator();
			while(iter.hasNext()){
				String key=iter.next();
				horizontalData.put(key, new ArrayList<Object>());
			}
		}
		for(Map<String, Object> map:verticalData){
			Iterator<String> iter = map.keySet().iterator();
			while(iter.hasNext()){
				String key=iter.next();
				Object value = map.get(key);
				horizontalData.get(key).add(value+"");
			}
		}
		return new PublicResult<>(PublicResultConstant.SUCCESS, horizontalData);
	}
    
    /**
     * @time   2018年9月27日 下午1:10:34
     * @author zuoqb
     * @todo   校验SQL个数
     */
	public static PublicResult<Map<String, List<Map<String, Object>>>> formatSql(String sql,
			List<String> matcher, PublicResult<Map<String, String>> dealParamsResult) {
		if(StringUtils.isBlank(sql)){
    		return new PublicResult<>(PublicResultConstant.PARAM_ERROR,"SQL语句为空！", null);
    	}
    	if(!sql.trim().toUpperCase().startsWith("SELECT")&&
    			!sql.trim().toUpperCase().startsWith("WITH")){
			return new PublicResult<>(PublicResultConstant.PARAM_ERROR,"SQL语句错误，只能执行查询语句！", null);
		}
		if(!PublicResultConstant.SUCCESS.msg.equals(dealParamsResult.getMsg())){
			return new PublicResult<>(PublicResultConstant.PARAM_ERROR,"params格式必须为key1::value1;;key2::value2格式!", null);
		}
		//验证SQL需要参数必须都传递 扩展
    	if(!canMatchSqlParams(matcher, dealParamsResult)){
    		return new PublicResult<>(PublicResultConstant.PARAM_ERROR,"params传递的参数与SQL中需要的参数不匹配！", null);
    	}
    	
    	return new PublicResult<>(PublicResultConstant.SUCCESS,null);
	}
	
	
	public static PublicResult<Map<String, List<Map<String, Object>>>> formatInsertSql(String sql,
			List<String> matcher, PublicResult<Map<String, String>> dealParamsResult) {
		if(StringUtils.isBlank(sql)){
    		return new PublicResult<>(PublicResultConstant.PARAM_ERROR,"SQL语句为空！", null);
    	}
		if(!PublicResultConstant.SUCCESS.msg.equals(dealParamsResult.getMsg())){
			return new PublicResult<>(PublicResultConstant.PARAM_ERROR,"params格式必须为key1::value1;;key2::value2格式!", null);
		}
		//验证SQL需要参数必须都传递 扩展
    	if(!canMatchSqlParams(matcher, dealParamsResult)){
    		return new PublicResult<>(PublicResultConstant.PARAM_ERROR,"params传递的参数与SQL中需要的参数不匹配！", null);
    	}
    	
    	return new PublicResult<>(PublicResultConstant.SUCCESS,null);
	}
    /**
     * @time   2018年9月27日 下午1:02:26
     * @author zuoqb
     * @todo   根据key获取参数值
     * @return_type   String
     */
	public static String getParamValueByKey(PublicResult<Map<String, String>> dealParamsResult, String matcherKey) {
		//获取变量名称
		String variableName=replaceSymbol(matcherKey);
		//获取变量值
		String variableValue=dealParamsResult.getData().get(variableName);
		return variableValue;
	}
    /**
     * @time   2018年9月27日 下午1:00:30
     * @author zuoqb
     * @todo   处理SQL中解析的变量 将特殊符号去除
     */
	public static String replaceSymbol(String matcherKey) {
		return matcherKey.replaceAll("#", "").replaceAll("\\$", "").replaceAll("\\{", "").replaceAll("\\}", "");
	}
    /**
     * 
     * @time   2018年9月27日 下午12:10:45
     * @author zuoqb
     * @todo   校验传递的参数与SQL中需要的参数是否匹配
     */
	public static boolean canMatchSqlParams(List<String> matcher, PublicResult<Map<String, String>> dealParamsResult) {
		/*if(matcher.size()>dealParamsResult.getData().size()){
			//传递的参数个数小于SQL中需要的参数数量
			return false;
		}*/
		//判断SQL中需要的参数必须都传递
		for(String sqlParam:matcher){
			String variableName=replaceSymbol(sqlParam);
			if(!dealParamsResult.getData().containsKey(variableName)){
				return false;
			}
		}
		return true;
	}
    
    
    /**
     * @time   2018年9月27日 上午11:34:00
     * @author zuoqb
     * @todo   处理请求参数params 封装为Map格式
     */
	public static PublicResult<Map<String,String>> dealParams(String params) {
    	Map<String,String> map=new HashMap<String, String>();
    	if(StringUtils.isBlank(params)){
    		return new PublicResult<>(PublicResultConstant.SUCCESS, map);
    	}
		String[] keyValues=params.split(";;");
		boolean isLegal=true;
		for(String para:keyValues){
			if(para.indexOf("::")!=-1){
				String[] split=para.split("::");
				if(split.length>1){
					map.put(split[0], split[1]);
				}else{
					if(para.endsWith("::")){
						map.put(split[0], "");
					}else{
						isLegal=false;
						break;
					}
				}
			}else{
				isLegal=false;
				break;
			}
		}
		if(isLegal){
			return new PublicResult<>(PublicResultConstant.SUCCESS, map);
		}else{
			return new PublicResult<>(PublicResultConstant.PARAM_ERROR, null);
		}
	}
	
    

}
