package com.ncr.powerswitch.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/***
 * 格式转化辅助类
 * @author rq185015
 *
 */

public class FormatUtil {
	
	//Json字符转Map
	public static Map<String, Object> json2Map(String json) {
		Map<String, Object> resultMap = new Gson()
				.fromJson(json, new TypeToken<HashMap<String, Object>>() {
		}.getType());
		
		return resultMap;
	}
	
	//Map转json字符
	public static String map2Json(Map<String, Object> jsonMap) {
		String json = new Gson().toJson(jsonMap);	
		return json; 
	}
	
	public static String stream2Str(InputStream bodyStream) throws IOException {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] contextBytes = new byte[4096];
		int realLen;
		while ((realLen = bodyStream.read(contextBytes, 0, 4096)) != -1) {
			outStream.write(contextBytes, 0, realLen);
		}
		try {
			return new String(outStream.toByteArray(), "UTF-8");
		} finally {
			outStream.close();
		}
	}
	
	public static String byteArray2Str(byte[] b) {
		return new String(b);
	}
	
}
