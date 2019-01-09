package com.ncr.powerswitch.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ncr.powerswitch.dataObject.TerminalKey;
import com.ncr.powerswitch.exception.PowerswitchException;
import com.ncr.powerswitch.hsm.HSMCommand_D132;
import com.ncr.powerswitch.hsm.HSMCommand_D134;
import com.ncr.powerswitch.utils.GeneralUtil;
import com.ncr.powerswitch.utils.LogUtil;
import com.ncr.powerswitch.utils.PowerSwitchConstant;
import com.ncr.powerswitch.utils.StringUtil;

public class MacVerificationProcessor {
	
	private final static Log log = LogFactory.getLog(MacVerificationProcessor.class);
	private String errMsg = null;
	
	public void putMacDataLenProcess(Exchange exchange) throws Exception{		
		String macData =  exchange.getProperty("macData", String.class);
		if (macData==null || macData.isEmpty()){
			errMsg = LogUtil.getClassMethodName() + ":" + "macData is null(empty)";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}	
		exchange.setProperty("macDataLen", GeneralUtil.generatePayloadLength(macData));
	}
	
	public void requestHsmD132Process(Exchange exchange) throws Exception{
		
		TerminalKey terminalKey = exchange.getProperty("TerminalKey", TerminalKey.class);			
		if (terminalKey==null){
			errMsg = LogUtil.getClassMethodName() + ":" + "terminalKey is null";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}
		
		String macKey = terminalKey.getMacKey();
		if (macKey==null||macKey.isEmpty()){
			errMsg = LogUtil.getClassMethodName() + ":" + "macKey is null(empty)";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}
		
		String macData =  exchange.getProperty("macData", String.class);
		if (macData==null || macData.isEmpty()){
			errMsg = LogUtil.getClassMethodName() + ":" + "macData is null(empty)";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}	
		
		Map<String, String> inputMap = new HashMap<String, String>();
		
		inputMap.put("macKeyLen", "HSM_KEYLEN_2");
		inputMap.put("macKey", macKey);
		inputMap.put("macData", macData);
		
		HSMCommand_D132 d132 = new HSMCommand_D132(inputMap);
		String d132msg = d132.packageInputField();
		//System.out.println("d132 message: " + d132msg + " Starting command D132");
		log.debug("d132 message: " + d132msg + " Starting command D132");
		byte[] bytelst = StringUtil.ASCII_To_BCD(d132msg.getBytes(), d132msg.length());		
		exchange.getOut().setBody(bytelst);
	}
	
	public void responseHsmD132Process(Exchange exchange) throws Exception{		
		byte[] bytes = exchange.getIn().getBody(byte[].class);
		if (bytes==null){
			errMsg = LogUtil.getClassMethodName() + ":" + "response byte[] is null";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}
		
		String d132Res = StringUtil.bcd2Str(bytes, bytes.length);
		if (d132Res == null || d132Res.isEmpty()) {
			errMsg = LogUtil.getClassName()+"-"+LogUtil.getMethodName()+":"+"d132Res is null(empty)";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}
		
		//System.out.println("d132 returns: " + d132Res);
		//log.info("d132 returns: " + d132Res);
		String mac=null;
		if (d132Res.length() > 2 && d132Res.substring(0, 2).equals("41")) {
			mac = d132Res.substring(2);
		}
		
		if (mac==null){
			errMsg = LogUtil.getClassName()+"-"+LogUtil.getMethodName()+":"+"return error and d132Res:" + d132Res;
			log.error(errMsg);		
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}
		
		exchange.setProperty("mac", mac);
	}
	
	public void responseHsmD132BlankProcess(Exchange exchange) throws Exception{		
		exchange.setProperty("mac", "0000000000000000");
	}
	
	public void requestHsmD134Process(Exchange exchange) throws Exception{		
		TerminalKey terminalKey = exchange.getProperty("TerminalKey", TerminalKey.class);			
		if (terminalKey==null){
			errMsg = LogUtil.getClassMethodName() + ":" + "terminalKey is null";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}
		
		String macKey = terminalKey.getMacKey();
		if (macKey==null||macKey.isEmpty()){
			errMsg = LogUtil.getClassMethodName() + ":" + "macKey is null(empty)";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}
		
		String mac =  exchange.getProperty("mac", String.class);
		if (mac==null || mac.isEmpty()){
			errMsg = LogUtil.getClassMethodName() + ":" + "mac is null(empty)";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}	
		
		String macData =  exchange.getProperty("macData", String.class);
		if (macData==null || macData.isEmpty()){
			errMsg = LogUtil.getClassMethodName() + ":" + "macData is null(empty)";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}		
		
		Map<String, String> inputMap = new HashMap<String, String>();		
		inputMap.put("macKeyLen", "HSM_KEYLEN_2");
		inputMap.put("macKey", macKey);
		inputMap.put("mac", mac);
		inputMap.put("macData", macData);		
		
		HSMCommand_D134 d134 = new HSMCommand_D134(inputMap);
		String d134msg = d134.packageInputField();
		//System.out.println("d134 message: " + d134msg + " Starting command D134");
		log.debug("d134 message: " + d134msg + " Starting command D134");
		byte[] bytelst = StringUtil.ASCII_To_BCD(d134msg.getBytes(), d134msg.length());		
		exchange.getOut().setBody(bytelst);
	}
	
	public void responseHsmD134Process(Exchange exchange) throws Exception{		
		byte[] bytes = exchange.getIn().getBody(byte[].class);
		if (bytes==null){
			errMsg = LogUtil.getClassMethodName() + ":" + "response byte[] is null";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}
		
		String d134Res = StringUtil.bcd2Str(bytes, bytes.length);
		if (d134Res == null || d134Res.isEmpty()) {
			errMsg = LogUtil.getClassName()+"-"+LogUtil.getMethodName()+":"+"d134Res is null(empty)";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}
		
		if (!(d134Res.length() >= 2 && d134Res.substring(0, 2).equals("41"))) {
			errMsg = LogUtil.getClassName()+"-"+LogUtil.getMethodName()+":"+"mac not match and d134Res:" + d134Res;
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR,errMsg);
		}
	}
	
	
}
