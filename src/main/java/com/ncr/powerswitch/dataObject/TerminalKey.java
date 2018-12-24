package com.ncr.powerswitch.dataObject;

/**
 * TerminalKey POJO¿‡
 * 
 * @author rq185015
 *
 */

public class TerminalKey implements DataObject {

	public String terminalId;
	public String masterKey;
	public String masterKeyCheck;
	public String macKey;
	public String macKeyHsm;
	public String pinKey;
	public String pinKeyHsm;
	public String traceKey;
	public String traceKeyHsm;
	public String keyIndex;

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getMasterKey() {
		return masterKey;
	}

	public void setMasterKey(String masterKey) {
		this.masterKey = masterKey;
	}

	public String getMasterKeyCheck() {
		return masterKeyCheck;
	}

	public void setMasterKeyCheck(String masterKeyCheck) {
		this.masterKeyCheck = masterKeyCheck;
	}

	public String getMacKey() {
		return macKey;
	}

	public void setMacKey(String macKey) {
		this.macKey = macKey;
	}

	public String getMacKeyHsm() {
		return macKeyHsm;
	}

	public void setMacKeyHsm(String macKeyHsm) {
		this.macKeyHsm = macKeyHsm;
	}

	public String getPinKey() {
		return pinKey;
	}

	public void setPinKey(String pinKey) {
		this.pinKey = pinKey;
	}

	public String getPinkeyHsm() {
		return pinKeyHsm;
	}

	public void setPinkeyHsm(String pinkeyHsm) {
		this.pinKeyHsm = pinkeyHsm;
	}

	public String getTraceKey() {
		return traceKey;
	}

	public void setTraceKey(String traceKey) {
		this.traceKey = traceKey;
	}

	public String getTraceKeyHsm() {
		return traceKeyHsm;
	}

	public void setTraceKeyHsm(String traceKeyHsm) {
		this.traceKeyHsm = traceKeyHsm;
	}

	public String getKeyIndex() {
		return keyIndex;
	}

	public void setKeyIndex(String keyIndex) {
		this.keyIndex = keyIndex;
	}
}
