package com.hailian.entity;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.hailian.base.BaseModel;

/**
 * 统一接口
 * @author zuoqb123
 * @date 2018-10-14
 */
@TableName("common_interface_exc")
@Data
@Accessors(chain = true)
public class CommonInterfaceExc extends BaseModel<CommonInterfaceExc> {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(name = "", value = "id", dataType = "String")
	private String id;
	/**
	 * 归属数据源 默认当前数据库
	 */
	@ApiModelProperty(name = "归属数据源 默认当前数据库", value = "dbDatasourceId", dataType = "String")
	@TableField("db_datasource_id")
	private String dbDatasourceId;
	/**
	 * 查询指标标识
	 */
	@ApiModelProperty(name = "查询指标标识", value = "dataType", dataType = "String")
	@TableField("data_type")
	private String dataType;
	/**
	 * 命名空间
	 */
	@ApiModelProperty(name = "命名空间", value = "dataSpace", dataType = "String")
	@TableField("data_space")
	private String dataSpace;
	/**
	 * 需要执行的sql语句，需要传参的位置使用#{参数名}，动态日期类型date_dt_kpi固定参数名称。
	 */
	@ApiModelProperty(name = "需要执行的sql语句，需要传参的位置使用#{参数名}，动态日期类型date_dt_kpi固定参数名称。", value = "dataSql", dataType = "String")
	@TableField("data_sql")
	private String dataSql;
	/**
	 * 横竖数据格式转换，默认纵向排列（V：垂直排列，H水平排列）
	 */
	@ApiModelProperty(name = "横竖数据格式转换，默认纵向排列（V：垂直排列，H水平排列）", value = "transformData", dataType = "String")
	@TableField("transform_data")
	private String transformData;

	@Override
	protected Serializable pkVal() {
		// TODO Auto-generated method stub
		return this.id;
	}
}
