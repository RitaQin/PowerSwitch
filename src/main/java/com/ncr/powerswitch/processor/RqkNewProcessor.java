package com.ncr.powerswitch.processor;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;

import com.ncr.powerswitch.hsm.HSMCommand_D104;
import com.ncr.powerswitch.hsm.HSMCommand_D107;
import com.ncr.powerswitch.dataObject.TerminalKey;
import com.ncr.powerswitch.utils.FormatUtil;
import com.ncr.powerswitch.utils.ResponseCode;
import com.ncr.powerswitch.utils.StringUtil;
import com.ncr.powerswitch.exception.PowerswitchException;
import com.ncr.powerswitch.utils.PowerSwitchConstant;
/**
 * 
 * MACKEY MACKEYNEW
 * PINKEY PINKEYNEW
 * TRACEKEY TRACEKEYNEW
 *
 */

public class RqkNewProcessor {
	
	public void getTerminalIdProcess(Exchange exchange) throws Exception{		
		Map<String, Object> requestMap =  exchange.getIn().getBody(Map.class);
		exchange.getProperties().putAll(requestMap);
		exchange.getOut().setBody(exchange.getProperties().get("terminalId"));
	}
	
	public void storeMasterkeyProcess(Exchange exchange) throws Exception{
		TerminalKey terminalKey =  exchange.getIn().getBody(TerminalKey.class);
		exchange.getProperties().put("TerminalKey", terminalKey);
	}
	
	public void requestHsmD107Process(Exchange exchange, String keyType) throws Exception{
		Map<String, String> inputMap = new HashMap<String, String>();
		inputMap.put("masterKey", exchange.getProperty("TerminalKey", TerminalKey.class).getMasterKey());
		inputMap.put("keyType", keyType);
		HSMCommand_D107 d107 = new HSMCommand_D107(inputMap);
		String d107msg = d107.packageInputField();
		System.out.println("d107 message: " + d107msg + " Starting command D107" + " ,KeyType:" + keyType);
		byte[] bytelst = StringUtil.ASCII_To_BCD(d107msg.getBytes(), d107msg.length());		
		exchange.getOut().setBody(bytelst);
	}
	
	public void responseHsmD107Process(Exchange exchange, String keyType) throws Exception{		
		byte[] bytes = exchange.getIn().getBody(byte[].class);;
		String d107Res = StringUtil.bcd2Str(bytes, bytes.length);		
		System.out.println("d107 returns: " + d107Res + " ,KeyType:" + keyType);
		String keyByHsm = null; 
		String keyByTsf = null;
		String checkVal = null;
		if (d107Res != null && d107Res.length() > 2 && d107Res.substring(0, 2).equals("41")) {
			int keyLen = Integer.parseInt(d107Res.substring(2, 4), 16); 
			keyByHsm = d107Res.substring(4, 4 + keyLen * 2);
			System.out.println(keyType + " key by hsm " + keyByHsm);
			keyByTsf = d107Res.substring(4 + keyLen * 2, 4 + keyLen * 4);
			System.out.println(keyType + " key by tfs " + keyByTsf);
			checkVal = d107Res.substring(4 + keyLen * 4, 20 + keyLen * 4);
		}
		if (keyByHsm.equals(null) || keyByTsf.equals(null) || checkVal.equals(null)) {			
			System.out.println("D107 failed..");			
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR,"D107 return error, " + d107Res);
		}
		
		Map<String, String> keySet = new HashMap<String, String>();		
		keySet.put("keyByHsm", keyByHsm);
		keySet.put("keyByTsf", keyByTsf);
		keySet.put("checkVal", checkVal);		
		exchange.getProperties().put(keyType,keySet);		
	}
	
	public void requestHsmD104Process(Exchange exchange, String keyType) throws Exception{
		
		@SuppressWarnings("unchecked")
		Map<String, String> keySet = (Map<String, String>)exchange.getProperties().get(keyType);
		Map<String, String> inputMap = new HashMap<String, String>();
		inputMap.put("workKey", keySet.get("keyByHsm"));
		inputMap.put("keyIndex", "0110");
		inputMap.put("keyType", keyType);
		
		HSMCommand_D104 d104 = new HSMCommand_D104(inputMap);
		String d104msg = d104.packageInputField();
		System.out.println("d104 message: " + d104msg + " Starting command D104" + " ,KeyType:" + keyType);
		byte[] bytelst = StringUtil.ASCII_To_BCD(d104msg.getBytes(), d104msg.length());		
		exchange.getOut().setBody(bytelst);		
	}
	
	public void responseHsmD104Process(Exchange exchange, String keyType) throws Exception{
		
		byte[] bytes = exchange.getIn().getBody(byte[].class);;
		String d104Res = StringUtil.bcd2Str(bytes, bytes.length);
		System.out.println("d104 returns: " + d104Res);
		String keyByHost = null;
		if (d104Res != null && d104Res.length() > 2 && d104Res.substring(0, 2).equals("41")) {
			int keyLen = Integer.parseInt(d104Res.substring(2, 4), 16); 
			keyByHost = d104Res.substring(4, 4 + keyLen * 2);
		}
		
		if(keyByHost.equals(null)) {
			System.out.println("D104 failed .... ");
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR,"D104 return error, " + d104Res);
		}
		@SuppressWarnings("unchecked")
		Map<String, String> keySet = (Map<String, String>)exchange.getProperties().get(keyType);	
		keySet.put("keyByHost", keyByHost);
		exchange.getProperties().put(keyType,keySet);		
	}
	
	public void updateTerminalKeyProcess(Exchange exchange) throws Exception{
		
		Map<String, Object> body = new HashMap<String, Object>();
		TerminalKey terminalKey = (TerminalKey)exchange.getProperties().get("TerminalKey");
		
		@SuppressWarnings("unchecked")
		Map<String, String> pinKeySet = (Map<String, String>)exchange.getProperties().get("HSM_KEYTYPE_PIN");
		terminalKey.setPinKey(pinKeySet.get("keyByTsf"));
		terminalKey.setPinkeyHsm(pinKeySet.get("keyByHost"));
		
		@SuppressWarnings("unchecked")
		Map<String, String> traceKeySet = (Map<String, String>)exchange.getProperties().get("HSM_KEYTYPE_TRACE");
		terminalKey.setTraceKey(traceKeySet.get("keyByTsf"));
		terminalKey.setTraceKeyHsm(traceKeySet.get("keyByHost"));
		
		@SuppressWarnings("unchecked")
		Map<String, String> macKeySet = (Map<String, String>)exchange.getProperties().get("HSM_KEYTYPE_MAC");	
		terminalKey.setMacKey(macKeySet.get("keyByTsf"));
		terminalKey.setMacKeyHsm(macKeySet.get("keyByHost"));
		terminalKey.setKeyIndex("0110");	
		
		exchange.getOut().setBody(terminalKey);
	}
	
	public void responseTerminalKeyProcess(Exchange exchange) throws Exception{
		Map<String, Object> head = new HashMap<String, Object>(); 
		head.put("channelId", exchange.getProperties().get("channelId"));
		head.put("transactionCode", exchange.getProperties().get("transactionCode"));
		head.put("terminalId", exchange.getProperties().get("terminalId"));
		head.put("channelId", exchange.getProperties().get("channelId"));
		head.put("traceNumber", exchange.getProperties().get("traceNumber"));
		head.put("transactionDate", exchange.getProperties().get("transactionDate"));
		head.put("transactionTime", exchange.getProperties().get("transactionTime"));
		head.put("responseCode", ResponseCode.RESPONSE_SUCCESS);
		
		Map<String, Object> body = new HashMap<String, Object>();
		@SuppressWarnings("unchecked")
		Map<String, String> pinKeySet = (Map<String, String>)exchange.getProperties().get("HSM_KEYTYPE_PIN");
		body.put("pinKey", pinKeySet.get("keyByTsf"));
		body.put("pinKeyCheck", pinKeySet.get("checkVal"));
		
		@SuppressWarnings("unchecked")
		Map<String, String> traceKeySet = (Map<String, String>)exchange.getProperties().get("HSM_KEYTYPE_TRACE");
		body.put("traceKey", traceKeySet.get("keyByTsf"));
		body.put("traceKeyCheck", traceKeySet.get("checkVal"));
		
		@SuppressWarnings("unchecked")
		Map<String, String> macKeySet = (Map<String, String>)exchange.getProperties().get("HSM_KEYTYPE_MAC");
		body.put("macKey", macKeySet.get("keyByTsf"));
		body.put("macKeyCheck", macKeySet.get("checkVal"));		
		
		Map<String, Object> retJsonMap = new HashMap<String, Object>();
		retJsonMap.put("head", head);
		retJsonMap.put("body", body);
		
		exchange.getOut().setBody(FormatUtil.map2Json(retJsonMap));	
	}
	
	public void responseError(Exchange exchange) {
		Exception e = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);		
		//String endPoint = exchange.getProperty(Exchange.FAILURE_ENDPOINT,String.class);
		
		Map<String, Object> head = new HashMap<String, Object>(); 
		head.put("channelId", exchange.getProperties().get("channelId"));
		head.put("transactionCode", exchange.getProperties().get("transactionCode"));
		head.put("terminalId", exchange.getProperties().get("terminalId"));
		head.put("channelId", exchange.getProperties().get("channelId"));
		head.put("traceNumber", exchange.getProperties().get("traceNumber"));
		head.put("transactionDate", exchange.getProperties().get("transactionDate"));
		head.put("transactionTime", exchange.getProperties().get("transactionTime"));
		head.put("responseCode", "9999");
		
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("errorDescription", e.getMessage());
		body.put("errorType", e.getClass().toString());	
		
		Map<String, Object> retJsonMap = new HashMap<String, Object>();
		retJsonMap.put("head", head);
		retJsonMap.put("body", body);
		
		exchange.getOut().setBody(FormatUtil.map2Json(retJsonMap));	
	}
}
