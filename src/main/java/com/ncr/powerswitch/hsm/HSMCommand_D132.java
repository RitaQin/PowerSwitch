package com.ncr.powerswitch.hsm;

import java.util.Map;

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
		commandBuffer.append(inputMap.get("MAK").toString().trim().length());
		
		//MAK
		commandBuffer.append(inputMap.get("MAK").toString().trim());
		
		//初始向量
		commandBuffer.append("0000000000000000"); //默认为16个0
		
		//数据长度
		commandBuffer.append(inputMap.get("data").toString().trim().length());
		
		//数据
		commandBuffer.append(inputMap.get("data").toString().trim());

		return commandBuffer.toString(); 
	}
}
