package com.ncr.powerswitch.processor;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ncr.powerswitch.dataObject.EppKey;
import com.ncr.powerswitch.dataObject.TerminalKey;
import com.ncr.powerswitch.exception.PowerswitchException;
import com.ncr.powerswitch.hsm.HSMCommand_C046;
import com.ncr.powerswitch.hsm.HSMCommand_C047;
import com.ncr.powerswitch.hsm.HSMCommand_C049;
import com.ncr.powerswitch.hsm.HSMCommand_D106;
import com.ncr.powerswitch.utils.FormatUtil;
import com.ncr.powerswitch.utils.LogUtil;
import com.ncr.powerswitch.utils.PowerSwitchConstant;
import com.ncr.powerswitch.utils.ResponseCode;
import com.ncr.powerswitch.utils.StringUtil;

public class RklNewProcessor  {
	
	private final static Log log = LogFactory.getLog(RklNewProcessor.class);
	private String errMsg = null;
	
	public void requestVerificationProcess(Exchange exchange) throws Exception{		
		String eppPublicKey =  exchange.getProperty("eppPublicKey",String.class);
		if (eppPublicKey==null || eppPublicKey.isEmpty()){
			errMsg = LogUtil.getClassMethodName() + ":" + "eppPublicKey is null(empty)";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}
		
		String eppPublicKeySign = exchange.getProperty("eppPublicKeySign",String.class);
		if (eppPublicKeySign==null || eppPublicKeySign.isEmpty()){
			errMsg = LogUtil.getClassMethodName() + ":" + "eppPublicKeySign is null(empty)";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}		
	}
	
	public void getTerminalIdProcess(Exchange exchange) throws Exception{		
		String terminalID = exchange.getProperty("terminalId",String.class);
		if (terminalID==null || terminalID.isEmpty()){
			errMsg = LogUtil.getClassMethodName() + ":" + "terminalID is null(empty)";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}		
		exchange.getOut().setBody(terminalID);
	}
	
	public void storeEppKeyProcess(Exchange exchange) throws Exception{
		EppKey eppKey = exchange.getIn().getBody(EppKey.class);		
		if (eppKey==null){
			errMsg = LogUtil.getClassMethodName() + ":" + "eppKey is null";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR,errMsg);
		}		
		exchange.getProperties().put("EppKey", eppKey);
	}
	
	public void requestHsmC047Process(Exchange exchange) throws Exception{		
		EppKey eppKey = exchange.getProperty("EppKey", EppKey.class);
		
		if (eppKey==null){
			errMsg = LogUtil.getClassMethodName() + ":" + "eppKey is null";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR,errMsg);
		}
		
		String vendorKey = eppKey.getManupk();
		
		if (vendorKey==null || vendorKey.isEmpty()){
			errMsg = LogUtil.getClassMethodName() + ":" + "vendorKey is null(empty)";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR,errMsg);
		}
		
		Map<String, String> inputMap = new HashMap<String, String>();
		inputMap.put("MANUPK", vendorKey);
		inputMap.put("MANUPKLENGTH", Integer.toString(vendorKey.length()));
		inputMap.put("strEppPublicKey", exchange.getProperty("eppPublicKey",String.class));
		inputMap.put("strEppPublicKeySign", exchange.getProperty("eppPublicKeySign",String.class));
		
		HSMCommand_C047 c047 = new HSMCommand_C047(inputMap);
		String c047msg = c047.packageInputField();
		log.debug("c047 message: " + c047msg + " Starting command C047");
		byte[] bytelst = StringUtil.ASCII_To_BCD(c047msg.getBytes(), c047msg.length());		
		exchange.getOut().setBody(bytelst);
	}	
	
	public void responseHsmC047Process(Exchange exchange) throws Exception{		
		
		byte[] bytes = exchange.getIn().getBody(byte[].class);
		
		if (bytes==null){
			errMsg=LogUtil.getClassMethodName() + ":" + "response byte[] is null";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR,errMsg);
		}
		
		String c047Res = StringUtil.bcd2Str(bytes, bytes.length);		
		
		if (c047Res==null||c047Res.isEmpty()){
			errMsg = LogUtil.getClassMethodName() + ":" + "response string is null(empty)";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}
		
		
		if (c047Res.length() == 20 && c047Res.substring(0, 2).equals("41")
				&& c047Res.substring(18, 20).equals("00")) {
			log.debug("C047 verified");
		} else {
			errMsg = LogUtil.getClassMethodName() + ":" + "failed and c047Res:" + c047Res;
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR,errMsg);
		}
		
	}	
	
	public void requestHsmD106Process(Exchange exchange) throws Exception{
		//System.out.println("Starting command D106");
		HSMCommand_D106 d106 = new HSMCommand_D106();
		String d106msg = d106.packageInputField();
		//System.out.println("D106 message: " + d106msg + " Starting command D106");
		log.debug("D106 message: " + d106msg + " Starting command D106");
		byte[] bytelst = StringUtil.ASCII_To_BCD(d106msg.getBytes(), d106msg.length());		
		exchange.getOut().setBody(bytelst);		
	}	
		
	public void responseHsmD106Process(Exchange exchange) throws Exception{
		byte[] bytes = exchange.getIn().getBody(byte[].class);
		if (bytes==null){
			errMsg = LogUtil.getClassMethodName() + ":" + "response byte[] is null";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}
		String d106Res = StringUtil.bcd2Str(bytes, bytes.length);		
		//System.out.println("verifying D106 Return " + d106Res);
		if (d106Res == null || d106Res.isEmpty()) {
			errMsg = LogUtil.getClassName()+"-"+LogUtil.getMethodName()+":"+"d106Res is null(empty)";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}
		
		TerminalKey terminalKey = new TerminalKey();
		String terminalID = exchange.getProperty("terminalId",String.class);
		if (terminalID==null || terminalID.isEmpty()){
			errMsg = LogUtil.getClassMethodName() + ":" + "terminalID is null(empty)";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}		
		terminalKey.setTerminalId(terminalID);
		
		String masterKey = null;
		String checkCode = null;		
		if (d106Res.length() == 52 && d106Res.substring(0, 2).equals("41")) {
			masterKey = d106Res.substring(4, 36);
			//System.out.println("Master Key is " + masterKey);
			//log.debug("Master Key is " + masterKey);
			terminalKey.setMasterKey(masterKey);
			checkCode = d106Res.substring(36, 52);
			terminalKey.setMasterKeyCheck(checkCode);
		} else {
			errMsg = LogUtil.getClassMethodName() + ":" + "return error and d106Res:" + d106Res ;
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}		
		
		exchange.getProperties().put("TerminalKey", terminalKey);	
	}	
	
	public void insertTerminalKeyProcess(Exchange exchange) throws Exception{	
		TerminalKey terminalKey = exchange.getProperty("TerminalKey", TerminalKey.class);		
		if (terminalKey==null){
			errMsg = LogUtil.getClassMethodName() + ":" + "terminalKey is null";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}		
		exchange.getOut().setBody(terminalKey);
	}
	
	public void requestHsmC049Process(Exchange exchange) throws Exception{		
		TerminalKey terminalKey = exchange.getProperty("TerminalKey", TerminalKey.class);			
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
		//System.out.println("Starting Command C049");
		inputMap.put("KEY_TEXT", masterKey);
		inputMap.put("userReservedStr", "0000000000000000");
		inputMap.put("strEppPublicKey", exchange.getProperty("eppPublicKey", String.class));
		HSMCommand_C049 c049 = new HSMCommand_C049(inputMap);
		String c049Msg = c049.packageInputField();
		
		//System.out.println("C049 message: " + c049Msg + " Starting command C049");
		log.debug("C049 message: " + c049Msg + " Starting command C049");
		byte[] bytelst = StringUtil.ASCII_To_BCD(c049Msg.getBytes(), c049Msg.length());		
		exchange.getOut().setBody(bytelst);		
	}	
	
	public void responseHsmC049Process(Exchange exchange) throws Exception{
		byte[] bytes = exchange.getIn().getBody(byte[].class);
		if (bytes==null){
			errMsg = LogUtil.getClassMethodName() + ":" + "response byte[] is null";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}
		String c049Res = StringUtil.bcd2Str(bytes, bytes.length);		
		//System.out.println("verifying C049 Return " + c049Res);
		if (c049Res == null || c049Res.isEmpty()) {
			errMsg = LogUtil.getClassName()+"-"+LogUtil.getMethodName()+":"+"c049Res is null(empty)";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}
		
		String encryptedMk = null;		
		if (c049Res.length() >= 23 || c049Res.substring(0, 2).equals("41")) {
			encryptedMk = c049Res.substring(22, c049Res.length());
			//System.out.println("C049转加密文： " + encryptedMk);
		} else {
			errMsg = LogUtil.getClassName()+"-"+LogUtil.getMethodName()+":"+"return error and c049Res:" + c049Res;
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}		
		
		exchange.getProperties().put("encryptedMk", encryptedMk);	
	}	
	
	public void requestHsmC046Process(Exchange exchange) throws Exception{		
		TerminalKey terminalKey = exchange.getProperty("TerminalKey", TerminalKey.class);			
		if (terminalKey==null){
			errMsg = LogUtil.getClassMethodName() + ":" + "terminalKey is null";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}
		
		EppKey eppKey = exchange.getProperty("EppKey", EppKey.class);		
		if (eppKey==null){
			errMsg = LogUtil.getClassMethodName() + ":" + "eppKey is null";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR,errMsg);
		}
		
		//System.out.println("Starting Command C046");
		String sk = eppKey.getSk();	
		if (sk==null||sk.isEmpty()){
			errMsg = LogUtil.getClassMethodName() + ":" + "sk is null(empty)";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}
		
		String encryptedMk = exchange.getProperty("encryptedMk", String.class);
		if (encryptedMk==null||encryptedMk.isEmpty()){
			errMsg = LogUtil.getClassMethodName() + ":" + "encryptedMk is null(empty)";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}
		
		Map<String, String> inputMap = new HashMap<String, String>();	
		inputMap.put("SK", sk);
		inputMap.put("userReservedStr", "0000000000000000");
		inputMap.put("encryption", encryptedMk);
		
		HSMCommand_C046 c046 = new HSMCommand_C046(inputMap);
		String c046Msg = c046.packageInputField();
		
		//System.out.println("C046 message: " + c046Msg + " Starting command C046");
		log.debug("C046 message: " + c046Msg + " Starting command C046");
		byte[] bytelst = StringUtil.ASCII_To_BCD(c046Msg.getBytes(), c046Msg.length());		
		exchange.getOut().setBody(bytelst);		
		
	}
	
	public void responseHsmC046Process(Exchange exchange) throws Exception{
		byte[] bytes = exchange.getIn().getBody(byte[].class);
		if (bytes==null){
			errMsg = LogUtil.getClassMethodName() + ":" + "response byte[] is null";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}
		
		String c046Res = StringUtil.bcd2Str(bytes, bytes.length);
		if (c046Res == null || c046Res.isEmpty()) {
			errMsg = LogUtil.getClassName()+"-"+LogUtil.getMethodName()+":"+"c046Res is null(empty)";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}
		
		//System.out.println("C046 Return " + c046Res);					
		String encryptSign = null;		
		if (c046Res.length()>=23 || c046Res.substring(0, 2).equals("41")) {
			//System.out.println("C046 密文簽名： " + c046Res.substring(22, c046Res.length()));
			encryptSign =  c046Res.substring(22, c046Res.length());			
		} else {
			errMsg = LogUtil.getClassName()+"-"+LogUtil.getMethodName()+":"+"return error and c049Res:" + c046Res;
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
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
