package com.hailian.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * @time   2018年9月27日 上午1:31:12
 * @author zuoqb
 * @todo  
 * 正则表达式 的用法主要是4种方面的使用 
 * 匹配，分割，替换，获取. 
 * 用一些简单的符号来代表代码的操作 
 */
public class RexUtils {
	public static void main(String[] args) {
		//针对字符串处理 
		//校验qq的reg正则表达式 
		//这里的\w 是指的是[a-zA-Z0-9],还有一个重要的是?,*.+这三个分别 
		//?表示出现1次或者1次都没有， 
		//+表示出现1次或者n次， 
		//*表示出现0次或者n次， 
		//还有些特殊的写法X{n}恰好n次X{n,}至少n次，X{n,m}n次到m次， 
		String mathReg = "[1-9]\\d{4,19}";
		String divisionReg = "(.)\\1+";
		//\\b 是指的边界值 
		String getStringReg = "\\b\\w{3}\\b";
		//字符串匹配(首位是除0 的字符串) 
		RexUtils.getMatch("739295732", mathReg);
		RexUtils.getMatch("039295732", mathReg);
		//字符串的替换 
		//去除叠词 
		RexUtils.getReplace("12111123ASDASDAAADDD", divisionReg, "$1");
		//字符串的分割 
		//切割叠词,重复的 
		//这里要知道一个组的概念(.)\\1第二个和第一个至相同 
		RexUtils.getDivision("aadddddasdasdasaaaaaassssfq", divisionReg);
		//字符串的获取 
		//现在获取三个字符串取出 
		RexUtils.getString("ming tian jiu yao fangjia le ", getStringReg);
		String sql="date_format(#{time},'%Y-%m-%d') and cxw_code like CONCAT('%',#{cbkCode},'%') limit ${startIndex},${pageSize} ";
		String m="\\{([^\\}]+)\\}";//匹配所有{}
		RexUtils.getString(sql, m);
		String m1="\\$\\{([^\\}]+)\\}";//匹配所有${}
		RexUtils.getString(sql, m1);
		String m2="\\#\\{([^\\}]+)\\}";//匹配所有#{}
		RexUtils.getString(sql, m2);
		String m3="\\#\\{([^\\}]+)\\}|\\$\\{([^\\}]+)\\}";//匹配所有${}与匹配所有#{}
		RexUtils.getString(sql, m3);
		System.out.println(sql.replaceAll(m3, "?"));
	}

	/**
	 * @time   2018年9月27日 上午1:32:40
	 * @author zuoqb
	 * @todo   获取查询的字符串  将匹配的字符串取出 
	 */
	public static List<String> getString(String str, String regx) {
		List<String> result=new ArrayList<String>();
		//1.将正在表达式封装成对象Patten 类来实现 
		Pattern pattern = Pattern.compile(regx);
		//2.将字符串和正则表达式相关联 
		Matcher matcher = pattern.matcher(str);
		//3.String 对象中的matches 方法就是通过这个Matcher和pattern来实现的。 
		//查找符合规则的子串 
		// matched
		while (matcher.find()) {
			//System.out.println(matcher.group());
			//获取的字符串的首位置和末位置 
			//System.out.println(matcher.start() + "--" + matcher.end());//获取的字符串的首位置 末位置 
			result.add(matcher.group());
		}
		return result;
	}

	/**
	 * @time   2018年9月27日 上午1:32:11
	 * @author zuoqb
	 * @todo   字符串的分割 
	 */
	public static String[] getDivision(String str, String regx) {
		String[] dataStr = str.split(regx);
		for (String s : dataStr) {
			System.out.println("正则表达式分割++" + s);
		}
		return dataStr;
	}

	/**
	 * @time   2018年9月27日 上午1:32:20
	 * @author zuoqb
	 * @todo   字符串的替换 
	 * @return_type   void
	 */
	public static String getReplace(String str, String regx, String replaceStr) {
		String stri = str.replaceAll(regx, replaceStr);
		//System.out.println("正则表达式替换" + stri);
		return stri;
	}

	/** 
	 * @time   2018年9月27日 上午1:32:28
	 * @author zuoqb
	 * @todo   字符串处理之匹配 
	 * String类中的match 方法 
	 */
	public static boolean getMatch(String str, String regx) {
		System.out.println("正则表达匹配" + str.matches(regx));
		return str.matches(regx);
	}
}
