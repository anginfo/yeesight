package com.hailian.entity;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.hailian.base.BaseModel;

import com.baomidou.mybatisplus.annotations.Version;

import lombok.Data;
import lombok.experimental.Accessors;
import io.swagger.annotations.ApiModelProperty;
/**
 * 用户表
 * @author zuoqb123
 * @date 2018-10-15
 */
@Data
@Accessors(chain = true)
@TableName("sys_user")
public class SysUser extends BaseModel<SysUser> {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
   @ApiModelProperty(name="编号",value="id",dataType="String")
   private String id;
    /**
     * 登录名
     */
   @ApiModelProperty(name="登录名",value="loginName",dataType="String")
   @TableField("login_name")
   private String loginName;
    /**
     * 密码
     */
   @ApiModelProperty(name="密码",value="password",dataType="String")
   private String password;
    /**
     * 工号
     */
   @ApiModelProperty(name="工号",value="no",dataType="String")
   private String no;
    /**
     * 姓名
     */
   @ApiModelProperty(name="姓名",value="name",dataType="String")
   private String name;
    /**
     * 邮箱
     */
   @ApiModelProperty(name="邮箱",value="email",dataType="String")
   private String email;
    /**
     * 电话
     */
   @ApiModelProperty(name="电话",value="phone",dataType="String")
   private String phone;
    /**
     * 手机
     */
   @ApiModelProperty(name="手机",value="mobile",dataType="String")
   private String mobile;
    /**
     * 用户类型
     */
   @ApiModelProperty(name="用户类型",value="userType",dataType="String")
   @TableField("user_type")
   private String userType;
    /**
     * 用户头像
     */
   @ApiModelProperty(name="用户头像",value="photo",dataType="String")
   private String photo;
    /**
     * 最后登陆IP
     */
   @ApiModelProperty(name="最后登陆IP",value="loginIp",dataType="String")
   @TableField("login_ip")
   private String loginIp;
    /**
     * 最后登陆时间
     */
   @ApiModelProperty(name="最后登陆时间",value="loginDate",dataType="Date")
   @TableField("login_date")
   private Date loginDate;
    /**
     * 是否可登录
     */
   @ApiModelProperty(name="是否可登录",value="loginFlag",dataType="String")
   @TableField("login_flag")
   private String loginFlag;
   @TableField(exist = false)
   private String accessToken;

   @Override
   protected Serializable pkVal() {
      return this.id;
   }

}
