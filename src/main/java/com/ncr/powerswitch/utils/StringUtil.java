package com.ncr.powerswitch.utils;

/***
 * 
 * @author rq185015
 *
 */

public class StringUtil {

	/**
	 * 是否为空
	 * 
	 * @param str
	 *            字符串的值
	 * @return false:不为空 true:为空
	 */
	public static boolean isNull(String str) {
		if (str == null || str.equals("")) {
			return true;
		}
		return false;
	}

	///////////////////////////////////////////////////////////////////////
	// 转码工具类
	///////////////////////////////////////////////////////////////////////
	public static byte asc_to_bcd(byte asc) {
		byte bcd;

		if ((asc >= '0') && (asc <= '9'))
			bcd = (byte) (asc - '0');
		else if ((asc >= 'A') && (asc <= 'F'))
			bcd = (byte) (asc - 'A' + 10);
		else if ((asc >= 'a') && (asc <= 'f'))
			bcd = (byte) (asc - 'a' + 10);
		else
			bcd = (byte) (asc - 48);
		return bcd;
	}

	public static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {
		byte[] bcd = new byte[asc_len / 2];
		System.out.println("asc_len" + asc_len);
		System.out.println("asc_len / 2" + asc_len / 2);
		int j = 0;
		for (int i = 0; i < (asc_len + 1) / 2; i++) {
			bcd[i] = asc_to_bcd(ascii[j++]);
			bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));
		}
		return bcd;
	}
	
	public static String bcd2Str(byte[] bytes, int n) {
		char temp[] = new char[n * 2], val;

		for (int i = 0; i < n; i++) {
			val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
			temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

			val = (char) (bytes[i] & 0x0f);
			temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
		}
		return new String(temp);
	}

	/**
	 * 替换字符串
	 * 
	 * @param strSource
	 *            字符串
	 * @param strFrom
	 *            要替换的字符串（已存在）
	 * @param strTo
	 *            (不存在)
	 * @return
	 */
	public static String replace(String strSource, String strFrom, String strTo) {
		if (strSource == null) {
			return null;
		}
		int i = 0;
		if ((i = strSource.indexOf(strFrom, i)) >= 0) {
			char[] cSrc = strSource.toCharArray();
			char[] cTo = strTo.toCharArray();
			int len = strFrom.length();
			StringBuffer buf = new StringBuffer(cSrc.length);
			buf.append(cSrc, 0, i).append(cTo);
			i += len;
			int j = i;
			while ((i = strSource.indexOf(strFrom, i)) > 0) {
				buf.append(cSrc, j, i - j).append(cTo);
				i += len;
				j = i;
			}
			buf.append(cSrc, j, cSrc.length - j);
			return buf.toString();
		}
		return strSource;
	}

	/**
	 * 10进制字符串型数字转换16进制字符串
	 */
	public static final String tentoSixteen(String strten, int number) {
		char digits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8',

				'9', 'A', 'B', 'C', 'D', 'E', 'F' };
		String str = "";
		int Num = Integer.parseInt(strten);// 要转换的数字
		Num = Num / 2;
		int length = 32;

		char[] result = new char[length];

		do {

			result[--length] = digits[Num & 15];

			Num >>>= 4;

		} while (Num != 0);

		for (int i = length; i < result.length; i++) {

			str += result[i];
		}
		if (str.length() == number) {
			return str;
		} else if (str.length() > number) {
			return null;
		} else {
			while (true) {
				if (str.length() < number) {
					str = "0" + str;

				} else if (str.length() == number) {
					break;
				}
			}
			return str;
		}

	}

}
