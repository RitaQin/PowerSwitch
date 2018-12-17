package com.ncr.powerswitch.processor;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.ncr.powerswitch.hsm.HSMCommand_C046;
import com.ncr.powerswitch.hsm.HSMCommand_C047;
import com.ncr.powerswitch.hsm.HSMCommand_C049;
import com.ncr.powerswitch.hsm.HSMCommand_D106;
import com.ncr.powerswitch.hsm.HSMSocketClient;
import com.ncr.powerswitch.utils.TestUtil;

public class DataKeyProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {

		Map<String, String> inputMap = TestUtil.loadKeyData();
		System.out.println("Starting command C047");
		HSMCommand_C047 c047 = new HSMCommand_C047(inputMap);
		String c047_msg = c047.packageInputField();
		String c047Res = HSMSocketClient.sendAndReceivePacket(c047_msg, "8.99.9.91", "3000", false);
		System.out.println("verifying C047 Return " + c047Res);
		if (c047Res != null) {
			if (c047Res.length() == 20 && c047Res.substring(0, 2).equals("41")
					&& c047Res.substring(18, 20).equals("00")) {
				System.out.println("C047  驗證通過" + c047Res);
			} else {
				exchange.getOut().setBody("C047 未通过验证  " + c047Res);
			}
		} else {
			exchange.getOut().setBody("C047 未返回随机密钥");
		}

		System.out.println("Starting command D106");
		HSMCommand_D106 d106 = new HSMCommand_D106();
		String d106_msg = d106.packageInputField();
		String returnMsg = HSMSocketClient.sendAndReceivePacket(d106_msg, "8.99.9.91", "3000", false);
		System.out.println("verifying D106 Return " + returnMsg);
		String masterKey = null;
		if (returnMsg != null) {
			if (returnMsg.length() == 52 && returnMsg.substring(0, 2).equals("41")) {
				masterKey = returnMsg.substring(4, 36);
				System.out.println("Master Key is " + masterKey);

			} else {
				exchange.getOut().setBody("D106格式未通过验证  " + returnMsg);
			}
		} else {
			exchange.getOut().setBody("D106 未返回随机密钥");
		}

		System.out.println("Starting Command C049");
		inputMap.put("KEY_TEXT", masterKey);
		inputMap.put("userReservedStr", "0000000000000000");
		HSMCommand_C049 c049 = new HSMCommand_C049(inputMap);
		String c049Msg = c049.packageInputField();
		String c049Res = HSMSocketClient.sendAndReceivePacket(c049Msg, "8.99.9.91", "3000", false);
		System.out.println("verifying C049 Return " + c049Res);
		String encryptedMk = "FFFFFFFF";
		if (c049Res != null) {
			if (c049Res.substring(0, 2).equals("41")) {
				encryptedMk = c049Res.substring(22, c049Res.length());
				System.out.println("C049 轉加密文： " + encryptedMk);
			} else {
				System.out.println("C049 not verified  " + c049Res);
			}
		} else {
			System.out.println("C049 returns null");
		}

		System.out.println("Starting Command C046");
		String sk = TestUtil.BANK_PRIVATE_KEY;
		int sklen = sk.length();
		inputMap.put("SKLENGTH", Integer.toString(sklen));
		inputMap.put("SK", sk);
		inputMap.put("encryption", encryptedMk);
		HSMCommand_C046 c046 = new HSMCommand_C046(inputMap);
		String c046Msg = c046.packageInputField();
		String c046Res = HSMSocketClient.sendAndReceivePacket(c046Msg, "8.99.9.91", "3000", false);
		System.out.println("verifying C046 Return " + c046Res);
		if (c046Res != null && c046Res.substring(0, 2).equals("41")) {
			exchange.getOut().setBody("C046 密文簽名： " + c046Res.substring(22, c046Res.length()));
		} else {
			exchange.getOut().setBody("C046未通過");
		}
	}
}
