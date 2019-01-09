package com.ncr.powerswitch.esb.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Ret")
public class EsbRet {
	
	@XStreamAlias("ReturnCode")
	private String ReturnCode;
	
	@XStreamAlias("ReturnMsg")
	private String ReturnMsg;
	
	public String getReturnCode() {
		return ReturnCode;
	}
	public void setReturnCode(String returnCode) {
		ReturnCode = returnCode;
	}
	public String getReturnMsg() {
		return ReturnMsg;
	}
	public void setReturnMsg(String returnMsg) {
		ReturnMsg = returnMsg;
	}

}
