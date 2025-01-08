package cn.tealc995.asmronline.util;

public class TimeFormatUtil {

	private static final String TIME_TEMPLATE="%02d:%02d";
	/**
	 * @Description:转换为时间格式输出
	 * @param: second
	 * @return: java.lang.String
	 * @Author: Leck
	 * @date: 2021/11/6
	 */
	public static String formatToClock(double second) {
		return String.format(TIME_TEMPLATE,(int)second/60,(int)second%60);
	}

	public static String formatToClock(int second) {
		return String.format(TIME_TEMPLATE,(int)second/60,(int)second%60);
	}



	/**
	 * @description: 转换时间格式
	 * @name: formatToClockFromMills
	 * @author: Leck
	 * @param:	mills	秒
	 * @return  java.lang.String
	 * @date:   2023/6/5
	 */
	public static String formatToClockFromMills(double mills) {
		mills=mills/1000;
		return String.format(TIME_TEMPLATE,(int)mills/60,(int)mills%60);
	}
}
