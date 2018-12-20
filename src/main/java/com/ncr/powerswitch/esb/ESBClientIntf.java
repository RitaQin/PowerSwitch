package com.ncr.powerswitch.esb;

import java.util.Map;

public interface ESBClientIntf {
	
	/**
	 * ���ɶ�Ӧ�ӿڱ���
	 * 
	 * @param ������Ϣmap
	 * @return xml����
	 */
	String constructRequest(Map<String, String> requestMap);
	
	/**
	 * ���ͺͽ���ESB�ӿڱ���
	 * 
	 * @param msg �ն˺�
	 * @param ip  ip��ַ
	 * @param port �˿ں�
	 * @param unpack
	 * @return ���ر���
	 */
	 String sendAndReceivePackets(String msg, String ip, String port, boolean unpack);
	
	/**
	 * ��֤��esb���صı���
	 * 
	 * @param ��esb���͵�����
	 * @param esb�ظ��ı���
	 * @param ��Ҫ��֤����
	 * @return ��֤��� boolean
	 */
	 boolean verifyResMessage(String msg, String receivedMsg, String[] verifyFields);
}
