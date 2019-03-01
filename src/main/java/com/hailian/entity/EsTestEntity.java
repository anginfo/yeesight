package com.hailian.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableName;
import com.hailian.base.BaseModel;
@TableName("etax_excel_file")
public class EsTestEntity  extends BaseModel<EsTestEntity>  implements Serializable{
	 
	
	public static final String INDEX_NAME = "index_entity";
	
	public static final String TYPE = "test_zuo";
 
	private String id;
	private String name;
	private String pwd;
	
	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public EsTestEntity() {
		super();
	}
	
	public EsTestEntity(String id, String name) {
		this.id = id;
		this.name = name;
	}
 
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

	@Override
	protected Serializable pkVal() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}

