package org.basis.framework.utils;

import java.math.BigDecimal;

public class BigDecimalUtil {

	// 默认除法运算精度
	private static final int DEF_DIV_SCALE = 10;
	/**
	 * 判断是不是null  是null返回0    否则直接返回
	 * @param data
	 * @return
	 */
	public static BigDecimal forBigDecimal(BigDecimal data){
		return data == null ? new BigDecimal(0) : data;
	}
	/**
	 * 将String类型的数据 转换成 BigDecimal类型
	 * 报错时 返回null
	 * @param string
	 * @return
	 */
	public static BigDecimal stringToBigDecimal(String string){
		try {
			return new BigDecimal(string);
		} catch (Exception e) {
			return null;
		}
	}
	
	
	/**
	 * 将String类型的数据 转换成 BigDecimal类型
	 * 报错时 返回  0
	 * @param string
	 * @return
	 */
	public static BigDecimal stringToBigDecimal0(String string){
		try {
			return new BigDecimal(string);
		} catch (Exception e) {
			return new BigDecimal(0);
		}
	}
	
	/**
	 * 将数据转换成double 类型   报错返回0
	 * @param data
	 * @return
	 */
	public static double doubleValue(BigDecimal data){
		try {
			return data.doubleValue();
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 提供精确的加法运算。
	 * @param v1 被加数
	 * @param v2 加数
	 * @return 两个参数的和
	 */
	public static double add(double v1, double v2) {
		BigDecimal b1;
		try {
			b1 = new BigDecimal(Double.toString(v1));
		} catch (Exception e) {
			b1 = new BigDecimal(0);
		}
		BigDecimal b2;
		try {
			b2 = new BigDecimal(Double.toString(v2));
		} catch (Exception e) {
			b2 = new BigDecimal(0);
		}
		return b1.add(b2).doubleValue();
	}
	
	/**
	 * 两个数相加
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static BigDecimal add(BigDecimal v1, BigDecimal v2) {
		if(v1 == null){
			return v2 == null?new BigDecimal(0):v2;
		}else {
			return v2 == null?v1:v1.add(v2);
		}
	}

	/**
	 * 提供精确的减法运算。
	 * @param v1 被减数
	 * @param v2 减数
	 * @return 两个参数的差
	 */
	public static double sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}
	
	/**
	 * 提供精确的减法运算。
	 * @param v1   被减数
	 * @param v2    减数
	 * @return 两个参数的差
	 */
	public static BigDecimal subtract(BigDecimal v1, BigDecimal v2) {
		if(v1 == null){
			return v2 == null?new BigDecimal(0):new BigDecimal(0).subtract(v2);
		}else{
			return v2 == null?v1:v1.subtract(v2);
		}
	}
	

	/**
	 * 提供精确的乘法运算。
	 * @param v1 被乘数
	 * @param v2 乘数
	 * @return 两个参数的积
	 */
	public static double mul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}
	
	/**
	 * 乘法运
	 * @param v1
	 * @param v2
	 * @param scale  小数点后精确度
	 * @return
	 */
	public static double mul(double v1, double v2,int scale) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		double doubleValue = b1.multiply(b2).doubleValue();
		return round(doubleValue, scale);
	}
	
	/**
	 * 错误的值做0处理    乘法运
	 * @param v1
	 * @param v2
	 * @param scale  小数点后精确度
	 * @return
	 */
	public static double mulDoFail(Object v1, Object v2,int scale) {
		BigDecimal b1;
		try {
			b1 = new BigDecimal(v1.toString());
		} catch (Exception e) {
			b1 = new BigDecimal(0);
		}
		BigDecimal b2;
		try {
			b2 = new BigDecimal(v2.toString());
		} catch (Exception e) {
			b2 = new BigDecimal(0);
		}
		double doubleValue = b1.multiply(b2).doubleValue();
		return round(doubleValue, scale);
	}

	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
	 * @param v1 被除数
	 * @param v2 除数
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2) {
		return div(v1, v2, DEF_DIV_SCALE);
	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
	 * 
	 * @param v1 被除数
	 * @param v2 除数
	 * @param scale 表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 提供精确的小数位四舍五入处理。
	 * @param v 需要四舍五入的数字
	 * @param scale 小数点后保留几位
	 * @return 四舍五入后的结果
	 */
	public static double round(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 获取小数点位数
	 * @param decimal
	 * @return
	 */
	public static int getDcimalDigits(BigDecimal decimal){
		String decimalStr = decimal.stripTrailingZeros().toPlainString();
		int indexOf = decimalStr.indexOf(".");
		int dcimalDigits = 0;
		if(indexOf > 0){
			dcimalDigits = decimalStr.length() - 1 - indexOf;
		}
		return dcimalDigits;
	}

	/**
	 * 为空时填充默认值
	 * @param val
	 * @return
	 */
	public static BigDecimal defaultValue(BigDecimal val){
		return val == null ? BigDecimal.ZERO : val;
	}

	public static BigDecimal divide(BigDecimal val1,BigDecimal val2){
		if (val2==null||val2.compareTo(BigDecimal.ZERO)!=1){
			return BigDecimal.ZERO;
		}
		return val1.divide(val2,5,BigDecimal.ROUND_DOWN);
	}
}
