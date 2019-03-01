package com.hailian.entity;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.hailian.base.BaseModel;

/**
 * 操作日志
 * @author zuoqb123
 * @date 2018-10-14
 */
@TableName("sys_operation_log")
@Data
@Accessors(chain = true)
public class SysOperationLog extends BaseModel<SysOperationLog> {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(name = "", value = "id", dataType = "String")
	private String id;
	/**
	 * 日志类型
	 */
	@ApiModelProperty(name = "日志类型", value = "logDescription", dataType = "String")
	@TableField("log_description")
	private String logDescription;
	/**
	 * 日志名称
	 */
	@ApiModelProperty(name = "日志名称", value = "actionArgs", dataType = "String")
	@TableField("action_args")
	private String actionArgs;
	/**
	 * 用户id
	 */
	@ApiModelProperty(name = "用户id", value = "userName", dataType = "String")
	@TableField("user_name")
	private String userName;
	/**
	 * 类名称
	 */
	@ApiModelProperty(name = "类名称", value = "className", dataType = "String")
	@TableField("class_name")
	private String className;
	/**
	 * 方法名称
	 */
	@ApiModelProperty(name = "方法名称", value = "method", dataType = "String")
	private String method;
	@ApiModelProperty(name = "", value = "ip", dataType = "String")
	private String ip;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(name = "创建时间", value = "createTime", dataType = "Long")
	@TableField("create_time")
	private Long createTime;
	/**
	 * 是否成功
	 */
	@ApiModelProperty(name = "是否成功", value = "succeed", dataType = "String")
	private String succeed;
	/**
	 * 备注
	 */
	@ApiModelProperty(name = "备注", value = "message", dataType = "String")
	private String message;

	@Override
	protected Serializable pkVal() {
		// TODO Auto-generated method stub
		return this.id;
	}
}
