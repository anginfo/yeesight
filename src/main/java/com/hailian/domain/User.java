package com.hailian.domain;

import java.io.Serializable;

/**
 * @Package: com.hailian.domain.User.java
 * @Description: InnoDB free: 4096 kB
 * @Copyright: Copyright (c) 2017
 * All right reserved.
 * Author jon lv
 * @date 2017/05/12 15:24
 * version v1.0.0
 */
public class User implements Serializable{
    private String id;

    private String head;

    private String name;

    private String phone;

    private String picture;

    private Integer sex;

    private String password;
    private String userType;//用户类型 1为超级管理员
    public User() {
		super();
	}
    public User(String id, String head, String name, String phone, String picture, Integer sex, String password) {
		super();
		this.id = id;
		this.head = head;
		this.name = name;
		this.phone = phone;
		this.picture = picture;
		this.sex = sex;
		this.password = password;
	}

	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	

    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	/**
     * @return head
     */
    public String getHead() {
        return head;
    }

    /**
     * @param head
     */
    public void setHead(String head) {
        this.head = head == null ? null : head.trim();
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * @return picture
     */
    public String getPicture() {
        return picture;
    }

    /**
     * @param picture
     */
    public void setPicture(String picture) {
        this.picture = picture == null ? null : picture.trim();
    }

    /**
     * @return sex
     */
    public Integer getSex() {
        return sex;
    }

    /**
     * @param sex
     */
    public void setSex(Integer sex) {
        this.sex = sex;
    }

    /**
     * @Title toString
     * @Author jon lv
     * @Date 2017/05/12 15:24
     * @return java.lang.String
     * @throws []
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", head=").append(head);
        sb.append(", name=").append(name);
        sb.append(", phone=").append(phone);
        sb.append(", picture=").append(picture);
        sb.append(", sex=").append(sex);
        sb.append("]");
        return sb.toString();
    }
}