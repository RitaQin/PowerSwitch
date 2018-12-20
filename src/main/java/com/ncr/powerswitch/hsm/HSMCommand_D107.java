package com.ncr.powerswitch.hsm;

import java.util.Map;


/**
 * 			命令		2	H	0xD107
			 密钥长度	1	H	08/16/24
			 密钥类型	1	H
			 传输主密钥索引	2	H
			 传输主密钥长度	1	H
			 传输主密钥	8/16/24	H	ZMK

			 应答码	1	A	“A”
			 密钥长度	1	H
			 密钥	N	H	LMK加密的密钥N=8/16/24
			 校验码	8	H	密钥的校验码
			  或
			 应答码	1	A	“E”
			 错误码	1	H	0x 01：无本地主密钥
			 0x10：长度标志错
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
			//类型错误
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
	 * 功能：转换密钥类型
	 * 
	 * 输入参数：
	 * iKeyType    String     密钥类型描述
	 * 
	 * 输出参数：
	 * return      String     密钥类型数值
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
