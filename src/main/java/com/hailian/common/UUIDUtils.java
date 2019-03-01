package com.hailian.common;

import java.util.UUID;

/**
 * Package: com.hailian.common.UUIDUtils
 * Description: 描述
 * Copyright: Copyright (c) 2017
 *
 * @author lv bin
 * Date: 2018/1/25 9:12
 * Version: V1.0.0
 */
public class UUIDUtils {

    /**
     * jdk uuid 工具类
     * @return
     */
    public static String getUuid(){
        return UUID.randomUUID().toString().replace("-", "");
    }
    public static String get4UUID() {
		UUID id = UUID.randomUUID();
		String[] idd = id.toString().split("-");
		return idd[1];
	}

	/**
	 * 获得8个长度的十六进制的UUID
	 * 
	 * @return UUID
	 */
	public static String get8UUID() {
		UUID id = UUID.randomUUID();
		String[] idd = id.toString().split("-");
		return idd[0];
	}

	/**
	 * 获得12个长度的十六进制的UUID
	 * 
	 * @return UUID
	 */
	public static String get12UUID() {
		UUID id = UUID.randomUUID();
		String[] idd = id.toString().split("-");
		return idd[0] + idd[1];
	}
}
