package com.ncr.powerswitch.util.hsm;

import java.io.OutputStream;

import com.ncr.powerswitch.utils.StringUtil;

/***
 * 0XB3090 通用3DES加密
 * 用输入的128位明文密钥对输入数据做3DES加密，得到密文数据CDATA
 * 返回CDATA
 * 
 * 命令类型	1	H	0xB0
 * 命令	    1	H	0x90
 * 用户保留字	8	H	
 * 数据密钥	16	H	
 * 数据	    8	H	
 * 
 * @author rq185015
 *
 */

public class B090 extends HSMCommand {
	
	public String dataKey; //数据密钥，从数据库获得
	public String data; //数据
	
	private static String USER_RESERVED = "00000000"; //用户保留字暂用8位"0"
	
	public B090(String dataKey, String data) {
		this.dataKey = dataKey; 
		this.data = data;
	}

	@Override
	//封装消息报文
	public String packageInputField(OutputStream os) throws Exception {
		StringBuffer commandBuffer = new StringBuffer(); 
		commandBuffer.append("B090"); //命令
		if(vendorKeyMap.get("R1") != null && vendorKeyMap.get("R1").toString().length()==16) {
			commandBuffer.append(vendorKeyMap.get("R1").toString()); //用户保留字
		} else {
			commandBuffer.append(USER_RESERVED); // 用户保留字  
		}
		commandBuffer.append(dataKey); //数据密钥
		commandBuffer.append(data); //数据
		String command = commandBuffer.toString();
		
		return command;
	}
	
}
