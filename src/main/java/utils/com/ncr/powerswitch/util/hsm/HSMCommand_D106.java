package com.ncr.powerswitch.util.hsm;


/***
 * 发送命令:	产生随机工作密钥<0XD106> 
 * 命令类型：	D106 
 * 密钥长度：	16 
 * 密钥类型：	01
 * 
 * @author rq185015
 *
 */

public class HSMCommand_D106 extends HSMCommand {
	
	public HSMCommand_D106() {
	
	}

	@Override
	public String packageInputField() throws Exception {
		commandBuffer.append("D106");//命令
		commandBuffer.append("10");//密钥长度
		commandBuffer.append("01");//密钥类型
		
		return commandBuffer.toString();
	}

}
