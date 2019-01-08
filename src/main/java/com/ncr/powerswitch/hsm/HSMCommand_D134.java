package com.ncr.powerswitch.hsm;

import java.util.Map;

import com.ncr.powerswitch.utils.StringUtil;

/**
 * 功能：校验mac值
 * 
 * 输入参数：
 * MacKeyLen      String     mac key长度
 * MacKey         String     mac key
 * Mac            String     由mac key计算出来的mac值
 * MacData        String     用于计算mac值的数据
 * 
 * 输出参数：
 * outs             String     加密机返回数据
 */

public class HSMCommand_D134 extends HSMCommand {

	private Map<String, String> inputMap;

	public HSMCommand_D134(Map<String, String> inputMap) {
		this.inputMap = inputMap;
	}

	@Override
	public String packageInputField() throws Exception {
		//命令
		commandBuffer.append("D134");	
		
		//算法标志，默认为ANSI9.19
		commandBuffer.append("03");
		
		//MAK长度
		String macKeyLen = keyLenChang(inputMap.get("macKeyLen"));
		if( !"FF".equals(macKeyLen)){
			commandBuffer.append(macKeyLen);
		}else{
			//logger.info("获取密钥长度失败.");
			return "FF";
		}
		
		//MAK
		commandBuffer.append(inputMap.get("macKey").toString().trim());
		
		//初始向量
		commandBuffer.append("0000000000000000"); //默认为16个0
		
		//mac值
		commandBuffer.append(inputMap.get("mac").toString().trim());
		
		//数据长度		
		String datalen = StringUtil.tentoSixteen(inputMap.get("macData").trim().length()+"", 4);
		/**
		 * 当上传数据长度大于 Ox1000 时，绕过产生mac流程直接返回16个0
		 */
		if( !"0".equals(datalen.substring(0, 1)) ){
			return "410000000000000000";
		}else{
			commandBuffer.append(datalen);
		}
		
		
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
