package com.ncr.powerswitch.utils;

public class GeneralUtil {
	
	public static String generatePayloadLength(String payload) {
		//计算报文长度  并返回8字节长度字符串 不够8位填充0
		byte[] payloadBytes = payload.getBytes();
		int requestLen = payloadBytes.length;
		String length = String.format("%08d", requestLen);
		return length; 
	}
	
	public static String generateTcpRequestHeader() {
		//TODO: 生成tcp请求报文头字符串
		
		return null;
	}
}
