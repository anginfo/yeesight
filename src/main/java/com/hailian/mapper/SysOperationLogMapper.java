package com.hailian.mapper;
import java.util.List;

import com.hailian.base.BaseMappers;
import com.hailian.entity.SysOperationLog;

/**
  * 操作日志Mapper接口
 * @author zuoqb123
 * @date 2018-09-28
 */
public interface SysOperationLogMapper extends BaseMappers<SysOperationLog> {
	List<SysOperationLog> dateList();

}


