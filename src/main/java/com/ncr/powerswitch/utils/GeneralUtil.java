package com.ncr.powerswitch.utils;

public class GeneralUtil {
	
	public static String generatePayloadLength(String payload) {
		//���㱨�ĳ���  ������8�ֽڳ����ַ��� ����8λ���0
		byte[] payloadBytes = payload.getBytes();
		int requestLen = payloadBytes.length;
		String length = String.format("%08d", requestLen);
		return length; 
	}
	
	public static String generateTcpRequestHeader() {
		//TODO: ����tcp������ͷ�ַ���
		
		return null;
	}
}
