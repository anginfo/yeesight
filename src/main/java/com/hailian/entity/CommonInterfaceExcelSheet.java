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
 * Excel导出sheet也配置表
 * @author zuoqb123
 * @date 2018-10-14
 */
@TableName("common_interface_excel_sheet")
@Data
@Accessors(chain = true)
public class CommonInterfaceExcelSheet extends BaseModel<CommonInterfaceExcelSheet> {

	private static final long serialVersionUID = 1L;

	/**
	 * 编码
	 */
	@ApiModelProperty(name = "编码", value = "id", dataType = "String")
	private String id;
	/**
	 * 标签名称
	 */
	@ApiModelProperty(name = "标签名称", value = "name", dataType = "String")
	private String name;
	/**
	 * 对应表ID
	 */
	@ApiModelProperty(name = "对应表ID", value = "tableId", dataType = "String")
	@TableField("table_id")
	private String tableId;
	/**
	 * 命名空间
	 */
	@ApiModelProperty(name = "命名空间", value = "dataSpace", dataType = "String")
	@TableField("data_space")
	private String dataSpace;
	/**
	 * 排序
	 */
	@ApiModelProperty(name = "排序", value = "orderNo", dataType = "Integer")
	@TableField("order_no")
	private Integer orderNo;

	@TableField(exist = false)
	private List<CommonInterfaceExcelSheetContent> sheetContens;

	@Override
	protected Serializable pkVal() {
		// TODO Auto-generated method stub
		return this.id;
	}
}
