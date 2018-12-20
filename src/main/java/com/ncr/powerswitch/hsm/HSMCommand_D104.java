package com.ncr.powerswitch.hsm;

import java.util.Map;

import com.ncr.powerswitch.utils.StringUtil;

/**
 * 			命令		2	H	0xD104
			工作密钥长度
			主密钥长度 
			密钥索引
			密钥长度
			密钥类型
			
			应答码	1	A	“A”
			MAC	8	H
			或
			应答码	1	A	“E”
			错误码	1	H	0x01：无本地主密钥
			0x2F：算法标志错
			0x68：数据长度错
			
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
		//命令
		commandBuffer.append("D104");
		//工作密钥长度
		commandBuffer.append("10");
		//主密钥长度
		commandBuffer.append("10");
		//密钥索引
		commandBuffer.append(StringUtil.getHexString(inputMap.get("keyIndex").trim()));
		//密钥类型
		String keyType = keyTypeChang(inputMap.get("keyType"));
		commandBuffer.append(keyType); 
		//工作密钥
		commandBuffer.append(inputMap.get("workKey").trim());
		
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
