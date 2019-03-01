package com.hailian.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * 文件编码表
 * 
 * @author zuoqb123
 * @date 2018-11-12
 */
@TableName("doc_file_code")
public class DocFileCode extends Model<DocFileCode> {

	private static final long serialVersionUID = 1L;

	/**
	 * 编码
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
	private String sub1;
	private String sub2;
	private String sub3;
	private Integer nums;
	private String code;
	private String type;//0-目录 1-文件
	/**
	 * 备注信息
	 */
	private String remarks;
	/**
	 * 创建者
	 */
	@TableField("create_by")
	private String createBy;
	/**
	 * 创建时间
	 */
	@TableField("create_date")
	private Date createDate;
	/**
	 * 更新者
	 */
	@TableField("update_by")
	private String updateBy;
	/**
	 * 更新时间
	 */
	@TableField("update_date")
	private Date updateDate;
	/**
	 * 删除标记（0：正常；1：删除）
	 */
	@TableField("del_flag")
	private String delFlag;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSub1() {
		return sub1;
	}

	public void setSub1(String sub1) {
		this.sub1 = sub1;
	}

	public String getSub2() {
		return sub2;
	}

	public void setSub2(String sub2) {
		this.sub2 = sub2;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSub3() {
		return sub3;
	}

	public void setSub3(String sub3) {
		this.sub3 = sub3;
	}

	public Integer getNums() {
		return nums;
	}

	public void setNums(Integer nums) {
		this.nums = nums;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "DocFileCode{" + "id=" + id + ", sub1=" + sub1 + ", sub2="
				+ sub2 + ", sub3=" + sub3 + ", nums=" + nums + ", code=" + code
				+ ", remarks=" + remarks + ", createBy=" + createBy
				+ ", createDate=" + createDate + ", updateBy=" + updateBy
				+ ", updateDate=" + updateDate + ", delFlag=" + delFlag + "}";
	}
}
