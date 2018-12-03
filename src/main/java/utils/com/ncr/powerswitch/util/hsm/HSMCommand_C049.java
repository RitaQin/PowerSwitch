package com.ncr.powerswitch.util.hsm;

import java.util.Map;

import com.ncr.powerswitch.utils.StringUtil;

/**
 * 发送命名<0XC049> 转加密KEY 
 * 命令： 0XC049 
 * 用户保留字： 数据库读取，暂默认为16 个 0 
 * 密钥类型： 0x01： 通信主密钥
 * KEY加密标志： 0x00： 明文 
 * 主密钥索引： ZMK索引 
 * KEY长度： 32 
 * KEY： ATM密钥 
 * 填充方式: 1--RSA_PKCS1_PADDING 3--RSA_NO_PADDING 
 * 公钥编码方式： 0：REF格式 1：DER格式
 * 公钥长度： EPP公钥长度
 * 公钥： EPP公钥
 * 
 * @author rq185015
 *
 */

public class HSMCommand_C049 extends HSMCommand {

	private Map<String, String> inputMap;

	public HSMCommand_C049(Map<String, String> inputMap) {
		this.inputMap = inputMap;
	}

	@Override
	public String packageInputField() throws Exception {
		//命令
		commandBuffer.append("C049");
		//用户保留字
		commandBuffer.append(inputMap.get("userReservedStr")); 
		//密钥类型
		commandBuffer.append("01");
		//KEY加密标志
		commandBuffer.append("02");
		//KEY长度 数据待定
		commandBuffer.append("10");
		//KEY
		commandBuffer.append(inputMap.get("KEY_TEXT").trim());
		//公钥索引
		commandBuffer.append("FFFF");
		//填充方式
		commandBuffer.append("01");
		//公钥编码方式
		commandBuffer.append("01");
		//EPP公钥长度
		commandBuffer.append(StringUtil.tentoSixteen(inputMap.get("strEppPublicKey").length() + "", 4));
		//EPP公钥
		commandBuffer.append(inputMap.get("strEppPublicKey"));/// EPP公钥

		return commandBuffer.toString();
	}

}
