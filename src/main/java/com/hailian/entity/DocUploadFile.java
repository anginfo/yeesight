package com.hailian.entity;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * 文件上传记录表
 * @author zuoqb123
 * @date 2018-10-24
 */
@TableName("doc_upload_file")
public class DocUploadFile extends Model<DocUploadFile> {

    private static final long serialVersionUID = 1L;

    /**
     * 编码
     */
   private String id;
   /**
    * 目录编码
    */
   @TableField("temp_id")
   private String tempId;
   /**
    * 编码
    */
   private String code;
    /**
     * 原名称
     */
   private String originalname;
    /**
     * 服务器文件路径
     */
   private String path;
    /**
     * 后缀
     */
   private String ext;
    /**
     * 上传后文件名称
     */
   @TableField("upload_name")
   private String uploadName;
    /**
     * 大小
     */
   private Integer size;
    /**
     * 转化的html名称
     */
   @TableField("html_name")
   private String htmlName;
    /**
     * 预览文件地址
     */
   @TableField("view_url")
   private String viewUrl;
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


   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getOriginalname() {
      return originalname;
   }

   public void setOriginalname(String originalname) {
      this.originalname = originalname;
   }

   public String getPath() {
      return path;
   }

   public void setPath(String path) {
      this.path = path;
   }

   public String getTempId() {
	return tempId;
}

public String getCode() {
	return code;
}

public void setCode(String code) {
	this.code = code;
}

public void setTempId(String tempId) {
	this.tempId = tempId;
}

public String getExt() {
      return ext;
   }

   public void setExt(String ext) {
      this.ext = ext;
   }

   public String getUploadName() {
      return uploadName;
   }

   public void setUploadName(String uploadName) {
      this.uploadName = uploadName;
   }

   public Integer getSize() {
      return size;
   }

   public void setSize(Integer size) {
      this.size = size;
   }

   public String getHtmlName() {
      return htmlName;
   }

   public void setHtmlName(String htmlName) {
      this.htmlName = htmlName;
   }

   public String getViewUrl() {
      return viewUrl;
   }

   public void setViewUrl(String viewUrl) {
      this.viewUrl = viewUrl;
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
      return "DocUploadFile{" +
         "id=" + id +
         ", originalname=" + originalname +
         ", path=" + path +
         ", ext=" + ext +
         ", uploadName=" + uploadName +
         ", size=" + size +
         ", htmlName=" + htmlName +
         ", viewUrl=" + viewUrl +
         ", remarks=" + remarks +
         ", createBy=" + createBy +
         ", createDate=" + createDate +
         ", updateBy=" + updateBy +
         ", updateDate=" + updateDate +
         ", delFlag=" + delFlag +
         "}";
   }
}
