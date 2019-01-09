package com.ncr.powerswitch.esb.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("LOCAL_HEAD")
public class LocalHead {
	
	@XStreamAlias("TranCode")
	private String TranCode;
	
	@XStreamAlias("SendCardPeriod")
	private String SendCardPeriod;
	/**
	 * @return the tranCode
	 */
	public String getTranCode() {
		return TranCode;
	}
	/**
	 * @param tranCode the tranCode to set
	 */
	public void setTranCode(String tranCode) {
		TranCode = tranCode;
	}
	/**
	 * @return the sendCardPeriod
	 */
	public String getSendCardPeriod() {
		return SendCardPeriod;
	}
	/**
	 * @param sendCardPeriod the sendCardPeriod to set
	 */
	public void setSendCardPeriod(String sendCardPeriod) {
		SendCardPeriod = sendCardPeriod;
	}
	
	
	
}
