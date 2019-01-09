package com.ncr.powerswitch.esb;

import java.util.Map;

public interface ESBClientIntf {
	
	/**
	 * ���ɶ�Ӧ�ӿڱ���
	 * 
	 * @param ������Ϣmap
	 * @return xml����
	 */
	String constructRequest(Map<String, Object> requestMap);
	
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
