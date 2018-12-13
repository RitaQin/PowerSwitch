package com.ncr.powerswitch.esb;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ESB_AC51 extends ESBClient {
	
	private final static Log log = LogFactory.getLog(ESB_AC51.class);
	
	//需要验证的域
	public String[] verifyFields = null; 

	public ESB_AC51(String serverIp_, int serverPort_, boolean isOpenClient_, int maxPacket_) {
		super(serverIp_, serverPort_, isOpenClient_, maxPacket_);
	}
	
	@Override
	public String constructRequest(Map<String, String> requestMap) {
		
		return null;
	}

	@Override
	public String sendAndReceivePackets(String msg, String ip, String port, boolean unpack) {
		
		return null;
	}
	
	public String[] getVerifyFields() {
		return verifyFields;
	}

	public void setVerifyFields(String[] verifyFields) {
		this.verifyFields = verifyFields;
	}

}
