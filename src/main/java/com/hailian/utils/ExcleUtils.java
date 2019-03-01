package com.hailian.utils;

import java.text.SimpleDateFormat;

public class ExcleUtils {

	
	 public static boolean isExcel2003(String filePath)
	    {
	        return filePath.matches("^.+\\.(?i)(xls)$");
	    }

	public static boolean isExcel2007(String filePath)
	    {
	        return filePath.matches("^.+\\.(?i)(xlsx)$");
	    }
	 
	   
	    
	  /**
	   * 验证EXCEL文件
	   * @param filePath
	   * @return
	   */
	  public static boolean validateExcel(String filePath){
	        if (filePath == null || !(isExcel2003(filePath) || isExcel2007(filePath))){  
	            return false;  
	        }  
	        return true;
	  }
}
