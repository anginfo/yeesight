package com.hailian.entity;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.hailian.base.BaseModel;

/**
 * 平台信息
 * @author zuoqb123
 * @date 2018-10-14
 */
@TableName("sys_plat_info")
@Data
@Accessors(chain = true)
public class SysPlatInfo extends BaseModel<SysPlatInfo> {

	private static final long serialVersionUID = 1L;

	/**
	 * 平台编号
	 */
	@ApiModelProperty(name = "平台编号", value = "id", dataType = "String")
	private String id;
	/**
	 * 平台名称
	 */
	@ApiModelProperty(name = "平台名称", value = "name", dataType = "String")
	private String name;
	/**
	 * 平台英文名称
	 */
	@ApiModelProperty(name = "平台英文名称", value = "enname", dataType = "String")
	private String enname;
	/**
	 * 接口版本
	 */
	@ApiModelProperty(name = "接口版本", value = "versions", dataType = "String")
	private String versions;
	/**
	 * 平台秘钥 X-Sign
	 */
	@ApiModelProperty(name = "平台秘钥 X-Sign", value = "secretKey", dataType = "String")
	@TableField("secret_key")
	private String secretKey;
	/**
	 * 平台联系人
	 */
	@ApiModelProperty(name = "平台联系人", value = "contacts", dataType = "String")
	private String contacts;
	/**
	 * 联系人电话
	 */
	@ApiModelProperty(name = "联系人电话", value = "contactsTel", dataType = "String")
	@TableField("contacts_tel")
	private String contactsTel;
	/**
	 * 联系人邮箱
	 */
	@ApiModelProperty(name = "联系人邮箱", value = "contactsMail", dataType = "String")
	@TableField("contacts_mail")
	private String contactsMail;

	@Override
	protected Serializable pkVal() {
		// TODO Auto-generated method stub
		return this.id;
	}
}
