package com.ncr.powerswitch.dataObject;

/**
 * HOSTKEYTABLE POJO¿‡
 * 
 * @author rq185015
 *
 */

public class TerminalKey implements DataObject {

	public String terminalId;
	public String masterKey;
	public String masterKeyCheck;
	public String macKey;
	public String macKeyNew;
	public String pinKey;
	public String pinkeyNew;
	public String traceKey;
	public String traceKeyNew;
	public String keyIndex;
	public String keyIndexNew;

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

	public String getMacKey() {
		return macKey;
	}

	public void setMacKey(String macKey) {
		this.macKey = macKey;
	}

	public String getMacKeyNew() {
		return macKeyNew;
	}

	public void setMacKeyNew(String macKeyNew) {
		this.macKeyNew = macKeyNew;
	}

	public String getPinKey() {
		return pinKey;
	}

	public void setPinKey(String pinKey) {
		this.pinKey = pinKey;
	}

	public String getPinkeyNew() {
		return pinkeyNew;
	}

	public void setPinkeyNew(String pinkeyNew) {
		this.pinkeyNew = pinkeyNew;
	}

	public String getTraceKey() {
		return traceKey;
	}

	public void setTraceKey(String traceKey) {
		this.traceKey = traceKey;
	}

	public String getTraceKeyNew() {
		return traceKeyNew;
	}

	public void setTraceKeyNew(String traceKeyNew) {
		this.traceKeyNew = traceKeyNew;
	}

	public String getKeyIndex() {
		return keyIndex;
	}

	public void setKeyIndex(String keyIndex) {
		this.keyIndex = keyIndex;
	}

	public String getKeyIndexNew() {
		return keyIndexNew;
	}

	public void setKeyIndexNew(String keyIndexNew) {
		this.keyIndexNew = keyIndexNew;
	}
	
	public String getKeyCheck() {
		return masterKeyCheck;
	}

	public void setKeyCheck(String masterKeyCheck) {
		this.masterKeyCheck = masterKeyCheck;
	}

}
