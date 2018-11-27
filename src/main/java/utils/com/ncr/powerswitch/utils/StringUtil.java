package com.ncr.powerswitch.utils;

/***
 * 
 * @author rq185015
 *
 */

public class StringUtil {

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
        int j = 0;  
        for (int i = 0; i < (asc_len + 1) / 2; i++) {  
            bcd[i] = asc_to_bcd(ascii[j++]);  
            bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));  
        }  
        return bcd;  
    }  
	
	 public static String bcd2Str(byte[] bytes,int n) {  
	        char temp[] = new char[n * 2], val;  
	  
	        for (int i = 0; i < n; i++) {  
	            val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);  
	            temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');  
	  
	            val = (char) (bytes[i] & 0x0f);  
	            temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');  
	        }  
	        return new String(temp);  
	    }

}
