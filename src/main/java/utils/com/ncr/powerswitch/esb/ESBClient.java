package com.ncr.powerswitch.esb;

import static com.ncr.powerswitch.utils.PowerSwitchConstant.HSM_TIME_OUT;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.SOCKET_CLIENT_CLOSE_ERROR_CODE;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.SOCKET_SUCCESS_CODE;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.SOCKET_TIMEOUT_ERROR_CODE;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.SOCKET_UNKNOWNHOST_ERROR_CODE;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.SOCKET_UNKNOWN_ERROR_CODE;
import static com.ncr.powerswitch.utils.ResourcesTool.IL8N_RESOURCES_DEFAULT;
import static com.ncr.powerswitch.utils.ResourcesTool.getText;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ncr.powerswitch.utils.FormatUtil;

/**
 * 与ESB交互接口客户端 
 * TCP短连接和报文发送
 * @author rq185015
 *
 */

public abstract class ESBClient {
	
	public final static Log log = LogFactory.getLog(ESBClient.class);
	
	public static boolean isOpenClient = true;
	
	/**
	 * socket服务器IP
	 */
	public static String serverIp = null;
	
	/**
	 * socket服务器端口号
	 */
	public static int serverPort = 0;
	
	/**
	 * 接包最大值
	 */
	public static int maxPacket = 1024;

	public static int timeout = Integer.parseInt(getText(IL8N_RESOURCES_DEFAULT, HSM_TIME_OUT)) * 1000;// SOCKET超时时间，默认为20秒
	
	public static Socket socket;

	public ESBClient(String serverIp_, int serverPort_, boolean isOpenClient_, int maxPacket_) {
		ESBClient.serverIp = serverIp_;
		ESBClient.serverPort = serverPort_;
		ESBClient.isOpenClient = isOpenClient_;
		ESBClient.maxPacket = maxPacket_;
		if(socket == null){
			String result = newInstance(serverIp,serverPort,timeout);
			if(result!=null&&!result.equals(SOCKET_SUCCESS_CODE)){
				socket = null;
			}
		}
	}
	
	public String newInstance(String ip, int port, int timeout) {
		if (isOpenClient) {
			log.debug("init socket client....");
			log.debug("socket server ip:" + serverIp);
			log.debug("socket server port:" + serverPort);
			try {
				socket = new Socket(serverIp, serverPort);
				log.debug("init socket client success.");
				return SOCKET_SUCCESS_CODE;
			} catch (UnknownHostException e) {
				log.error("init socket client error,message is " + e.getMessage() + ".");
				return SOCKET_UNKNOWNHOST_ERROR_CODE;
			} catch (SocketTimeoutException e) {
				log.error("init socket client error,message is " + e.getMessage() + ".");
				return SOCKET_TIMEOUT_ERROR_CODE;
			} catch (IOException e) {
				log.error("init socket client error,message is " + e.getMessage() + ".");
				return SOCKET_UNKNOWN_ERROR_CODE;
			}
		} else {
			log.error("socket.client.open = false. ");
			return SOCKET_CLIENT_CLOSE_ERROR_CODE;
		}
	}
	
	/**
	 * 生成对应接口报文
	 * 
	 * @param 请求信息map
	 * @return xml报文
	 */
	
	public abstract String constructRequest(Map<String, String> requestMap);
	
	/**
	 * 发送和接收ESB接口报文
	 * 
	 * @param msg  终端号
	 * @param ip  ip地址
	 * @param port   端口号
	 * @param unpack  
	 * @return 返回报文
	 */
	
	public abstract String sendAndReceivePackets(String msg, String ip, String port, boolean unpack);
	
	/**
	 * 验证由esb返回的报文
	 * 
	 * @param 向esb发送的请求
	 * @param esb回复的报文
	 * @param 需要验证的域
	 * @return 验证结果 boolean
	 */
	public boolean verifyResMessage(String msg, String receivedMsg, String[] verifyFields) {
		Map<String, String> sendMap = FormatUtil.xml2Map(msg);
		if (sendMap == null || sendMap.size() == 0) {
			return false;
		}
		Map<String, String> receivedMap = FormatUtil.xml2Map(receivedMsg);
		if (receivedMap == null || receivedMap.size() == 0) {
			return false;
		}
		for (int i = 0; i < verifyFields.length; i++) {
			String key = verifyFields[i];
			if (!sendMap.get(key).equals(receivedMap.get(key))) {
				return false;
			}
		}
		return true;
	}

}
