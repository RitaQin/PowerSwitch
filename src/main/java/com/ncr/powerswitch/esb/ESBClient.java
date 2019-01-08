package com.ncr.powerswitch.esb;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ncr.powerswitch.utils.FormatUtil;


/**
 * ��ESB�����ӿڿͻ��� TCP�����Ӻͱ��ķ���
 * 
 * @author rq185015
 *
 */

public abstract class ESBClient implements ESBClientIntf {

	public final static Log log = LogFactory.getLog(ESBClient.class);
	
	public ESBClient() {
	}

	/**
	 * ���ɶ�Ӧ�ӿڱ���
	 * 
	 * @param ������Ϣmap
	 * @return xml����
	 */

	public abstract String constructRequest(Map<String, String> requestValues);

	/**
	 * ��֤��esb���صı���
	 * 
	 * @param ��esb���͵�����
	 * @param esb�ظ��ı���
	 * @param ��Ҫ��֤����
	 * @return ��֤��� boolean
	 */
	public boolean verifyResMessage(String msg, String receivedMsg, String[] verifyFields) {
		Map<String, String> sendMap = FormatUtil.xml2Map(msg);
		if (sendMap == null || sendMap.size() == 0) {
			return false;
		}
		Map<String, String> receivedMap = FormatUtil.xml2Map(receivedMsg);
		if (receivedMap == null || receivedMap.size() == 0) {
			return false;
		}
		for (int i = 0; i < verifyFields.length; i++) {
			String key = verifyFields[i];
			if (!sendMap.get(key).equals(receivedMap.get(key))) {
				return false;
			}
		}
		return true;
	}

}
