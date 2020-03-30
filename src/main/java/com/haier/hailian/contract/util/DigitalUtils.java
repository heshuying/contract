package com.haier.hailian.contract.util;

import java.math.BigDecimal;
import java.util.regex.Pattern;

//数值格式判断工具类
public class DigitalUtils {
	/*
	 * 判断是否为整数
	 * 
	 * @param str 传入的字符串
	 * 
	 * @return 是整数返回true,否则返回false
	 */
	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}

	/**
	 * 是否是数字
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		try {
			new BigDecimal(str);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * String 转 Integer
	 * String为空或者“” Integer为空
	 * @param str
	 * @return
	 */
	public static Integer toInteger(String str) {
		Integer result = null;
		if (str != null && !str.isEmpty()){result = Integer.valueOf(str);}
		return result;
	}

}
