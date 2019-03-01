package com.hailian.entity;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.hailian.base.BaseModel;

/**
 * 数据源配置
 * @author zuoqb123
 * @date 2018-10-14
 */
@TableName("db_datasource_config")
@Data
@Accessors(chain = true)
public class DbDatasourceConfig extends BaseModel<DbDatasourceConfig> {

	private static final long serialVersionUID = 1L;

	/**
	 * 编号
	 */
	@ApiModelProperty(name = "编号", value = "id", dataType = "String")
	private String id;
	/**
	 * 归属平台
	 */
	@ApiModelProperty(name = "归属平台", value = "sysPlatId", dataType = "String")
	@TableField("sys_plat_id")
	private String sysPlatId;
	/**
	 * 数据源名称
	 */
	@ApiModelProperty(name = "数据源名称", value = "name", dataType = "String")
	private String name;
	/**
	 * 数据源英文名称
	 */
	@ApiModelProperty(name = "数据源英文名称", value = "enname", dataType = "String")
	private String enname;
	/**
	 * 数据源类型
	 */
	@ApiModelProperty(name = "数据源类型", value = "dbType", dataType = "String")
	@TableField("db_type")
	private String dbType;
	/**
	 * 数据源驱动
	 */
	@ApiModelProperty(name = "数据源驱动", value = "dbDiver", dataType = "String")
	@TableField("db_diver")
	private String dbDiver;
	/**
	 * 数据连接地址
	 */
	@ApiModelProperty(name = "数据连接地址", value = "dbUrl", dataType = "String")
	@TableField("db_url")
	private String dbUrl;
	/**
	 * 用户名
	 */
	@ApiModelProperty(name = "用户名", value = "dbName", dataType = "String")
	@TableField("db_name")
	private String dbName;
	/**
	 * 连接密码
	 */
	@ApiModelProperty(name = "连接密码", value = "dbPassword", dataType = "String")
	@TableField("db_password")
	private String dbPassword;
	/**
	 * 数据库版本
	 */
	@ApiModelProperty(name = "数据库版本", value = "dbVersion", dataType = "String")
	@TableField("db_version")
	private String dbVersion;
	/**
	 * 最大连接数
	 */
	@ApiModelProperty(name = "最大连接数", value = "maxNum", dataType = "Integer")
	@TableField("max_num")
	private Integer maxNum;

	@Override
	protected Serializable pkVal() {
		// TODO Auto-generated method stub
		return this.id;
	}

}
