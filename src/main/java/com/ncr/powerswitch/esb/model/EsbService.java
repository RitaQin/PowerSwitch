package com.ncr.powerswitch.esb.model;

public class EsbService {
	
	private SysHead SYS_HEAD;
	private AppHead APP_HEAD;
	private LocalHead LOCAL_HEAD;
	private Body BODY;
	/**
	 * @return the sYS_HEAD
	 */
	public SysHead getSYS_HEAD() {
		return SYS_HEAD;
	}
	/**
	 * @param sYS_HEAD the sYS_HEAD to set
	 */
	public void setSYS_HEAD(SysHead sYS_HEAD) {
		SYS_HEAD = sYS_HEAD;
	}
	/**
	 * @return the aPP_HEAD
	 */
	public AppHead getAPP_HEAD() {
		return APP_HEAD;
	}
	/**
	 * @param aPP_HEAD the aPP_HEAD to set
	 */
	public void setAPP_HEAD(AppHead aPP_HEAD) {
		APP_HEAD = aPP_HEAD;
	}
	/**
	 * @return the lOCAL_HEAD
	 */
	public LocalHead getLOCAL_HEAD() {
		return LOCAL_HEAD;
	}
	/**
	 * @param lOCAL_HEAD the lOCAL_HEAD to set
	 */
	public void setLOCAL_HEAD(LocalHead lOCAL_HEAD) {
		LOCAL_HEAD = lOCAL_HEAD;
	}
	/**
	 * @return the bODY
	 */
	public Body getBODY() {
		return BODY;
	}
	/**
	 * @param bODY the bODY to set
	 */
	public void setBODY(Body bODY) {
		BODY = bODY;
	}	
}
