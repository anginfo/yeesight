package com.lianxin.tax;

import java.io.File;

/**
 * 
 * @time   2018年11月16日 下午4:47:41
 * @author zuoqb
 * @todo   纳税文件
 */
public class TaxFilesModel {
	/**
	 * 公用属性
	 */
	private String fileType;//文件类型
	private boolean isPdf;
	private boolean isExcel2007;
	private String name;//文件名称
	private File file;//相应文件
	private Long size;//文件大小

	private boolean isApply;//是否为申请表 -主表
	private String startYear;//开始所属年
	private String startMonth;//开始所属月
	private String startDate;//开始日期
	private String companyName;//公司名称
	/**
	 * 数据字段
	 */
	private String xiaoshoueYear;//销售额年累
	private String xiaoshoueMonth;//销售额月累

	private String shuieYear;//税额年累
	private String shuieMonth;//税额月累

	private String disanxiangYear;//第三项年累
	private String disanxiangMonth;//第三项月累

	private String disixiangYear;//第四项年累
	private String disixiangMonth;//第四项月累

	private String jyzsYear;//简易征收年累
	private String jyzsMonth;//简易征收月累

	private String jysYear;//简易税年累
	private String jysMonth;//简易税月累

	private String jinxiangshuiYear;//进项税年累
	
	
	
	private String jzjtxseYear;// 即征即退销售额年累
	private String jzjtxseMonth;// 即征即退销售额月累
	private String jzjtseYear;// 即征即退税额年累
	private String jzjtseMonth;// 即征即退税额月累
	
	public String getJzjtxseYear() {
		return jzjtxseYear;
	}

	public void setJzjtxseYear(String jzjtxseYear) {
		this.jzjtxseYear = jzjtxseYear;
	}

	public String getJzjtxseMonth() {
		return jzjtxseMonth;
	}

	public void setJzjtxseMonth(String jzjtxseMonth) {
		this.jzjtxseMonth = jzjtxseMonth;
	}

	public String getJzjtseYear() {
		return jzjtseYear;
	}

	public void setJzjtseYear(String jzjtseYear) {
		this.jzjtseYear = jzjtseYear;
	}

	public String getJzjtseMonth() {
		return jzjtseMonth;
	}

	public void setJzjtseMonth(String jzjtseMonth) {
		this.jzjtseMonth = jzjtseMonth;
	}

	public boolean isPdf() {
		return isPdf;
	}

	public void setPdf(boolean isPdf) {
		this.isPdf = isPdf;
	}

	public boolean isExcel2007() {
		return isExcel2007;
	}

	public void setExcel2007(boolean isExcel2007) {
		this.isExcel2007 = isExcel2007;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isApply() {
		return isApply;
	}

	public void setApply(boolean isApply) {
		this.isApply = isApply;
	}

	public String getStartYear() {
		return startYear;
	}

	public void setStartYear(String startYear) {
		this.startYear = startYear;
	}

	public String getStartMonth() {
		return startMonth;
	}

	public void setStartMonth(String startMonth) {
		this.startMonth = startMonth;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getXiaoshoueYear() {
		return xiaoshoueYear;
	}

	public void setXiaoshoueYear(String xiaoshoueYear) {
		this.xiaoshoueYear = xiaoshoueYear;
	}

	public String getXiaoshoueMonth() {
		return xiaoshoueMonth;
	}

	public void setXiaoshoueMonth(String xiaoshoueMonth) {
		this.xiaoshoueMonth = xiaoshoueMonth;
	}

	public String getShuieYear() {
		return shuieYear;
	}

	public void setShuieYear(String shuieYear) {
		this.shuieYear = shuieYear;
	}

	public String getShuieMonth() {
		return shuieMonth;
	}

	public void setShuieMonth(String shuieMonth) {
		this.shuieMonth = shuieMonth;
	}

	public String getDisanxiangYear() {
		return disanxiangYear;
	}

	public void setDisanxiangYear(String disanxiangYear) {
		this.disanxiangYear = disanxiangYear;
	}

	public String getDisanxiangMonth() {
		return disanxiangMonth;
	}

	public void setDisanxiangMonth(String disanxiangMonth) {
		this.disanxiangMonth = disanxiangMonth;
	}

	public String getDisixiangYear() {
		return disixiangYear;
	}

	public void setDisixiangYear(String disixiangYear) {
		this.disixiangYear = disixiangYear;
	}

	public String getDisixiangMonth() {
		return disixiangMonth;
	}

	public void setDisixiangMonth(String disixiangMonth) {
		this.disixiangMonth = disixiangMonth;
	}

	public String getJyzsYear() {
		return jyzsYear;
	}

	public void setJyzsYear(String jyzsYear) {
		this.jyzsYear = jyzsYear;
	}

	public String getJyzsMonth() {
		return jyzsMonth;
	}

	public void setJyzsMonth(String jyzsMonth) {
		this.jyzsMonth = jyzsMonth;
	}

	public String getJysYear() {
		return jysYear;
	}

	public void setJysYear(String jysYear) {
		this.jysYear = jysYear;
	}

	public String getJysMonth() {
		return jysMonth;
	}

	public void setJysMonth(String jysMonth) {
		this.jysMonth = jysMonth;
	}

	public String getJinxiangshuiYear() {
		return jinxiangshuiYear;
	}

	public void setJinxiangshuiYear(String jinxiangshuiYear) {
		this.jinxiangshuiYear = jinxiangshuiYear;
	}

}
