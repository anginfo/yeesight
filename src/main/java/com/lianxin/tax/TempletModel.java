package com.lianxin.tax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @time   2018年11月17日 下午2:57:59
 * @author zuoqb
 * @todo   生成的模板信息  对处理完文件之后获取的List<TaxFilesModel>再次封装
 */
public class TempletModel {
	private List<TaxFilesModel> orgTaxs=new ArrayList<TaxFilesModel>();//原始数据
	private String excelName;//Excel名称 公司名称+月份（比如海联1-9）
	private List<String> years=new ArrayList<String>();//全部年份  按照冲小到大排序
	/**
	 * 按照年划分数据，并且每一年数据按照月份从小到大排序
	 */
	private Map<String,List<TaxFilesModel>> mapTax=new HashMap<String,List<TaxFilesModel>>();
	/**
	 * 最大时间的实体，最多六个  
	 * 按照月份从小到大排序
	 * 生成年销售额比较第四行（取集合最后一个）以及ETAX\VAT
	 */
	private List<TaxFilesModel> currentSixMonthTaxs=new ArrayList<TaxFilesModel>();
	/**
	 * 上一年同期月份数据
	 * 最多6个生成年销售额比较第三行
	 */
	private List<TaxFilesModel> lastYearSixMonthTaxs=new ArrayList<TaxFilesModel>();
	
	/**
	 * 上一年最后一个月（必须是十二月）数据
	 * 用于生成年销售额比较第二行数据
	 */
	private TaxFilesModel lastOneYearDecember=new TaxFilesModel();
	/**
	 * 上两年最后一个月（必须是十二月）数据
	 * 用于生成年销售额比较第一行数据
	 */
	private TaxFilesModel lastTwoYearDecember=new TaxFilesModel();
	
	private String title1;//第一行标题 格式2016.1-12
	private String title2;//第二行标题 格式2017.1-12
	private String title3;//第三行标题 格式2017.1-9
	private String title4;//第四行标题 格式2018.1-9
	private String allName;//文件全名称 带xls、xlsx
	public String getExcelName() {
		return excelName;
	}
	public void setExcelName(String excelName) {
		this.excelName = excelName;
	}
	public String getAllName() {
		return allName;
	}
	public void setAllName(String allName) {
		this.allName = allName;
	}
	public Map<String, List<TaxFilesModel>> getMapTax() {
		return mapTax;
	}
	public void setMapTax(Map<String, List<TaxFilesModel>> mapTax) {
		this.mapTax = mapTax;
	}
	public List<String> getYears() {
		return years;
	}
	public void setYears(List<String> years) {
		this.years = years;
	}
	public List<TaxFilesModel> getOrgTaxs() {
		return orgTaxs;
	}
	public void setOrgTaxs(List<TaxFilesModel> orgTaxs) {
		this.orgTaxs = orgTaxs;
	}
	public List<TaxFilesModel> getCurrentSixMonthTaxs() {
		return currentSixMonthTaxs;
	}
	public void setCurrentSixMonthTaxs(List<TaxFilesModel> currentSixMonthTaxs) {
		this.currentSixMonthTaxs = currentSixMonthTaxs;
	}
	public List<TaxFilesModel> getLastYearSixMonthTaxs() {
		return lastYearSixMonthTaxs;
	}
	public void setLastYearSixMonthTaxs(List<TaxFilesModel> lastYearSixMonthTaxs) {
		this.lastYearSixMonthTaxs = lastYearSixMonthTaxs;
	}
	public TaxFilesModel getLastOneYearDecember() {
		return lastOneYearDecember;
	}
	public void setLastOneYearDecember(TaxFilesModel lastOneYearDecember) {
		this.lastOneYearDecember = lastOneYearDecember;
	}
	public TaxFilesModel getLastTwoYearDecember() {
		return lastTwoYearDecember;
	}
	public void setLastTwoYearDecember(TaxFilesModel lastTwoYearDecember) {
		this.lastTwoYearDecember = lastTwoYearDecember;
	}
	public String getTitle1() {
		return title1;
	}
	public void setTitle1(String title1) {
		this.title1 = title1;
	}
	public String getTitle2() {
		return title2;
	}
	public void setTitle2(String title2) {
		this.title2 = title2;
	}
	public String getTitle3() {
		return title3;
	}
	public void setTitle3(String title3) {
		this.title3 = title3;
	}
	public String getTitle4() {
		return title4;
	}
	public void setTitle4(String title4) {
		this.title4 = title4;
	}
	
}
