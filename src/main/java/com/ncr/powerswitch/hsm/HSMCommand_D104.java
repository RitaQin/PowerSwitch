package com.ncr.powerswitch.hsm;

import java.util.Map;

import com.ncr.powerswitch.utils.StringUtil;

/**
 * 			����		2	H	0xD104
			������Կ����
			����Կ���� 
			��Կ����
			��Կ����
			��Կ����
			
			Ӧ����	1	A	��A��
			MAC	8	H
			��
			Ӧ����	1	A	��E��
			������	1	H	0x01���ޱ�������Կ
			0x2F���㷨��־��
			0x68�����ݳ��ȴ�
			
 * @author rq185015
 *
 */

public class HSMCommand_D104 extends HSMCommand {
	
	private Map<String, String> inputMap; 
	public HSMCommand_D104(Map<String, String> inputMap) {
		this.inputMap = inputMap;
	}

	@Override
	public String packageInputField() throws Exception {
		//����
		commandBuffer.append("D104");
		//������Կ����
		commandBuffer.append("10");
		//����Կ����
		commandBuffer.append("10");
		//��Կ����
		commandBuffer.append(StringUtil.getHexString(inputMap.get("keyIndex").trim()));
		//��Կ����
		String keyType = keyTypeChang(inputMap.get("keyType"));
		commandBuffer.append(keyType); 
		//������Կ
		commandBuffer.append(inputMap.get("workKey").trim());
		
		return commandBuffer.toString();
	}
	
	/**
	 * keyTypeChang
	 * ���ܣ�ת����Կ����
	 * 
	 * ���������
	 * iKeyType    String     ��Կ��������
	 * 
	 * ���������
	 * return      String     ��Կ������ֵ
	 */
	private String keyTypeChang(String keyType) {
		switch (keyType) {
		case "HSM_KEYTYPE_TSF":
			return "01";
		case "HSM_KEYTYPE_PIN":
			return "11";
		case "HSM_KEYTYPE_MAC":
			return "12";
		case "HSM_KEYTYPE_TRACE":
			return "13";
		default:
			return "FF";
		}
	}

}
