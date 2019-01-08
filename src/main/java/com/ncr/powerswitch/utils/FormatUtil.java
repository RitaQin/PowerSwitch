package com.ncr.powerswitch.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thoughtworks.xstream.XStream;

/***
 * 格式转化辅助类
 * 
 * @author rq185015
 *
 */

public class FormatUtil {

	// Json字符转Map
	public static Map<String, Object> json2Map(String json) {
		Map<String, Object> resultMap = new Gson().fromJson(json, new TypeToken<HashMap<String, Object>>() {
		}.getType());

		return resultMap;
	}

	// map转json字符
	public static String map2Json(Map<String, Object> jsonMap) {
		String json = new Gson().toJson(jsonMap);
		return json;
	}

	// 将map转成xml字符串
	public static String map2Xml(Map<String, String> xmlMap) {
		XStream xstream = new XStream();
		xstream.alias("map", java.util.Map.class);
		String xml = xstream.toXML(xmlMap);
		return xml;
	}

	// 将xml字符串转成map
	public static Map<String, String> xml2Map(String xml) {
		XStream xstream = new XStream();
		xstream.alias("map", java.util.Map.class);
		@SuppressWarnings("unchecked")
		Map<String, String> xmlMap = (Map<String, String>) xstream.fromXML(xml);
		return xmlMap;
	}

	// stream转字符串
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

	// byte转字符串
	public static String byteArray2Str(byte[] b) {
		return new String(b);
	}

	public static int bytes2int(byte[] bytes) {
		
		int integer = Integer.parseInt(byteArray2Str(bytes)); 
		
		return integer;
	}

	public static byte[] int2bytes(int num) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			b[i] = (byte) (num >>> (24 - i * 8));
		}
		return b;
	}
}
