package com.ncr.powerswitch.hsm;

import java.util.Map;

import com.ncr.powerswitch.utils.StringUtil;

/***
 * 
 * 验证签名 命令：0xC047 
 * 用户保留字：数据库读取，暂默认为16 个 0
 * 区域主密钥索引号 ：数据库读取，或默认16位"0"
 * 公钥编码方式：1：DER格式 
 * 公钥长度：数据库读取 
 * 公钥：数据库读取 
 * Hash方式： 0：SHA1 
 * 数据长度：Epp公钥长度 
 * 数据：Epp公钥
 * 签名长度：Epp公钥签名长度 
 * 签名 ：Epp公钥签名
 * 
 * @author rq185015
 *
 */

public class HSMCommand_C047 extends HSMCommand {
	
	private Map<String, String> inputMap; 
	
	public HSMCommand_C047(Map<String, String> inputMap) {
		this.inputMap = inputMap;
	}
	
	@Override
	//封装命令消息报文
	public String packageInputField() throws Exception {
		//加密机命令
		commandBuffer.append("C047"); 
		//用户保留字
		commandBuffer.append(inputMap.get("userReservedStr"));
		//索引
		commandBuffer.append("FFFF");
		//公钥编码方式
		commandBuffer.append("01");
		//厂商公钥长度
		commandBuffer.append(StringUtil.tentoSixteen(inputMap.get("MANUPKLENGTH").toString(), 4));
		//公钥长度
		commandBuffer.append(inputMap.get("MANUPK").toString());
		//Hash 方式 0：SHA1   1：MD5
		commandBuffer.append("00");
		//数据长度
		commandBuffer.append(StringUtil.tentoSixteen(inputMap.get("strEppPublicKey").trim().length()+"", 4));
		//数据
		commandBuffer.append(inputMap.get("strEppPublicKey"));
		//签名长度 
		commandBuffer.append(StringUtil.tentoSixteen(inputMap.get("strEppPublicKeySign").trim().length()+"",4));
		//签名
		commandBuffer.append(inputMap.get("strEppPublicKeySign"));
		
		return commandBuffer.toString();
	}


}
