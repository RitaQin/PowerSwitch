package com.ncr.powerswitch.processor;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;

import com.ncr.powerswitch.dataObject.EppKey;
import com.ncr.powerswitch.dataObject.TerminalKey;
import com.ncr.powerswitch.hsm.HSMCommand_C046;
import com.ncr.powerswitch.hsm.HSMCommand_C047;
import com.ncr.powerswitch.hsm.HSMCommand_C049;
import com.ncr.powerswitch.hsm.HSMCommand_D106;
import com.ncr.powerswitch.hsm.HSMSocketClient;
import com.ncr.powerswitch.utils.FormatUtil;
import com.ncr.powerswitch.utils.ResponseCode;

public class RklProcessorNew  {
	
	public void getTerminalIdProcess(Exchange exchange) throws Exception{
		Map<String, Object> requestMap =  exchange.getIn().getBody(Map.class);
		exchange.getProperties().putAll(requestMap);
		exchange.getOut().setBody(exchange.getProperties().get("terminalId"));
	}
	
	public void storeEppKeyProcess(Exchange exchange) throws Exception{
		EppKey eppKey =  exchange.getIn().getBody(EppKey.class);
		exchange.getProperties().put("EppKey", eppKey);
	}
	
	public void requestHsmC047Process(Exchange exchange, String hsmIp, String hsmPort) throws Exception{
		Map<String, String> inputMap = new HashMap<String, String>();
		//EppKey eppKey 
		String vendorKey = exchange.getProperty("EppKey", EppKey.class).getManupk();//eppInfo.getManupk();
		inputMap.put("MANUPK", vendorKey);
		inputMap.put("MANUPKLENGTH", Integer.toString(vendorKey.length()));
		inputMap.put("strEppPublicKey", exchange.getProperty("eppPublicKey").toString());
		inputMap.put("strEppPublicKeySign", exchange.getProperty("eppPublicKeySign").toString());
		System.out.println("Starting command C047");
		
		HSMCommand_C047 c047 = new HSMCommand_C047(inputMap);
		String c047_msg = c047.packageInputField();
		String c047Res = HSMSocketClient.sendAndReceivePacket(c047_msg, hsmIp, hsmPort, false);		
		System.out.println("verifying C047 Return " + c047Res);
		if (c047Res != null) {
			if (c047Res.length() == 20 && c047Res.substring(0, 2).equals("41")
					&& c047Res.substring(18, 20).equals("00")) {
				System.out.println("C047验证通过" + c047Res);
			} else {
				exchange.getOut().setBody("C047 未通过验证 " + c047Res);
			}
		} else {
			exchange.getOut().setBody("C047 未返回随机密钥");
		}
	}	
	
	public void requestHsmD106Process(Exchange exchange, String hsmIp, String hsmPort) throws Exception{
		Map<String, String> inputMap = new HashMap<String, String>();		
		
		TerminalKey terminalKey = new TerminalKey();			
		terminalKey.setTerminalId(exchange.getProperty("terminalId",String.class));
		
		System.out.println("Starting command D106");
		HSMCommand_D106 d106 = new HSMCommand_D106();
		String d106_msg = d106.packageInputField();
		String returnMsg = HSMSocketClient.sendAndReceivePacket(d106_msg, hsmIp, hsmPort, false);
		System.out.println("verifying D106 Return " + returnMsg);
		String masterKey = null;
		String checkCode = null;
		if (returnMsg != null) {
			if (returnMsg.length() == 52 && returnMsg.substring(0, 2).equals("41")) {
				masterKey = returnMsg.substring(4, 36);
				System.out.println("Master Key is " + masterKey);
				terminalKey.setMasterKey(masterKey);
				checkCode = returnMsg.substring(36, 52);
				terminalKey.setMasterKeyCheck(checkCode);
			} else {
				exchange.getOut().setBody("D106格式未通过验证  " + returnMsg);
			}
		} else {
			exchange.getOut().setBody("D106 未返回随机密钥");
		}
		
		exchange.getProperties().put("TerminalKey", terminalKey);	
	}	
	
	public void insertTerminalKeyProcess(Exchange exchange) throws Exception{	
		
//		TerminalKey terminalKey = new TerminalKey();
//		terminalKey.setTerminalId(exchange.getProperty("terminalId",String.class));
//		terminalKey.setMasterKey("12341234123412341234123412341234");
//		terminalKey.setMasterKeyCheck("1234123412341234");		
		TerminalKey terminalKey = (TerminalKey)exchange.getProperties().get("TerminalKey");		
		exchange.getOut().setBody(terminalKey);
	}
	
	public void requestHsmC049Process(Exchange exchange, String hsmIp, String hsmPort) throws Exception{
		Map<String, String> inputMap = new HashMap<String, String>();		
		
		TerminalKey terminalKey = (TerminalKey)exchange.getProperties().get("TerminalKey");			
		
		System.out.println("Starting Command C049");
		inputMap.put("KEY_TEXT", terminalKey.getMasterKey());
		inputMap.put("userReservedStr", "0000000000000000");
		HSMCommand_C049 c049 = new HSMCommand_C049(inputMap);
		String c049Msg = c049.packageInputField();
		String c049Res = HSMSocketClient.sendAndReceivePacket(c049Msg, hsmIp, hsmPort, false);
		System.out.println("verifying C049 Return " + c049Res);
		String encryptedMk = null;
		if (c049Res != null) {
			if (c049Res.substring(0, 2).equals("41")) {
				encryptedMk = c049Res.substring(22, c049Res.length());
				System.out.println("C049转加密文： " + encryptedMk);
			} else {
				System.out.println("C049 not verified  " + c049Res);
				exchange.getOut().setBody("C049 not verified  " + c049Res);
			}
		} else {
			System.out.println("C049 returns null");
			exchange.getOut().setBody("C049 returns null");
		}
		
		exchange.getProperties().put("encryptedMk", encryptedMk);	
	}	
	
	public void requestHsmC046Process(Exchange exchange, String hsmIp, String hsmPort) throws Exception{
		Map<String, String> inputMap = new HashMap<String, String>();		
		
		TerminalKey terminalKey = (TerminalKey)exchange.getProperties().get("TerminalKey");			
		
		System.out.println("Starting Command C046");
		String sk = exchange.getProperty("EppKey", EppKey.class).getSk();
		inputMap.put("SK", sk);
		inputMap.put("encryption", exchange.getProperty("encryptedMk", String.class));
		HSMCommand_C046 c046 = new HSMCommand_C046(inputMap);
		String c046Msg = c046.packageInputField();
		String encryptSign = null;
		
		String c046Res = HSMSocketClient.sendAndReceivePacket(c046Msg, hsmIp, hsmPort, false);
		if (c046Res != null && c046Res.substring(0, 2).equals("41")) {
			System.out.println("C046 密文簽名： " + c046Res.substring(22, c046Res.length()));
			encryptSign =  c046Res.substring(22, c046Res.length());
			
		} else {
			exchange.getOut().setBody("C046未通過");
		}
		
		exchange.getProperties().put("encryptSign", encryptSign);	
	}	
	
	public void responseRklProcess(Exchange exchange) throws Exception{
		Map<String, Object> head = new HashMap<String, Object>(); 
		head.put("channelId", exchange.getProperty("channelId", String.class));
		head.put("transactionCode", exchange.getProperty("transactionCode", String.class));
		head.put("terminalId", exchange.getProperty("terminalId", String.class));
		head.put("traceNumber", exchange.getProperty("traceNumber", String.class));
		head.put("transactionDate", exchange.getProperty("transactionDate", String.class));
		head.put("transactionTime", exchange.getProperty("transactionTime", String.class));
		head.put("responseCode", ResponseCode.RESPONSE_SUCCESS);
		
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("MasterKeyCypher", exchange.getProperty("encryptedMk", String.class));
		body.put("MasterKeyCypherSign",exchange.getProperty("encryptSign", String.class));
		body.put("MasterKeyCheckCode", exchange.getProperty("TerminalKey",TerminalKey.class).getMasterKeyCheck());
		body.put("BankPublicKey", exchange.getProperty("EppKey",EppKey.class).getBankPK());
		body.put("BankPublicKeySign", exchange.getProperty("EppKey",EppKey.class).getBankPkSignature());
		
		Map<String, Object> retJsonMap = new HashMap<String, Object>(); 
		retJsonMap.put("head", head); 
		retJsonMap.put("body", body);
		// 构造JSON并放入上下文
		exchange.getOut().setBody(FormatUtil.map2Json(retJsonMap));	
	}	
}
