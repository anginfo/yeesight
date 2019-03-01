package com.hailian.entity;

import lombok.Data;

@Data
public class MenuAutority {
	private static final long serialVersionUID = 1L;
	
	//`application_id`, `application_name`, `user_code`, `dim_type_name`, `dim_value`, `dim_value_code
	private String application_id;
	private String application_name;
	private String user_code;
	private String dim_type_name;
	private String dim_value_code;
	private String dim_value;
}
