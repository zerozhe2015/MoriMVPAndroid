package com.moriarty.base.util;//package cn.ebatech.base.util;
//
//import android.annotation.SuppressLint;
//import android.text.Html;
//import android.text.Spanned;
//
//import com.msyd.client.R;
//
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.text.DecimalFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
///**
// * 展示字符utils
// *
// * @author viyu
// *
// */
//public class UITextUtils {
//
//	public static long toLong(String str) {
//		if (str == null || str.equals("")) {
//			return 0L;
//		}
//		long value = 0L;
//		try {
//			value = Long.parseLong(str);
//		} catch (NumberFormatException e) {
//			value = 0L;
//		}
//		return value;
//	}
//
//	public static double toDouble(String str) {
//		if (str == null || str.equals("")) {
//			return 0L;
//		}
//		double value = 0.0;
//		try {
//			value = Double.parseDouble(str);
//		} catch (NumberFormatException e) {
//			value = 0.0;
//		}
//		return value;
//	}
//
//	public static int toInt(String str) {
//		if (str == null || str.equals("")) {
//			return 0;
//		}
//		int value = -1;
//		try {
//			value = Integer.parseInt(str);
//		} catch (NumberFormatException e) {
//			value = -1;
//		}
//		return value;
//	}
//
//	/**
//	 * 只支持yyyy-MM-dd HH:mm:ss格式
//	 *
//	 * @param str
//	 * @return
//	 */
//	@SuppressLint("SimpleDateFormat")
//	public static long getDateFromString(String str) {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		long l = 0;
//		try {
//			Date d = sdf.parse(str);
//			if (d != null) {
//				l = d.getTime();
//			}
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		return l;
//	}
//
//	/**
//	 * 转换一个double值到百分之x，保留两个小数点
//	 *
//	 * @param number
//	 * @param withPercent
//	 *            要不要带着符号%
//	 * @return
//	 */
//	public static String formatDoubleToPercent(double number, boolean withPercent) {
//		String text = null;
//		try {
//			text = String.format(CommonUtils.getStringRes(withPercent ? R.string.loan_percent_with_unit : R.string.loan_percent), number);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (text == null) {
//				text = String.valueOf(number);
//			}
//		}
//		return text;
//	}
//
//	@SuppressLint("SimpleDateFormat")
//	public static String formatCurrentTime() {
//		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
//		return sdf.format(new Date(System.currentTimeMillis()));
//	}
//
//	@SuppressLint("DefaultLocale")
//	public static String formatLeftTime(long millisUntilFinished) {
//		int hours = (int) (millisUntilFinished / (60 * 60 * 1000));
//		int minutes = (int) ((millisUntilFinished / (60 * 1000)) % 60);
//		int seconds = (int) ((millisUntilFinished / 1000) % 60);
//		String str = "";
//		if (hours > 0) {
//			str = String.format("%01d小时%02d分%02d秒", hours, minutes, seconds);
//		} else if (minutes > 0) {
//			str = String.format("%02d分%02d秒", minutes, seconds);
//		} else if (seconds > 0) {
//			str = String.format("%02d秒", seconds);
//		}
//		return str;
//	}
//
//	@SuppressLint("DefaultLocale")
//	public static Spanned formatLeftDayTime(long millisUntilFinished) {
//		int days = (int) (millisUntilFinished / (60 * 60 * 1000 * 24));
//		int hours = (int) (millisUntilFinished / (60 * 60 * 1000) % 24);
//		int minutes = (int) ((millisUntilFinished / (60 * 1000)) % 60);
//		int seconds = (int) ((millisUntilFinished / 1000) % 60);
//		String str = "";
//		if (days > 0) {
//			str = String
//					.format("%01d<font color=\"#2E3031\">天</font>%01d<font color=\"#2E3031\">小时</font>%02d<font color=\"#2E3031\">分</font>%02d<font color=\"#2E3031\">秒</font>",
//							days, hours, minutes, seconds);
//		} else if (hours > 0) {
//			str = String.format("%01d<font color=\"#2E3031\">小时</font>%02d<font color=\"#2E3031\">分</font>%02d<font color=\"#2E3031\">秒</font>", hours, minutes,
//					seconds);
//		} else if (minutes > 0) {
//			str = String.format("%02d<font color=\"#2E3031\">分</font>%02d<font color=\"#2E3031\">秒</font>", minutes, seconds);
//		} else if (seconds > 0) {
//			str = String.format("%02d<font color=\"#2E3031\">秒</font>", seconds);
//		}
//		return Html.fromHtml(str);
//	}
//
//	/**
//	 * 转换元到万
//	 *
//	 * @param yuanNumber
//	 * @return
//	 */
//	public static String formatDoubleToWanyuan(double yuanNumber) {
//		double wanYuan = yuanNumber / 10000;
//		String text = null;
//		try {
//			text = String.format(CommonUtils.getStringRes(R.string.loan_wan), wanYuan);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (text == null) {
//				text = String.valueOf(wanYuan);
//			}
//		}
//		return text;
//	}
//
//	/**
//	 * 返回收益期限的字符说明，比如120天或者3个月
//	 *
//	 * @param termCount
//	 * @param termUnit
//	 *            还款周期单位 M 1:日 3:月
//	 * @param withUnit
//	 *            要不要单位
//	 * @return
//	 */
//	public static String formatInvestTime(int termCount, int termUnit, boolean withUnit) {
//		String text = null;
//		try {
//			if (termUnit == 1) {
//				text = String.format(CommonUtils.getStringRes(withUnit ? R.string.loan_invest_day_withunit : R.string.loan_invest_day), termCount);
//			} else if (termUnit == 3) {
//				text = String.format(CommonUtils.getStringRes(withUnit ? R.string.loan_invest_month_withunit : R.string.loan_invest_month), termCount);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (text == null) {
//				text = String.valueOf(termCount);
//			}
//		}
//		return text;
//	}
//
//	public static int calcInvestTimeToDays(int termCount, int termUnit) {
//		int days = 0;
//		if (termUnit == 1) {
//			days = termCount;
//		} else if (termUnit == 3) {
//			days = termCount * 30;
//		}
//		return days;
//	}
//
//	/**
//	 * 转换一个double成xxx.xx元或者万元
//	 *
//	 * @param value
//	 * @return
//	 */
//	public static String formatDoubleToYuan(double value) {
//		String text = null;
//		try {
//			text = String.format(CommonUtils.getStringRes(R.string.loan_yuan), value);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (text == null) {
//				text = String.valueOf(value);
//			}
//		}
//		return text;
//	}
//
//	/**
//	 * 格式化已售的百分比
//	 *
//	 * @param total
//	 * @param yishou
//	 * @return
//	 */
//	public static String formatYishouPercent(double total, double yishou, boolean withSuf) {
//		int percent = 0;
//		if (total > 0.0) {
//			percent = (int) ((yishou / total) * 100);
//		} else {
//			percent = 100;
//		}
//		String text = null;
//		try {
//			if (withSuf == true) {
//				text = String.format(CommonUtils.getStringRes(R.string.loan_yishou_with_suf), percent);
//			} else {
//				text = String.format(CommonUtils.getStringRes(R.string.loan_yishou), percent);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (text == null) {
//				text = String.valueOf(percent);
//			}
//		}
//		return text;
//	}
//
//	/**
//	 * 把已知的double表示的百分比格式化成百分之几
//	 * @param schedule
//	 * @param withSuf
//	 * @return
//	 */
//	public static String formatYishouPercent(double schedule, boolean withSuf) {
//		int percent = (int) (schedule * 100);
//		String text = null;
//		try {
//			if (withSuf == true) {
//				text = String.format(CommonUtils.getStringRes(R.string.loan_yishou_with_suf), percent);
//			} else {
//				text = String.format(CommonUtils.getStringRes(R.string.loan_yishou), percent);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (text == null) {
//				text = String.valueOf(percent);
//			}
//		}
//		return text;
//	}
//
//	/**
//	 * 格式化金额,可以指定带不带单位，xx,xxx.xx
//	 * @param value
//	 * @return
//	 */
//	public static String formatDouble(double value, boolean withYuan){
//		DecimalFormat df = null;
//		BigDecimal amount=new BigDecimal(value);
//		df = new DecimalFormat("###,##0.00");
//	    df.setRoundingMode(RoundingMode.HALF_UP);
//		String result = df.format(amount);
//		if(withYuan)
//			result=result+"元";
//		return result;
//	}
//
//	/**
//	 * 格式化金额-元
//	 * @param value
//	 * @param scale 0-2
//	 * @return
//	 */
//	public static String formatDouble(double value, int scale, boolean withYuan) {
//		DecimalFormat df = null;
//		if (scale <= 0) {
//			df = new DecimalFormat("###,###");
//		} else {
//			df = new DecimalFormat("###,##0.00");
//		}
//	    df.setRoundingMode(RoundingMode.HALF_UP);
//	    BigDecimal amount=new BigDecimal(value);
//		String result = df.format(amount);
//		if(withYuan)
//			result=result+"元";
//		return result;
//	}
//
//	/**
//	 * 将已经用逗号格式化的字符串转换成double
//	 * @return
//	 */
//	public static double formatedToDouble(String str){
//		if (str == null || str.equals("")) {
//			return 0L;
//		}
//		String s=str.replaceAll(",", "");
//		double value = 0.0;
//		try {
//			value = Double.parseDouble(s);
//		} catch (NumberFormatException e) {
//			value = 0.0;
//		}
//		return value;
//	}
//	  @SuppressLint("SimpleDateFormat")
//      public static String formatTimeWithYear(long time) {
//      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//      return sdf.format(new Date(time));
//      }
//
//
//
//
//
//	/**
//	 * 数字金额大写转换，思想先写个完整的然后将如零拾替换成零 要用到正则表达式
//	 */
//	public static String formatGoldGram(double g){
//		return  new DecimalFormat("0.########").format(g);
//	}
//
//
//
//
//
//	/**
//	 * 数字金额大写转换，思想先写个完整的然后将如零拾替换成零 要用到正则表达式
//	 */
//	public static String digitUppercase(double n) {
//		String fraction[] = { "角", "分" };
//		String digit[] = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
//		String unit[][] = { { "元", "万", "亿" }, { "", "拾", "佰", "仟" } };
//
//		String head = n < 0 ? "负" : "";
//		n = Math.abs(n);
//
//		String s = "";
//		for (int i = 0; i < fraction.length; i++) {
//			s += (digit[(int) (Math.floor(n * 10 * Math.pow(10, i)) % 10)] + fraction[i])
//					.replaceAll("(零.)+", "");
//		}
//		if (s.length() < 1) {
//			s = "整";
//		}
//		int integerPart = (int) Math.floor(n);
//
//		for (int i = 0; i < unit[0].length && integerPart > 0; i++) {
//			String p = "";
//			for (int j = 0; j < unit[1].length && n > 0; j++) {
//				p = digit[integerPart % 10] + unit[1][j] + p;
//				integerPart = integerPart / 10;
//			}
//			s = p.replaceAll("(零.)*零$", "").replaceAll("^$", "零") + unit[0][i]
//					+ s;
//		}
//		return head
//				+ s.replaceAll("(零.)*零元", "元").replaceFirst("(零.)+", "")
//				.replaceAll("(零.)+", "零").replaceAll("^整$", "零元整");
//	}
//
//}