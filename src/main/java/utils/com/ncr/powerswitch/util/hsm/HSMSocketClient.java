package com.ncr.powerswitch.util.hsm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ncr.powerswitch.utils.StringUtil;

import static com.ncr.powerswitch.utils.PowerSwitchConstant.HSM_TIME_OUT;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.SOCKET_CLIENT_CLOSE_ERROR_CODE;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.SOCKET_SUCCESS_CODE;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.SOCKET_TIMEOUT_ERROR_CODE;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.SOCKET_UNKNOWN_ERROR_CODE;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.SOCKET_UNKNOWNHOST_ERROR_CODE;
import static com.ncr.powerswitch.utils.ResourcesTool.IL8N_RESOURCES_DEFAULT;
import static com.ncr.powerswitch.utils.ResourcesTool.getText;

/**
 * 建立与加密机连接辅助类
 * 
 * @author rq185015
 *
 */

public class HSMSocketClient {

	private final static Log log = LogFactory.getLog(HSMSocketClient.class);

	/**
	 * 是否打开客户端
	 */
	private static boolean isOpenClient = true;
	/**
	 * socket服务器IP
	 */
	private static String serverIp = null;

	/**
	 * socket服务器端口号
	 */
	private static int serverPort = 0;
	/**
	 * 接包最大值
	 */
	private static int maxPacket = 1024;

	private static int timeout = Integer.parseInt(getText(IL8N_RESOURCES_DEFAULT, HSM_TIME_OUT)) * 1000;// SOCKET超时时间，默认为20秒
	// private static int timeout =1;
	private static Socket socket;

	private HSMSocketClient(String serverIp_,int serverPort_,boolean isOpenClient_,int maxPacket_) {
		serverIp = serverIp_;
		serverPort = serverPort_;
		isOpenClient = isOpenClient_;
		maxPacket = maxPacket_;
		if(socket == null){
			String result = newInstance(serverIp,serverPort,timeout);
			if(result!=null&&!result.equals(SOCKET_SUCCESS_CODE)){
				socket = null;
			}
		}
	}

	private static String newInstance(String serverIp, int serverPort, int timeout) {
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
			log.error("jdbc.properties,socket.client.open = false. ");
			return SOCKET_CLIENT_CLOSE_ERROR_CODE;
		}
	}

	/**
	 * 发包和接报
	 * 
	 * @param packetContent
	 * @return
	 */
	public synchronized static String sendAndReceivePacket(String msg, String ip, String port, boolean unpack) {
		if (!isOpenClient) {
			return null;
		}
		if (socket == null) {
			String result = newInstance(ip, Integer.parseInt(port), timeout);
			if (result != null && !result.equals(SOCKET_SUCCESS_CODE)) {
				return result;
			}
		}
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			outputStream = socket.getOutputStream();
			byte[] bytelst = StringUtil.ASCII_To_BCD(msg.getBytes(), msg.length());
			outputStream.write(bytelst);
			outputStream.flush();

			inputStream = socket.getInputStream();
			byte[] bytes = new byte[maxPacket];
			int n = inputStream.read(bytes);
			if (!unpack) {
				return StringUtil.bcd2Str(bytes, n);
			} else {
				String retmsg = StringUtil.bcd2Str(bytes, n);
				if (retmsg != null) {
					byte[] bs = StringUtil.ASCII_To_BCD(retmsg.getBytes(), retmsg.length());
					if (bs != null) {
						String retmsg_unpack = "";
						for (int i = 0; i < bs.length; i++) {
							retmsg_unpack += StringUtil.asc_to_bcd(bs[i]);
						}
					}
				}
			}
		} catch (IOException e) {
			log.error("socket client receive packet error, message is" + e.getMessage() + ".");
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					log.error(
							"socket client receive packet,InputStream close error, message is" + e.getMessage() + ".");
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					log.error(
							"socket client receive packet,outputStream close error, message is" + e.getMessage() + ".");
				}
			}
			if (socket != null) {
				try {
					socket.close();
					socket = null;
					// if(socket == null){
					// newInstance(serverIp,serverPort,timeout);
					// }
				} catch (IOException e) {
					log.error("socket client receive packet,socket close error, message is" + e.getMessage() + ".");
				}
			}
		}
		return SOCKET_UNKNOWN_ERROR_CODE;
	}

	public static String getServerIp() {
		return serverIp;
	}

	public static int getServerPort() {
		return serverPort;
	}
}
