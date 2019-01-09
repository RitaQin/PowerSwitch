package com.ncr.powerswitch.processor;


import org.apache.camel.Exchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ncr.powerswitch.dataObject.TerminalKey;
import com.ncr.powerswitch.exception.PowerswitchException;
import com.ncr.powerswitch.utils.LogUtil;
import com.ncr.powerswitch.utils.PowerSwitchConstant;

public class NormalProcessor {
	
	private final static Log log = LogFactory.getLog(NormalProcessor.class);
	private String errMsg = null;
	
	public void getTerminalIdProcess(Exchange exchange) throws Exception{
		String terminalID = exchange.getProperty("terminalId",String.class);
		if (terminalID==null || terminalID.isEmpty()){
			errMsg = LogUtil.getClassMethodName() + ":" + "terminalID is null(empty)";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}	
		exchange.getOut().setBody(terminalID);
	}
	
	public void putTerminalKeyProcess(Exchange exchange) throws Exception{
		TerminalKey terminalKey =  exchange.getIn().getBody(TerminalKey.class);
		if (terminalKey==null){
			errMsg = LogUtil.getClassMethodName() + ":" + "terminalKey is null";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR,errMsg);
		}	
		exchange.setProperty("terminalKey", terminalKey);		
	}
}
