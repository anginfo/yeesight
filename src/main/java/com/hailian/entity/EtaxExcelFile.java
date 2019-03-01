package com.hailian.entity;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.hailian.base.BaseModel;
/**
 * 保税处理之后的文件信息表表
 * @author zuoqb123
 * @date 2018-11-18
 */
@TableName("etax_excel_file")
public class EtaxExcelFile extends BaseModel<EtaxExcelFile> {

   private static final long serialVersionUID = 1L;

    /**
     * 编码
     */
   @ApiModelProperty(name="编码",value="id",dataType="String")
   private String id;
    /**
     * 版本 基本使用时间戳
     */
   @ApiModelProperty(name="版本 基本使用时间戳",value="version",dataType="String")
   private String version;
    /**
     * 原压缩文件类型
     */
   @ApiModelProperty(name="原压缩文件类型",value="orgCompressType",dataType="String")
   @TableField("org_compress_type")
   private String orgCompressType;
    /**
     * 原压缩文件名称
     */
   @ApiModelProperty(name="原压缩文件名称",value="orgCompressName",dataType="String")
   @TableField("org_compress_name")
   private String orgCompressName;
    /**
     * 原压缩文件保存路径
     */
   @ApiModelProperty(name="原压缩文件保存路径",value="orgCompressPath",dataType="String")
   @TableField("org_compress_path")
   private String orgCompressPath;
    /**
     * 解压后文件路径
     */
   @ApiModelProperty(name="解压后文件路径",value="uncompressPath",dataType="String")
   @TableField("uncompress_path")
   private String uncompressPath;
    /**
     * 处理完后文件保存路径
     */
   @ApiModelProperty(name="处理完后文件保存路径",value="targetPath",dataType="String")
   @TableField("target_path")
   private String targetPath;
    /**
     * 最终生成目标文件名称
     */
   @ApiModelProperty(name="最终生成目标文件名称",value="targetFileName",dataType="String")
   @TableField("target_file_name")
   private String targetFileName;
    /**
     * 文件上传编码 引用doc_upload_file  可以查看html信息
     */
   @ApiModelProperty(name="文件上传编码 引用doc_upload_file  可以查看html信息",value="fileId",dataType="String")
   @TableField("file_id")
   private String fileId;


   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getVersion() {
      return version;
   }

   public void setVersion(String version) {
      this.version = version;
   }

   public String getOrgCompressType() {
      return orgCompressType;
   }

   public void setOrgCompressType(String orgCompressType) {
      this.orgCompressType = orgCompressType;
   }

   public String getOrgCompressName() {
      return orgCompressName;
   }

   public void setOrgCompressName(String orgCompressName) {
      this.orgCompressName = orgCompressName;
   }

   public String getOrgCompressPath() {
      return orgCompressPath;
   }

   public void setOrgCompressPath(String orgCompressPath) {
      this.orgCompressPath = orgCompressPath;
   }

   public String getUncompressPath() {
      return uncompressPath;
   }

   public void setUncompressPath(String uncompressPath) {
      this.uncompressPath = uncompressPath;
   }

   public String getTargetPath() {
      return targetPath;
   }

   public void setTargetPath(String targetPath) {
      this.targetPath = targetPath;
   }

   public String getTargetFileName() {
      return targetFileName;
   }

   public void setTargetFileName(String targetFileName) {
      this.targetFileName = targetFileName;
   }

   public String getFileId() {
      return fileId;
   }

   public void setFileId(String fileId) {
      this.fileId = fileId;
   }

   @Override
   protected Serializable pkVal() {
      return this.id;
   }

   @Override
   public String toString() {
      return "EtaxExcelFile{" +
         ", id=" + id +
         ", version=" + version +
         ", orgCompressType=" + orgCompressType +
         ", orgCompressName=" + orgCompressName +
         ", orgCompressPath=" + orgCompressPath +
         ", uncompressPath=" + uncompressPath +
         ", targetPath=" + targetPath +
         ", targetFileName=" + targetFileName +
         ", fileId=" + fileId +
         "}";
   }
}
