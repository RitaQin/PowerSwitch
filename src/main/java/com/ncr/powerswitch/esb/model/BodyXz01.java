package com.ncr.powerswitch.esb.model;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BODY")
public class BodyXz01 implements BodyIntf {
	
	@XStreamAlias("RcrdCnt")
	private String RcrdCnt;
	
	@XStreamAlias("array")
	private List<BusinessInfo> array;

	/**
	 * @return the rcrdCnt
	 */
	public String getRcrdCnt() {
		return RcrdCnt;
	}

	/**
	 * @param rcrdCnt the rcrdCnt to set
	 */
	public void setRcrdCnt(String rcrdCnt) {
		RcrdCnt = rcrdCnt;
	}

	/**
	 * @return the array
	 */
	public List<BusinessInfo> getArray() {
		return array;
	}

	/**
	 * @param array the array to set
	 */
	public void setArray(List<BusinessInfo> array) {
		this.array = array;
	} 
	
	
	
	
}
