package com.ncr.powerswitch.utils;

/***
 *  PowerSwitch全局变量
 * @author rq185015
 */

public class PowerSwitchConstant {
	
	//加密机超时时间， 单位:秒
	public final static String HSM_TIMEOUT = "timeout"; 
	
	//Socket连接错误码 
	public final static String SOCKET_TIMEOUT_ERROR_CODE 							            = "00000000"; //socket超时
	public final static String SOCKET_UNKNOWNHOST_ERROR_CODE 						            = "00000001"; //未知主机
	public final static String SOCKET_CLIENT_CLOSE_ERROR_CODE 						            = "00000002"; //客户端关闭
	public final static String SOCKET_UNKNOWN_ERROR_CODE 							            = "00000098"; //未知错误
	public final static String SOCKET_SUCCESS_CODE 									            = "00000099"; //成功

}
