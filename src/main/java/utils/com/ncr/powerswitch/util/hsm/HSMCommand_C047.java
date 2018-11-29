package com.ncr.powerswitch.util.hsm;


import java.io.OutputStream;
import java.util.Map;

import com.ncr.powerswitch.utils.StringUtil;

/***
 * 
 * 发送命名<0XC047> 
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
	
	public static String DEFAULT_USER_RESERVED_STR = "0000000000000000"; //默认用户保留字

	@Override
	//封装命令消息报文
	public String packageInputField(OutputStream os, Map<String, String> inputMap) throws Exception {
		//读取用户保留字
		String userReservedStr = inputMap.get("R1") != null ? inputMap.get("R1").toString() : DEFAULT_USER_RESERVED_STR; 
		//加密机命令
		commandBuffer.append("C047"); 
		//用户保留字
		commandBuffer.append(userReservedStr);
		//索引
		commandBuffer.append("FFFF");
		//公钥编码方式
		commandBuffer.append("01");
		//厂商公钥长度
		commandBuffer.append(StringUtil.tentoSixteen(inputMap.get("MANUPKLENGTH").toString(),4));
		//公钥长度
		commandBuffer.append(inputMap.get("MANUPK").toString().trim());
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
