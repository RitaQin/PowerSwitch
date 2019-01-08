package com.ncr.powerswitch.hsm;

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
