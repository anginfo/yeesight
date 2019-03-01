package com.lianxin.tax;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
/**
 * @time   2018年11月20日 上午9:46:59
 * @author zuoqb
 * @todo   处理PDF转成报税实体
 */
public class PDFToEtax {
	//初始化excel
    public static WritableWorkbook wwb = null;
    public static WritableSheet ws;
    
    /**
	 * 
	 * @time   2018年11月16日 下午5:50:10
	 * @author zuoqb
	 * @throws Exception 
	 * @todo   处理excel文件读取需要内容
	 * @return_type   TaxFiles
	 */
	public static TaxFilesModel pdfFileToTaxFiles(File file,TaxFilesModel taxFiles) throws Exception{
		if(file!=null&&!file.exists()){
			return taxFiles;
		}
		getPdfProperties2Bean(taxFiles, file);
		return taxFiles;
	}
    
	
	/**
	 * 
	 * @time   2018年11月16日 下午6:53:42
	 * @author zuoqb
	 * @todo   将pdf文件中相应cell数值写入实体类
	 * @return_type   void
	 */
	private static void getPdfProperties2Bean(TaxFilesModel taxFiles, File pdf) {
		/**
		 * 解析pdf通用属性
		 */
		Map<String,Object> map=getPdfContent(pdf.getAbsolutePath(),pdf);
		if(map!=null&&"操作成功".equals(map.get("msg")+"")){
			String textPath=map.get("path")+"";
			String textName=map.get("textName")+"";
			File outText=new File(textPath+textName);
			BufferedReader br=null;
			try {
				br = new BufferedReader(new FileReader(outText));
				String line = "";
				List<String> content=new ArrayList<String>();
		        while((line = br.readLine()) != null) {
		        	  content.add(line);
		        }
				Map<String,Object> readMap=readPdfValueByKey(content, Arrays.asList("增值税纳税申报表","增值税申报表"));
		        Map<String,Object> readMap1=readPdfValueByKey(content, Arrays.asList("一般纳税人适用"));
				if("true".equals(readMap.get("has").toString())&&"true".equals(readMap1.get("has").toString())){
					//（适用于增值税一般纳税人）
					pdfAddedGeneralTaxpayer(taxFiles, content);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(br != null) {
	                try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
	            }
			}
		}
	}
	
	/**
	 * @time   2018年11月16日 下午8:08:21
	 * @author zuoqb
	 * @throws Exception 
	 * @todo   适用于增值税一般纳税人-pdf
	 * @return_type   void
	 */
	public static void pdfAddedGeneralTaxpayer(TaxFilesModel taxFiles,List<String> content) throws Exception {
		taxFiles.setApply(true);//业务主表
		//处理日期
		List<String> dateKeys=Arrays.asList("税款所属期间","税款所属期","税款所属时间","所属时期");
		Map<String,Object> mapDate=readPdfValueByKey(content,dateKeys);
		if("true".equals(String.valueOf(mapDate.get("has")))){
			String startDate=mapDate.get("value").toString().replaceAll("金额单位：元（列至角分）", "").replaceAll("自", "").replaceAll("：", "").replaceAll(":", "");
			for(String key:dateKeys){
				startDate=startDate.replace(key, "");
			}
			if(StringUtils.isNotBlank(startDate)){
				startDate=startDate.replace("：", "").replaceAll(":", "").replaceAll("年", "").replaceAll("月", "")
						.replaceAll("日", "").split("至")[0].replaceAll(" ", "").replaceAll("-", "");
			}
			if(StringUtils.isNotBlank(startDate)){
				taxFiles.setStartDate(startDate);
				taxFiles.setStartYear(startDate.substring(0,4));
				taxFiles.setStartMonth(Integer.valueOf(startDate.substring(4,6))+"");
			}
		}
		//公司名称
		Map<String,Object> mapCompany=new HashMap<String, Object>();
		Map<String,Object> fddbr= readPdfValueByKey(content, Arrays.asList("法定代表人"));
		if("false".equals(String.valueOf(fddbr.get("has")))){
			//没有法定代表人  比如上海 厦门  他们公司名称位置格式为- 纳税人名称：上海龙诺实业有限公司 纳税人识别号：9131011270339814XA
			mapCompany=readPdfValueByKey(content, Arrays.asList("纳税人名称"));
			if("true".equals(String.valueOf(mapCompany.get("has")))){
				String value=mapCompany.get("value").toString();
				if(value.indexOf("纳税人识别号")!=-1){
					value=value.substring(0,value.indexOf("纳税人识别号"));
				}
				mapCompany.put("value", value.replaceAll(":", "").replaceAll("：", ""));
			}
		}else{
			/**
			 * 格式
			 * 纳税人名称
				（公章）：
				大连俱泰科技发展
				有限公司
				法定代表人
			 */
			mapCompany=readPdfValueByKey(content, "纳税人名称","法定代表人");
		}
		if("true".equals(String.valueOf(mapCompany.get("has")))){
			String companyName=mapCompany.get("value").toString();
			companyName=companyName.replace("：", "").replaceAll(":", "").replaceAll("纳税人名称", "").replaceAll("（公章）", "").replaceAll("（公章）", "");
			if(companyName.indexOf("金额单位")!=-1){
				companyName=companyName.substring(0,companyName.indexOf("金额单位")).trim();
			}
			taxFiles.setCompanyName(companyName);
		}
		/**
		 * 解析pdf 数据字段
		 */
		/**
		 * 销售额-（一）按适用税率计税销售额
		 */
		//上海 厦门处理  他们key是（一）按适用税率征税销售额
		String xiaoshoueYear=readPdfValueByPosition(content, Arrays.asList("（一）按适用税率计税销售额","（一）按适用税率征税销售额"), 2);//销售额年累-（一）按适用税率计税销售额
		taxFiles.setXiaoshoueYear(xiaoshoueYear);
		String xiaoshoueMonth=readPdfValueByPosition(content, Arrays.asList("（一）按适用税率计税销售额","（一）按适用税率征税销售额"), 1);//销售额月累-（一）按适用税率计税销售额
		taxFiles.setXiaoshoueMonth(xiaoshoueMonth);
		/**
		 * 税额-销项税额
		 */
		String shuieYear=readPdfValueByPosition(content, Arrays.asList("销项税额"), 2);//税额年累-销项税额
		taxFiles.setShuieYear(shuieYear);
		String shuieMonth=readPdfValueByPosition(content, Arrays.asList("销项税额"), 1);//税额月累-销项税额
		taxFiles.setShuieMonth(shuieMonth);
		
		/**
		 * 第三项-（三）免、抵、退办法出口销售额
		 */
		String disanxiangYear=readPdfValueByPosition(content, Arrays.asList("（三）免、抵、退办法出口销售额"), 2);//第三项年累-（三）免、抵、退办法出口销售额
		taxFiles.setDisanxiangYear(disanxiangYear);
		String disanxiangMonth=readPdfValueByPosition(content, Arrays.asList("（三）免、抵、退办法出口销售额"), 1);//第三项月累-（三）免、抵、退办法出口销售额
		taxFiles.setDisanxiangMonth(disanxiangMonth);
		/**
		 * 第四项-（四）免税销售额
		 */
		String disixiangYear=readPdfValueByPosition(content, Arrays.asList("（四）免税销售额"), 2);//第四项年累-（四）免税销售额
		taxFiles.setDisixiangYear(disixiangYear);
		String disixiangMonth=readPdfValueByPosition(content, Arrays.asList("（四）免税销售额"), 1);//第四项月累-（四）免税销售额
		taxFiles.setDisixiangMonth(disixiangMonth);
		
		
		/**
		 * 简易征收-（二）按简易办法计税销售额
		 */
		String jyzsYear=readPdfValueByPosition(content, Arrays.asList("（二）按简易办法计税销售额", "（二）按简易征收办法征税销售额"), 2);//简易征收年累-（二）按简易办法计税销售额
		taxFiles.setJyzsYear(jyzsYear);
		String jyzsMonth=readPdfValueByPosition(content, Arrays.asList("（二）按简易办法计税销售额", "（二）按简易征收办法征税销售额"), 1);//简易征收月累-（二）按简易办法计税销售额
		taxFiles.setJyzsMonth(jyzsMonth);
		
		/**
		 * 简易税-简易计税办法计算的应纳税额
		 */
		String jysYear=readPdfValueByPosition(content, Arrays.asList("简易计税办法计算的应纳税额"), 2);//简易税年累-简易计税办法计算的应纳税额
		taxFiles.setJysYear(jysYear);
		String jysMonth=readPdfValueByPosition(content, Arrays.asList("简易计税办法计算的应纳税额"), 1);//简易税月累-简易计税办法计算的应纳税额
		taxFiles.setJysMonth(jysMonth);
		
		/**
		 * 即征即退销售额  
		 */
		double jzjtxseYear0=Double.parseDouble(readPdfValueByPosition(content, Arrays.asList("（一）按适用税率计税销售额","（一）按适用税率征税销售额"), 4));
		double jzjtxseYear1=Double.parseDouble(readPdfValueByPosition(content, Arrays.asList("（二）按简易办法计税销售额", "（二）按简易征收办法征税销售额"), 4));
		String jzjtxseYear=(jzjtxseYear0+jzjtxseYear1)+"";
		taxFiles.setJzjtxseYear(jzjtxseYear);
		double jzjtxseMonth0=Double.parseDouble(readPdfValueByPosition(content, Arrays.asList("（一）按适用税率计税销售额","（一）按适用税率征税销售额"), 3));
		double jzjtxseMonth1=Double.parseDouble(readPdfValueByPosition(content, Arrays.asList("（二）按简易办法计税销售额", "（二）按简易征收办法征税销售额"), 3));
		String jzjtxseMonth=(jzjtxseMonth0+jzjtxseMonth1)+"";
		taxFiles.setJzjtxseMonth(jzjtxseMonth);
		
		/**
		 * 即征即退税额  
		 */
		double jzjtseYear0=Double.parseDouble(readPdfValueByPosition(content, Arrays.asList("销项税额"), 4));
		double jzjtseYear1=Double.parseDouble(readPdfValueByPosition(content, Arrays.asList("简易计税办法计算的应纳税额"), 4));
		String jzjtseYear=(jzjtseYear0+jzjtseYear1)+"";
		taxFiles.setJzjtseYear(jzjtseYear);
		double jzjtseMonth0=Double.parseDouble(readPdfValueByPosition(content, Arrays.asList("销项税额"), 3));
		double jzjtseMonth1=Double.parseDouble(readPdfValueByPosition(content, Arrays.asList("简易计税办法计算的应纳税额"), 3));
		String jzjtseMonth=(jzjtseMonth0+jzjtseMonth1)+"";
		taxFiles.setJzjtseMonth(jzjtseMonth);
		
		
		/**
		 * 进项税-进项税额
		 */
		double jinxiangshuiYear0=Double.parseDouble(readPdfValueByPosition(content, Arrays.asList("进项税额"), 2));
		//double jinxiangshuiYear1=Double.parseDouble(readPdfValueByPosition(content, Arrays.asList("进项税额"), 4));
		double jinxiangshuiYear1=0;
		String jinxiangshuiYear=(jinxiangshuiYear0+jinxiangshuiYear1)+"";//进项税年累-进项税额
		taxFiles.setJinxiangshuiYear(jinxiangshuiYear);
	}
    
	/**
	 * @time   2018年11月20日 上午9:57:04
	 * @author zuoqb
	 * @todo   读取pdf文件内容
	 */
    public static Map<String,Object> getPdfContent(String path,File pdf) {
    	Map<String,Object> map=new HashMap<String,Object>();
        PDDocument document=null;
        String content = null;
        BufferedWriter wr=null;
        String textName=null;
        try {
            File input = pdf; 
            int pos = pdf.getName().lastIndexOf('.');
	        if (pos != -1) {
	        	textName=pdf.getName().substring(0,pos)+".txt";
	        }
            String outPath=pdf.getPath().replaceAll(pdf.getName(), "");
            File output = new File(textName);
            document = PDDocument.load(input);
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            pdfTextStripper.setStartPage(1);
			pdfTextStripper.setEndPage(1);//只读取第一页
			content = pdfTextStripper.getText(document);
			//System.out.println(content);
			//生成text只是为了数据核对
            wr = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(outPath+output)));
            pdfTextStripper.writeText(document, wr);
            map.put("textName", textName);
            map.put("path", outPath);
            map.put("content", content);
            map.put("success", true);
            map.put("msg", "操作成功");
            if (document != null) {
            	document.close();
            }
            if(wr!=null){
            	wr.close();
            }
        } catch (Exception e) {
        	map.put("content", "");
        	map.put("success", false);
        	map.put("msg", e.getMessage());
            e.printStackTrace();
        }finally{
        	try {
        		if (document != null) {
                	document.close();
                }
                if(wr!=null){
                	wr.close();
                }
			} catch (Exception e2) {
			}
        }
        return map;
    }
    
    /**
	 * 
	 * @time   2018年11月20日 下午6:10:05
	 * @author zuoqb
	 * @todo   根据值读取数据 一般处理日期 公司 判断主附件
	 * @return_type   Map<String,Object>
	 */
    public static Map<String,Object> readPdfValueByKey(List<String> content,List<String> keys) {
    	Map<String,Object>  map=new HashMap<String, Object>();
    	for(String key:keys){
    		map=readPdfValueByKey(content, key,null);
    		if("true".equals(map.get("has").toString())){
    			break; 
    		}
    	}
    	return map;
    }
    /**
     * 
     * @time   2018年11月20日 下午4:45:17
     * @author zuoqb
     * @todo   解析指定pdf位置的值,返回指定位置数值---一般处理数值
     * @return_type   String
     */
    public static String readPdfValueByPosition(List<String> content,List<String> keys,Integer position) {
    	String result = "";
    	boolean has=false;
        try {
        	for(String key:keys){
        		for(String line:content) {
        			if(StringUtils.isNotBlank(line)&&line.indexOf(key)!=-1){
        				has=true;
        				result=line.substring(line.indexOf(key),line.length()).replaceAll(key, "");
        				result=result.trim();
        				break;
        			}
        		}
        	}
        }catch (Exception e) {
            e.printStackTrace();
        }
        if(has){
        	String[] splited = result.split("\\s+");
        	if(splited!=null&&splited.length>0&&splited.length>position){
        		result= splited[position];
        		result=result.replaceAll("，", "").replaceAll(",", "");
        	}
        }
        return result;
    }
	/**
	 * 
	 * @time   2018年11月20日 下午6:10:05
	 * @author zuoqb
	 * @todo   根据值读取数据 一般处理日期 公司 判断主附件
	 * @return_type   Map<String,Object>
	 */
    public static Map<String,Object> readPdfValueByKey(List<String> content,String key,String nextKey) {
    	Map<String,Object> map=new HashMap<String, Object>();
    	String value = "";
    	map.put("has", false);
    	map.put("value", value);
    	map.put("key", key);
        try {
            for(String line:content){
            	line=StringUtils.deleteWhitespace(line);//去掉空格
            	if(StringUtils.isBlank(nextKey)){
            		if(StringUtils.isNotBlank(line)&&line.indexOf(key)!=-1){
            			map.put("has", true);
                		line=line.substring(line.indexOf(key),line.length()).replaceAll(key, "");
                    	map.put("value", line);
                		break;
            		}
            	}else{
            		if(StringUtils.isNotBlank(line)&&line.indexOf(key)!=-1){
            			map.put("has", true);
            			int start=getIndex(content,line);
            			int end=getIndex(content,nextKey);
            			for(int x=start;x<end+1;x++){
            				line+=content.get(x);
            			}
                		line=line.substring(line.indexOf(key),line.length()).replaceAll(key, "");;
                    	map.put("value", line);
                		break;
            		}
            	}
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
  
    public  static int getIndex(List<String> content,String key){
    	int index=0;
    	for(int x=0;x<content.size();x++){
    		if(content.get(x).indexOf(key)!=-1){
    			index=x;
    			break;
    		}
    	}
    	return index;
    }
}
