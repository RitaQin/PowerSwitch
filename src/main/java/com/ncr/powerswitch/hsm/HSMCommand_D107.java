package com.ncr.powerswitch.hsm;

import java.util.Map;


/**
 * 			����		2	H	0xD107
			 ��Կ����	1	H	08/16/24
			 ��Կ����	1	H
			 ��������Կ����	2	H
			 ��������Կ����	1	H
			 ��������Կ	8/16/24	H	ZMK

			 Ӧ����	1	A	��A��
			 ��Կ����	1	H
			 ��Կ	N	H	LMK���ܵ���ԿN=8/16/24
			 У����	8	H	��Կ��У����
			  ��
			 Ӧ����	1	A	��E��
			 ������	1	H	0x 01���ޱ�������Կ
			 0x10�����ȱ�־��
 * @author rq185015
 *
 */

public class HSMCommand_D107 extends HSMCommand {

	private Map<String, String> inputMap;

	public HSMCommand_D107(Map<String, String> inputMap) {
		this.inputMap = inputMap;
	}

	@Override
	public String packageInputField() throws Exception {
		commandBuffer.append("D107");
		commandBuffer.append("10");
		String keyType = keyTypeChang(inputMap.get("keyType"));
		if (keyType.equals("FF")) {
			//���ʹ���
			return "FF"; 
		} 
		commandBuffer.append(keyType);
		commandBuffer.append("FFFF");
		commandBuffer.append("10");
		commandBuffer.append(inputMap.get("masterKey").trim());
		
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
