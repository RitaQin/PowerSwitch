package com.ncr.powerswitch.esb.model;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("SYS_HEAD")
public class SysHead {
	private String ServiceCode;
	private String ServiceScene;
	private String ConsumerId;
	private String TargetId;
	private String ChannelType;
	private String OrgConsumerId;
	private String ConsumerSeqNo;
	private String OrgConsumerSeqNo;
	private String TranDate;
	private String TranTime;
	private String ServSeqNo;
	private String ReturnStatus;
	private List<EsbRet> array; 
	private String TerminalCode;
	private String OrgTerminalCode;
	private String ConsumerSvrId;
	private String OrgConsumerSvrId;
	private String DestSvrId;
	private String UserLang;
	private String FileFlag;
	private String SrcFilePath;
	private String SrcFileName;
	private String DestFilePath;
	private String DestFileName;
	/**
	 * @return the serviceCode
	 */
	public String getServiceCode() {
		return ServiceCode;
	}
	/**
	 * @param serviceCode the serviceCode to set
	 */
	public void setServiceCode(String serviceCode) {
		ServiceCode = serviceCode;
	}
	/**
	 * @return the serviceScene
	 */
	public String getServiceScene() {
		return ServiceScene;
	}
	/**
	 * @param serviceScene the serviceScene to set
	 */
	public void setServiceScene(String serviceScene) {
		ServiceScene = serviceScene;
	}
	/**
	 * @return the consumerId
	 */
	public String getConsumerId() {
		return ConsumerId;
	}
	/**
	 * @param consumerId the consumerId to set
	 */
	public void setConsumerId(String consumerId) {
		ConsumerId = consumerId;
	}
	/**
	 * @return the targetId
	 */
	public String getTargetId() {
		return TargetId;
	}
	/**
	 * @param targetId the targetId to set
	 */
	public void setTargetId(String targetId) {
		TargetId = targetId;
	}
	/**
	 * @return the channelType
	 */
	public String getChannelType() {
		return ChannelType;
	}
	/**
	 * @param channelType the channelType to set
	 */
	public void setChannelType(String channelType) {
		ChannelType = channelType;
	}
	/**
	 * @return the orgConsumerId
	 */
	public String getOrgConsumerId() {
		return OrgConsumerId;
	}
	/**
	 * @param orgConsumerId the orgConsumerId to set
	 */
	public void setOrgConsumerId(String orgConsumerId) {
		OrgConsumerId = orgConsumerId;
	}
	/**
	 * @return the consumerSeqNo
	 */
	public String getConsumerSeqNo() {
		return ConsumerSeqNo;
	}
	/**
	 * @param consumerSeqNo the consumerSeqNo to set
	 */
	public void setConsumerSeqNo(String consumerSeqNo) {
		ConsumerSeqNo = consumerSeqNo;
	}
	/**
	 * @return the orgConsumerSeqNo
	 */
	public String getOrgConsumerSeqNo() {
		return OrgConsumerSeqNo;
	}
	/**
	 * @param orgConsumerSeqNo the orgConsumerSeqNo to set
	 */
	public void setOrgConsumerSeqNo(String orgConsumerSeqNo) {
		OrgConsumerSeqNo = orgConsumerSeqNo;
	}
	/**
	 * @return the tranDate
	 */
	public String getTranDate() {
		return TranDate;
	}
	/**
	 * @param tranDate the tranDate to set
	 */
	public void setTranDate(String tranDate) {
		TranDate = tranDate;
	}
	/**
	 * @return the tranTime
	 */
	public String getTranTime() {
		return TranTime;
	}
	/**
	 * @param tranTime the tranTime to set
	 */
	public void setTranTime(String tranTime) {
		TranTime = tranTime;
	}
	/**
	 * @return the servSeqNo
	 */
	public String getServSeqNo() {
		return ServSeqNo;
	}
	/**
	 * @param servSeqNo the servSeqNo to set
	 */
	public void setServSeqNo(String servSeqNo) {
		ServSeqNo = servSeqNo;
	}
	/**
	 * @return the returnStatus
	 */
	public String getReturnStatus() {
		return ReturnStatus;
	}
	/**
	 * @param returnStatus the returnStatus to set
	 */
	public void setReturnStatus(String returnStatus) {
		ReturnStatus = returnStatus;
	}
	/**
	 * @return the array
	 */
	public List<EsbRet> getArray() {
		return array;
	}
	/**
	 * @param array the array to set
	 */
	public void setArray(List<EsbRet> array) {
		this.array = array;
	}
	/**
	 * @return the terminalCode
	 */
	public String getTerminalCode() {
		return TerminalCode;
	}
	/**
	 * @param terminalCode the terminalCode to set
	 */
	public void setTerminalCode(String terminalCode) {
		TerminalCode = terminalCode;
	}
	/**
	 * @return the orgTerminalCode
	 */
	public String getOrgTerminalCode() {
		return OrgTerminalCode;
	}
	/**
	 * @param orgTerminalCode the orgTerminalCode to set
	 */
	public void setOrgTerminalCode(String orgTerminalCode) {
		OrgTerminalCode = orgTerminalCode;
	}
	/**
	 * @return the consumerSvrId
	 */
	public String getConsumerSvrId() {
		return ConsumerSvrId;
	}
	/**
	 * @param consumerSvrId the consumerSvrId to set
	 */
	public void setConsumerSvrId(String consumerSvrId) {
		ConsumerSvrId = consumerSvrId;
	}
	/**
	 * @return the orgConsumerSvrId
	 */
	public String getOrgConsumerSvrId() {
		return OrgConsumerSvrId;
	}
	/**
	 * @param orgConsumerSvrId the orgConsumerSvrId to set
	 */
	public void setOrgConsumerSvrId(String orgConsumerSvrId) {
		OrgConsumerSvrId = orgConsumerSvrId;
	}
	/**
	 * @return the destSvrId
	 */
	public String getDestSvrId() {
		return DestSvrId;
	}
	/**
	 * @param destSvrId the destSvrId to set
	 */
	public void setDestSvrId(String destSvrId) {
		DestSvrId = destSvrId;
	}
	/**
	 * @return the userLang
	 */
	public String getUserLang() {
		return UserLang;
	}
	/**
	 * @param userLang the userLang to set
	 */
	public void setUserLang(String userLang) {
		UserLang = userLang;
	}
	/**
	 * @return the fileFlag
	 */
	public String getFileFlag() {
		return FileFlag;
	}
	/**
	 * @param fileFlag the fileFlag to set
	 */
	public void setFileFlag(String fileFlag) {
		FileFlag = fileFlag;
	}
	/**
	 * @return the srcFilePath
	 */
	public String getSrcFilePath() {
		return SrcFilePath;
	}
	/**
	 * @param srcFilePath the srcFilePath to set
	 */
	public void setSrcFilePath(String srcFilePath) {
		SrcFilePath = srcFilePath;
	}
	/**
	 * @return the srcFileName
	 */
	public String getSrcFileName() {
		return SrcFileName;
	}
	/**
	 * @param srcFileName the srcFileName to set
	 */
	public void setSrcFileName(String srcFileName) {
		SrcFileName = srcFileName;
	}
	/**
	 * @return the destFilePath
	 */
	public String getDestFilePath() {
		return DestFilePath;
	}
	/**
	 * @param destFilePath the destFilePath to set
	 */
	public void setDestFilePath(String destFilePath) {
		DestFilePath = destFilePath;
	}
	/**
	 * @return the destFileName
	 */
	public String getDestFileName() {
		return DestFileName;
	}
	/**
	 * @param destFileName the destFileName to set
	 */
	public void setDestFileName(String destFileName) {
		DestFileName = destFileName;
	}
	
	
}
