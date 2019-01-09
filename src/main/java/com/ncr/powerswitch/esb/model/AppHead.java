package com.ncr.powerswitch.esb.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("APP_HEAD")
public class AppHead {
	
	@XStreamAlias("TranTellerNo")
	private String TranTellerNo;
	
	@XStreamAlias("TranBranchId")
	private String TranBranchId;
	
	@XStreamAlias("TranTellerPassword")
	private String TranTellerPassword;
	
	@XStreamAlias("TranTellerLevel")
	private String TranTellerLevel;
	
	@XStreamAlias("TranTellerType")
	private String TranTellerType;
	
	@XStreamAlias("ApprFlag")
	private String ApprFlag;
	
	@XStreamAlias("AuthFlag")
	private String AuthFlag;
	
	/**
	 * @return the tranTellerNo
	 */
	public String getTranTellerNo() {
		return TranTellerNo;
	}
	/**
	 * @param tranTellerNo the tranTellerNo to set
	 */
	public void setTranTellerNo(String tranTellerNo) {
		TranTellerNo = tranTellerNo;
	}
	/**
	 * @return the tranBranchId
	 */
	public String getTranBranchId() {
		return TranBranchId;
	}
	/**
	 * @param tranBranchId the tranBranchId to set
	 */
	public void setTranBranchId(String tranBranchId) {
		TranBranchId = tranBranchId;
	}
	/**
	 * @return the tranTellerPassword
	 */
	public String getTranTellerPassword() {
		return TranTellerPassword;
	}
	/**
	 * @param tranTellerPassword the tranTellerPassword to set
	 */
	public void setTranTellerPassword(String tranTellerPassword) {
		TranTellerPassword = tranTellerPassword;
	}
	/**
	 * @return the tranTellerLevel
	 */
	public String getTranTellerLevel() {
		return TranTellerLevel;
	}
	/**
	 * @param tranTellerLevel the tranTellerLevel to set
	 */
	public void setTranTellerLevel(String tranTellerLevel) {
		TranTellerLevel = tranTellerLevel;
	}
	/**
	 * @return the tranTellerType
	 */
	public String getTranTellerType() {
		return TranTellerType;
	}
	/**
	 * @param tranTellerType the tranTellerType to set
	 */
	public void setTranTellerType(String tranTellerType) {
		TranTellerType = tranTellerType;
	}
	/**
	 * @return the apprFlag
	 */
	public String getApprFlag() {
		return ApprFlag;
	}
	/**
	 * @param apprFlag the apprFlag to set
	 */
	public void setApprFlag(String apprFlag) {
		ApprFlag = apprFlag;
	}
	/**
	 * @return the authFlag
	 */
	public String getAuthFlag() {
		return AuthFlag;
	}
	/**
	 * @param authFlag the authFlag to set
	 */
	public void setAuthFlag(String authFlag) {
		AuthFlag = authFlag;
	}
	
}
