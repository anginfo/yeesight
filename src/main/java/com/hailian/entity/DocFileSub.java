package com.hailian.entity;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * 文件编码表
 * @author zuoqb123
 * @date 2018-11-12
 */
@TableName("doc_file_sub")
public class DocFileSub extends Model<DocFileSub> {

    private static final long serialVersionUID = 1L;

    /**
     * 编码
     */
   private String id;
    /**
     * 0-目录 1-文件
     */
   private String name;
   private String code;
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

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
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
      return "DocFileSub{" +
         "id=" + id +
         ", name=" + name +
         ", code=" + code +
         ", remarks=" + remarks +
         ", createBy=" + createBy +
         ", createDate=" + createDate +
         ", updateBy=" + updateBy +
         ", updateDate=" + updateDate +
         ", delFlag=" + delFlag +
         "}";
   }
}
