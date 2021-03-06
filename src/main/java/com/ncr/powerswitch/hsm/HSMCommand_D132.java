package com.ncr.powerswitch.hsm;

import java.util.Map;

import com.ncr.powerswitch.utils.StringUtil;

/**
 * 产生MAC命令
 * 
	命令		2	H	0xD132
	算法标志	1	H	01：XOR  02：ANSI9.9   03：ANSI9.19
	MAK长度	1	H	8/16/24
	MAK	N	H	N = 8/16/24 LMK加密的MAK
	初始向量	8	H	
	数据长度	2	H	
	数据	N	H	
 * 
 * @author rq185015
 *
 */

public class HSMCommand_D132 extends HSMCommand {

	private Map<String, String> inputMap;

	public HSMCommand_D132(Map<String, String> inputMap) {
		this.inputMap = inputMap;
	}

	@Override
	public String packageInputField() throws Exception {
		//命令
		commandBuffer.append("D132");	
		
		//算法标志，默认为ANSI9.19
		commandBuffer.append("03");
		
		//MAK长度
		String macKeyLen = keyLenChang(inputMap.get("macKeyLen"));		
		if( !"FF".equals(macKeyLen)){
			commandBuffer.append(macKeyLen);
		}else{
			return "FF";
		}
		
		//MAK
		commandBuffer.append(inputMap.get("macKey").toString().trim());
		
		//初始向量
		commandBuffer.append("0000000000000000"); //默认为16个0
		
		//数据长度		
		String datalen = StringUtil.tentoSixteen(inputMap.get("macData").trim().length()+"", 4);
		/**
		 * 当上传数据长度大于 Ox1000 时，绕过产生mac流程直接返回16个0
		 */
		if( !"0".equals(datalen.substring(0, 1)) ){
			return "410000000000000000";
		}		
		commandBuffer.append(datalen);
		
		//数据
		commandBuffer.append(inputMap.get("macData").toString().trim());

		return commandBuffer.toString(); 
	}
	
	private String keyLenChang(String iKeyLen)
	{
		/**
		 *0x08：单长度(64bits)密钥
		 *0x10：双长度(128bits)密钥
		 *0x18：三倍长度(192bits)密钥
		 */
		if("HSM_KEYLEN_1".equals(iKeyLen))
		{
			return "08";
		}
		else if("HSM_KEYLEN_2".equals(iKeyLen))
		{
			return "10";
		}
		else if("HSM_KEYLEN_3".equals(iKeyLen))
		{
			return "18";
		}
		else
		{
			return "FF";
		}
	}
}
