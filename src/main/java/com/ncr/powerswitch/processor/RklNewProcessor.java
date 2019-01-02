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
import com.ncr.powerswitch.utils.StringUtil;

public class RklNewProcessor  {
	
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
		String c047msg = c047.packageInputField();
		System.out.println("c047 message: " + c047msg + " Starting command C047");
		byte[] bytelst = StringUtil.ASCII_To_BCD(c047msg.getBytes(), c047msg.length());		
		exchange.getOut().setBody(bytelst);
	}	
	
	public void responseHsmC047Process(Exchange exchange, String hsmIp, String hsmPort) throws Exception{
		byte[] bytes = exchange.getIn().getBody(byte[].class);;
		String c047Res = StringUtil.bcd2Str(bytes, bytes.length);		
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
		
		System.out.println("Starting command D106");
		HSMCommand_D106 d106 = new HSMCommand_D106();
		String d106msg = d106.packageInputField();
		System.out.println("D106 message: " + d106msg + " Starting command D106");
		byte[] bytelst = StringUtil.ASCII_To_BCD(d106msg.getBytes(), d106msg.length());		
		exchange.getOut().setBody(bytelst);		
	}	
	
	
	public void responseHsmD106Process(Exchange exchange, String hsmIp, String hsmPort) throws Exception{
		byte[] bytes = exchange.getIn().getBody(byte[].class);;
		String d106Res = StringUtil.bcd2Str(bytes, bytes.length);		
		System.out.println("verifying D106 Return " + d106Res);
		
		TerminalKey terminalKey = new TerminalKey();			
		terminalKey.setTerminalId(exchange.getProperty("terminalId",String.class));
		
		String masterKey = null;
		String checkCode = null;
		if (d106Res != null) {
			if (d106Res.length() == 52 && d106Res.substring(0, 2).equals("41")) {
				masterKey = d106Res.substring(4, 36);
				System.out.println("Master Key is " + masterKey);
				terminalKey.setMasterKey(masterKey);
				checkCode = d106Res.substring(36, 52);
				terminalKey.setMasterKeyCheck(checkCode);
			} else {
				exchange.getOut().setBody("D106格式未通过验证  " + d106Res);
			}
		} else {
			exchange.getOut().setBody("D106 未返回随机密钥");
		}
		
		exchange.getProperties().put("TerminalKey", terminalKey);	
	}	
	
	public void insertTerminalKeyProcess(Exchange exchange) throws Exception{	
		TerminalKey terminalKey = (TerminalKey)exchange.getProperties().get("TerminalKey");		
		exchange.getOut().setBody(terminalKey);
	}
	
	public void requestHsmC049Process(Exchange exchange, String hsmIp, String hsmPort) throws Exception{
		Map<String, String> inputMap = new HashMap<String, String>();		
		
		TerminalKey terminalKey = (TerminalKey)exchange.getProperties().get("TerminalKey");			
		
		System.out.println("Starting Command C049");
		inputMap.put("KEY_TEXT", terminalKey.getMasterKey());
		inputMap.put("userReservedStr", "0000000000000000");
		inputMap.put("strEppPublicKey", exchange.getProperty("eppPublicKey", String.class));
		HSMCommand_C049 c049 = new HSMCommand_C049(inputMap);
		String c049Msg = c049.packageInputField();
		
		System.out.println("C049 message: " + c049Msg + " Starting command C049");
		byte[] bytelst = StringUtil.ASCII_To_BCD(c049Msg.getBytes(), c049Msg.length());		
		exchange.getOut().setBody(bytelst);		
	}	
	
	public void responseHsmC049Process(Exchange exchange, String hsmIp, String hsmPort) throws Exception{
		byte[] bytes = exchange.getIn().getBody(byte[].class);;
		String c049Res = StringUtil.bcd2Str(bytes, bytes.length);		
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
		inputMap.put("userReservedStr", "0000000000000000");
		inputMap.put("encryption", exchange.getProperty("encryptedMk", String.class));
		
		HSMCommand_C046 c046 = new HSMCommand_C046(inputMap);
		String c046Msg = c046.packageInputField();
		
		System.out.println("C046 message: " + c046Msg + " Starting command C046");
		byte[] bytelst = StringUtil.ASCII_To_BCD(c046Msg.getBytes(), c046Msg.length());		
		exchange.getOut().setBody(bytelst);		
		
	}
	
	public void responseHsmC046Process(Exchange exchange, String hsmIp, String hsmPort) throws Exception{
		byte[] bytes = exchange.getIn().getBody(byte[].class);;
		String c046Res = StringUtil.bcd2Str(bytes, bytes.length);		
		System.out.println("C046 Return " + c046Res);
					
		String encryptSign = null;		
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
