package com.hailian.entity;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.hailian.base.BaseModel;

/**
 * excel导出模板配置表
 * @author zuoqb123
 * @date 2018-10-14
 */
@TableName("common_interface_excel_table")
@Data
@Accessors(chain = true)
public class CommonInterfaceExcelTable extends BaseModel<CommonInterfaceExcelTable> {

	private static final long serialVersionUID = 1L;

	/**
	 * 唯一标识
	 */
	@ApiModelProperty(name = "唯一标识", value = "id", dataType = "String")
	private String id;
	/**
	 * 表名称
	 */
	@ApiModelProperty(name = "表名称", value = "name", dataType = "String")
	private String name;
	/**
	 * excel模板系统路径
	 */
	@ApiModelProperty(name = "excel模板系统路径", value = "templetPath", dataType = "String")
	@TableField("templet_path")
	private String templetPath;
	/**
	 * 查询指标标识
	 */
	@ApiModelProperty(name = "查询指标标识", value = "dataType", dataType = "String")
	@TableField("data_type")
	private String dataType;
	@TableField(exist = false)
	private List<CommonInterfaceExcelSheet> sheets;

	@Override
	protected Serializable pkVal() {
		// TODO Auto-generated method stub
		return this.id;
	}
}
