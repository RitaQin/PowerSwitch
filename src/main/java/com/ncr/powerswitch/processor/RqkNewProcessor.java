package com.ncr.powerswitch.processor;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ncr.powerswitch.hsm.HSMCommand_D104;
import com.ncr.powerswitch.hsm.HSMCommand_D107;
import com.ncr.powerswitch.dataObject.TerminalKey;
import com.ncr.powerswitch.utils.FormatUtil;
import com.ncr.powerswitch.utils.LogUtil;
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
	
	private final static Log log = LogFactory.getLog(RqkNewProcessor.class);
	private String errMsg = null;
	
	public void requestVerificationProcess(Exchange exchange) throws Exception{
//		@SuppressWarnings("unchecked")
//		Map<String, Object> requestMap =  exchange.getIn().getBody(Map.class);
//		exchange.getProperties().putAll(requestMap);			
	}
	
	public void getTerminalIdProcess(Exchange exchange) throws Exception{
		String terminalID = exchange.getProperty("terminalId",String.class);
		if (terminalID==null || terminalID.isEmpty()){
			errMsg = getClassMethodName() + ":" + "terminalID is null(empty)";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}		
		exchange.getOut().setBody(terminalID);
	}
	
	public void storeMasterkeyProcess(Exchange exchange) throws Exception{
		TerminalKey terminalKey =  exchange.getIn().getBody(TerminalKey.class);
		if (terminalKey==null){
			errMsg = getClassMethodName() + ":" + "terminalKey is null";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR,errMsg);
		}	
		exchange.setProperty("terminalKey", terminalKey);		
	}
	
	public void requestHsmD107Process(Exchange exchange, String keyType) throws Exception{
		
		TerminalKey terminalKey = exchange.getProperty("terminalKey", TerminalKey.class);			
		if (terminalKey==null){
			errMsg = LogUtil.getClassMethodName() + ":" + "terminalKey is null";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}
		
		String masterKey = terminalKey.getMasterKey();
		if (masterKey==null||masterKey.isEmpty()){
			errMsg = LogUtil.getClassMethodName() + ":" + "masterKey is null(empty)";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}
		
		Map<String, String> inputMap = new HashMap<String, String>();		
		inputMap.put("masterKey", masterKey);
		inputMap.put("keyType", keyType);
		HSMCommand_D107 d107 = new HSMCommand_D107(inputMap);
		String d107msg = d107.packageInputField();
		//System.out.println("d107 message: " + d107msg + " Starting command D107" + " ,KeyType:" + keyType);
		log.debug("d107 message: " + d107msg + " Starting command D107" + " ,KeyType:" + keyType);
		byte[] bytelst = StringUtil.ASCII_To_BCD(d107msg.getBytes(), d107msg.length());		
		exchange.getOut().setBody(bytelst);
	}
	
	public void responseHsmD107Process(Exchange exchange, String keyType) throws Exception{		
		byte[] bytes = exchange.getIn().getBody(byte[].class);
		if (bytes==null){
			errMsg = LogUtil.getClassMethodName() + ":" + "response byte[] is null";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}
		
		String d107Res = StringUtil.bcd2Str(bytes, bytes.length);
		if (d107Res == null || d107Res.isEmpty()) {
			errMsg = LogUtil.getClassName()+"-"+LogUtil.getMethodName()+":"+"d107Res is null(empty)";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}
		
		//System.out.println("d107 returns: " + d107Res + " ,KeyType:" + keyType);
		String keyByHsm = null; 
		String keyByTsf = null;
		String checkVal = null;
		if (d107Res.length() > 2 && d107Res.substring(0, 2).equals("41")) {
			int keyLen = Integer.parseInt(d107Res.substring(2, 4), 16); 
			keyByHsm = d107Res.substring(4, 4 + keyLen * 2);
			System.out.println(keyType + " key by hsm " + keyByHsm);
			keyByTsf = d107Res.substring(4 + keyLen * 2, 4 + keyLen * 4);
			System.out.println(keyType + " key by tfs " + keyByTsf);
			checkVal = d107Res.substring(4 + keyLen * 4, 20 + keyLen * 4);
		}
		
		if (keyByHsm==null || keyByTsf==null || checkVal==null) {	
			errMsg = LogUtil.getClassName()+"-"+LogUtil.getMethodName()+":"+"return error and d107Res:" + d107Res;
			log.error(errMsg);		
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
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
		if (keySet==null){
			errMsg = LogUtil.getClassMethodName() + ":" + "keySet(" + keyType + ") is null";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}
		
		String keyByHsm = keySet.get("keyByHsm");
		if (keyByHsm == null || keyByHsm.isEmpty()) {
			errMsg = LogUtil.getClassName()+"-"+LogUtil.getMethodName()+":"+"keyByHsm(" + keyType + ") is null(empty)";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}
		
		Map<String, String> inputMap = new HashMap<String, String>();
		inputMap.put("workKey", keyByHsm);
		inputMap.put("keyIndex", "0110");
		inputMap.put("keyType", keyType);
		
		HSMCommand_D104 d104 = new HSMCommand_D104(inputMap);
		String d104msg = d104.packageInputField();
		//System.out.println("d104 message: " + d104msg + " Starting command D104" + " ,KeyType:" + keyType);
		log.debug("d104 message: " + d104msg + " Starting command D104" + " ,KeyType:" + keyType);
		byte[] bytelst = StringUtil.ASCII_To_BCD(d104msg.getBytes(), d104msg.length());		
		exchange.getOut().setBody(bytelst);		
	}
	
	public void responseHsmD104Process(Exchange exchange, String keyType) throws Exception{
		
		byte[] bytes = exchange.getIn().getBody(byte[].class);
		if (bytes==null){
			errMsg = LogUtil.getClassMethodName() + ":" + "response byte[] is null";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}
		
		String d104Res = StringUtil.bcd2Str(bytes, bytes.length);
		if (d104Res == null || d104Res.isEmpty()) {
			errMsg = LogUtil.getClassName()+"-"+LogUtil.getMethodName()+":"+"d104Res is null(empty)";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}
		
		//System.out.println("d104 returns: " + d104Res);
		String keyByHost = null;
		if (d104Res.length() > 2 && d104Res.substring(0, 2).equals("41")) {
			int keyLen = Integer.parseInt(d104Res.substring(2, 4), 16); 
			keyByHost = d104Res.substring(4, 4 + keyLen * 2);
		}
		
		if(keyByHost==null) {
			errMsg = LogUtil.getClassName()+"-"+LogUtil.getMethodName()+":"+"return error and d104Res:" + d104Res;
			log.error(errMsg);		
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}
		
		@SuppressWarnings("unchecked")
		Map<String, String> keySet = (Map<String, String>)exchange.getProperties().get(keyType);	
		keySet.put("keyByHost", keyByHost);
		exchange.getProperties().put(keyType,keySet);		
	}
	
	public void updateTerminalKeyProcess(Exchange exchange) throws Exception{		
		TerminalKey terminalKey = exchange.getProperty("terminalKey", TerminalKey.class);		
		if (terminalKey==null){
			errMsg = LogUtil.getClassMethodName() + ":" + "terminalKey is null";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}	
		
		@SuppressWarnings("unchecked")
		Map<String, String> pinKeySet = (Map<String, String>)exchange.getProperties().get("HSM_KEYTYPE_PIN");
		//terminalKey.setPinKey(pinKeySet.get("keyByTsf"));
		terminalKey.setTraceKey(pinKeySet.get("keyByHsm"));
		terminalKey.setPinkeyHsm(pinKeySet.get("keyByHost"));
		
		@SuppressWarnings("unchecked")
		Map<String, String> traceKeySet = (Map<String, String>)exchange.getProperties().get("HSM_KEYTYPE_TRACE");
		//terminalKey.setTraceKey(traceKeySet.get("keyByTsf"));
		terminalKey.setTraceKey(traceKeySet.get("keyByHsm"));
		terminalKey.setTraceKeyHsm(traceKeySet.get("keyByHost"));
		
		@SuppressWarnings("unchecked")
		Map<String, String> macKeySet = (Map<String, String>)exchange.getProperties().get("HSM_KEYTYPE_MAC");	
		//terminalKey.setMacKey(macKeySet.get("keyByTsf"));
		terminalKey.setTraceKey(macKeySet.get("keyByHsm"));
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
	
	public static String getClassMethodName() {
		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[2];
        String className = e.getClassName();
        String methodName = e.getMethodName();
        return className + "-" + methodName;
	}
}
