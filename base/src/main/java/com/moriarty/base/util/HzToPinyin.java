package com.moriarty.base.util;
//
//import net.sourceforge.pinyin4j.PinyinHelper;
//import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
//import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
//import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
//import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
//import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
public class HzToPinyin {
//	/**
//	 *
//	 * [汉字转拼音]
//	 *
//	 * @param src
//	 *            欲转换的汉字
//	 * @return [汉字的拼音的首字母]
//	 * @createTime 2012-1-4 下午03:56:31
//	 */
//	public static String getPingYin(String src) {
//
//		char[] t1 = null;
//		t1 = src.toCharArray();
//		String[] t2 = new String[t1.length];
//		HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
//		t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
//		t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
//		t3.setVCharType(HanyuPinyinVCharType.WITH_V);
//		String t4 = "";
//		int t0 = t1.length;
//		try {
//			for (int i = 0; i < t0; i++) {
//				// 判断是否为汉字字符
//				if (Character.toString(t1[i]).matches(
//						"[\\u4E00-\\u9FA5]+")) {
//					t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
//					t4 += t2[0];
//				} else
//					t4 += Character.toString(t1[i]);
//			}
//			System.out.println(t4);
//			return t4;
//		} catch (BadHanyuPinyinOutputFormatCombination e1) {
//			e1.printStackTrace();
//		}
//		return t4;
//	}
//
//	// 返回中文的首字母
//	public static String getPinYinHeadChar(String str) {
//
//		for (String key : specialCityMap.keySet()) {
//			if (str.contains(key))
//				return specialCityMap.get(key);
//		}
//
//		String convert = "";
//		for (int j = 0; j < str.length(); j++) {
//			char word = str.charAt(j);
//			String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
//			if (pinyinArray != null) {
//				convert += pinyinArray[0].charAt(0);
//			} else {
//				convert += word;
//			}
//		}
//		return convert;
//	}
//
//	// 将字符串转移为ASCII码
//	public static String getCnASCII(String cnStr) {
//		StringBuffer strBuf = new StringBuffer();
//		byte[] bGBK = cnStr.getBytes();
//		for (int i = 0; i < bGBK.length; i++) {
//			// System.out.println(Integer.toHexString(bGBK[i]&0xff));
//			strBuf.append(Integer.toHexString(bGBK[i] & 0xff));
//		}
//		return strBuf.toString();
//	}
//
//	public static boolean isChinese(String str) {
//		String regex = "[\\u4e00-\\u9fa5]";
//		Pattern pattern = Pattern.compile(regex);
//		Matcher matcher = pattern.matcher(str);
//		return matcher.find();
//	}
//
//	private static Map<String, String> specialCityMap = new HashMap<String, String>() {
//		{
//			put("重庆", "CQ");
//			put("长沙", "CS");
//		}
//	};

}