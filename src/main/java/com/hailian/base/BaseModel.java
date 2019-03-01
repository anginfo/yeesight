package com.hailian.base;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * 
 * @time   2018年9月26日 下午2:26:29
 * @author zuoqb
 * @todo   通用实体类，包含公共属性
 */
/*@SuppressWarnings({ "serial", "rawtypes" })
@Data
@Accessors(chain = true)*/
public abstract class BaseModel<T extends Model> extends Model<T> {
	/**
	* 创建者
	*/
	@ApiModelProperty(value="创建者",name="createBy",dataType="String")
	@TableField("create_by")
	private String createBy;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value="创建时间",name="createDate",dataType="Date")
	@TableField("create_date")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")//去掉时间后面的.0
	private Date createDate;
	/**
	 * 更新者
	 */
	@ApiModelProperty(value="更新者",name="updateBy",dataType="String")
	@TableField("update_by")
	private String updateBy;
	/**
	 * 更新时间
	 */
	@ApiModelProperty(value="更新时间",name="updateDate",dataType="Date")
	@TableField("update_date")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")//去掉时间后面的.0
	private Date updateDate;
	/**
	 * 备注信息
	 */
	@ApiModelProperty(value="备注",name="remarks",dataType="String")
	private String remarks;
	/**
	 * 删除标记
	 */
	@ApiModelProperty(value="删除标记",name="delFlag",dataType="String")
	@TableField("del_flag")
	private String delFlag;
	/**
	 * 排序字段 值必须对照表中字段
	 */
	@ApiModelProperty(value="排序字段 值必须对照表中字段",name="orderBy",dataType="String")
	@TableField(exist = false)
	private String orderBy;
	/**
	 * 排序方式  默认升序排列
	 */
	@ApiModelProperty(value="是否为升序",name="asc",dataType="boolean")
	@TableField(exist = false)
	private boolean asc=true   ;//是否为升序;
	@TableField(exist = false)
	/**
	 * 开始时间（用于检索创建时间）
	 */
	@ApiModelProperty(value="检索开始时间",name="startDate",dataType="Date")
	private Date startDate;
	@TableField(exist = false)
	/**
	 * 结束时间（用于检索创建时间）
	 */
	@ApiModelProperty(value="检索结束时间",name="endDate",dataType="Date")
	private Date endDate;
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
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public boolean isAsc() {
		return asc;
	}
	public void setAsc(boolean asc) {
		this.asc = asc;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
