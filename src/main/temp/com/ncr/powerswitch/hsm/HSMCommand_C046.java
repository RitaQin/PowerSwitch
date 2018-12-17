package com.ncr.powerswitch.hsm;

import java.util.Map;
import com.ncr.powerswitch.utils.StringUtil;

/**
* 发送命名<0XC46> 密钥签名
* 命令：0XC049
* 用户保留字：数据库读取，暂默认为16 个 0 
* 密钥类型：0x01：通信主密钥
* KEY加密标志：0x00：明文
* 主密钥索引：ZMK索引
* KEY长度：32
* KEY ：ATM密钥
* 填充方式1--RSA_PKCS1_PADDING   3-- RSA_NO_PADDING
* 公钥编码方式：0：REF格式 1：DER格式
* 公钥长度：EPP公钥长度
* 公钥：EPP公钥
 * @author rq185015
 *
 */

public class HSMCommand_C046 extends HSMCommand {
	
	private Map<String, String> inputMap;
	
	public HSMCommand_C046(Map<String, String> inputMap) {
		this.inputMap = inputMap;
	}

	@Override
	public String packageInputField() throws Exception {
		// 命令
		commandBuffer.append("C046");
		// 用户保留字
		commandBuffer.append("userReservedStr");
		// 索引
		commandBuffer.append("FFFF");
		// 索引
		commandBuffer.append("01");
		// 私钥长度
		commandBuffer.append(StringUtil.tentoSixteen(inputMap.get("SKLENGTH").toString().trim(), 4));
		// 私钥
		commandBuffer.append(inputMap.get("SK").toString().trim());
		// 私钥加解密标志
		commandBuffer.append("01");
		// 索引
		commandBuffer.append("FFFF");
		// Hash方式
		commandBuffer.append("00");
		// 加密密文长度
		commandBuffer.append(StringUtil.tentoSixteen(inputMap.get("encryption").toString().trim().length() + "", 4));
		// 加密密文
		commandBuffer.append(inputMap.get("encryption").toString().trim());

		return commandBuffer.toString();
	}
}
