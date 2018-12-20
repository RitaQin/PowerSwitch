package com.ncr.powerswitch.esb;

import java.util.Map;

public interface ESBClientIntf {
	
	/**
	 * 生成对应接口报文
	 * 
	 * @param 请求信息map
	 * @return xml报文
	 */
	String constructRequest(Map<String, String> requestMap);
	
	/**
	 * 发送和接收ESB接口报文
	 * 
	 * @param msg 终端号
	 * @param ip  ip地址
	 * @param port 端口号
	 * @param unpack
	 * @return 返回报文
	 */
	 String sendAndReceivePackets(String msg, String ip, String port, boolean unpack);
	
	/**
	 * 验证由esb返回的报文
	 * 
	 * @param 向esb发送的请求
	 * @param esb回复的报文
	 * @param 需要验证的域
	 * @return 验证结果 boolean
	 */
	 boolean verifyResMessage(String msg, String receivedMsg, String[] verifyFields);
}
