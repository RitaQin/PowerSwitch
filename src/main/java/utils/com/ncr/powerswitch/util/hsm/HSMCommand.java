package com.ncr.powerswitch.util.hsm;

import java.io.OutputStream;
import java.util.Map;

/**
 * 加密机父类
 * @author rq185015
 *
 */
public abstract class HSMCommand {
	//错误码
	protected int errorCode; 
	protected Map<String, Object> vendorKeyMap; //厂商密钥配置表 
	public StringBuffer commandBuffer = new StringBuffer(); 
	
	//封装消息报文并返回封装好的报文
	abstract public String packageInputField() throws Exception; 
	
	public int getErrorCode() {
		return errorCode; 
	}
	
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode; 
	}
	
	public void setVendorKeyMap(Map<String, Object> keyMap) {
		this.vendorKeyMap = keyMap;
	}


}
