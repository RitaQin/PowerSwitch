package com.ncr.powerswitch.utils;

import java.nio.charset.StandardCharsets;

public class GeneralUtil {
	
	public static String generatePayloadLength(String payload) {

		byte[] payloadBytes = payload.getBytes(StandardCharsets.UTF_8);
		int requestLen = payloadBytes.length;
		String length = String.format("%08d", requestLen);
		return length; 
	}
	
}
