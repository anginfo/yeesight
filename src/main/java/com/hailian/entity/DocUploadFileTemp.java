package com.hailian.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 文件上传目录表
 * 
 * @author zuoqb123
 * @date 2018-11-10
 */
@TableName("doc_upload_file_temp")
public class DocUploadFileTemp extends Model<DocUploadFileTemp> {

	private static final long serialVersionUID = 1L;

	/**
	 * 编码
	 */
	private String id;
	private String code;
	/**
	 * 目录图标
	 */
	private String icon;
	private String url;
	/**
	 * 打开方式 _blank
	 */
	@TableField("open_type")
	private String openType;
	/**
	 * 父级编码
	 */
	@TableField("parent_id")
	private String parentId;
	/**
	 * 排序
	 */
	@TableField("order_no")
	private Integer orderNo;
	/**
	 * 目录名称
	 */
	@TableField("temp_name")
	private String tempName;
	/**
	 * 是否公开 0-公开 1-不公开
	 */
	@TableField("is_public")
	private String isPublic;
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
	@TableField(exist = false)
	private List<DocUploadFile> files = new ArrayList<DocUploadFile>();
	@TableField(exist = false)
	private String isAdmin;
	@TableField(exist = false)
	private List<DocUploadFileTemp> children = new ArrayList<DocUploadFileTemp>();

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getOpenType() {
		return openType;
	}

	public void setOpenType(String openType) {
		this.openType = openType;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public List<DocUploadFileTemp> getChildren() {
		return children;
	}

	public void setChildren(List<DocUploadFileTemp> children) {
		this.children = children;
	}

	public String getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(String isAdmin) {
		this.isAdmin = isAdmin;
	}

	public List<DocUploadFile> getFiles() {
		return files;
	}

	public void setFiles(List<DocUploadFile> files) {
		this.files = files;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getTempName() {
		return tempName;
	}

	public void setTempName(String tempName) {
		this.tempName = tempName;
	}

	public String getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(String isPublic) {
		this.isPublic = isPublic;
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
		return "DocUploadFileTemp{" + "id=" + id + ", parentId=" + parentId
				+ ", tempName=" + tempName + ", isPublic=" + isPublic
				+ ", remarks=" + remarks + ", createBy=" + createBy
				+ ", createDate=" + createDate + ", updateBy=" + updateBy
				+ ", updateDate=" + updateDate + ", delFlag=" + delFlag + "}";
	}
}
