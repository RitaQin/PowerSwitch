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

/**
 * 建立与加密机连接辅助类
 * 
 * @author rq185015
 *
 */

public class HSMSocketClient {
	
	/**
	 * Log4j记录日志的工具类
	 */
	private final static Log log = LogFactory.getLog(HSMSocketClient.class);

	// 加密机IP地址
	private static String host;
	// 加密机端口
	private static int port;
	// 客户端开启状态
	private static boolean isOpenClient = true;
	// 最大接包数量
	private static int maxPacket = 1024;
	// 加密机超时
	private static int timeout = Integer.parseInt(HSM_TIME_OUT);

	private static Socket socket = null;

	private HSMSocketClient(String host_, int port_, boolean isOpenClient_, int maxPacket_) {
		host = host_;
		port = port_;
		isOpenClient = isOpenClient_;
		maxPacket = maxPacket_;
		if (socket == null) {
			String socketResCode = newInstance(host, port, timeout);
			if (socketResCode != null && !socketResCode.equals(SOCKET_SUCCESS_CODE)) {
				socket = null;
			}
		}
	}

	private static String newInstance(String host, int port, int timeout) {
		if (isOpenClient) {
			log.info("TCP Socket initializing..");
			try {
				socket = new Socket(host, port);
				socket.setSoTimeout(timeout);
				log.info("Socket host: " + host + " port: " + port);
				return SOCKET_SUCCESS_CODE;
			} catch (UnknownHostException he) {
				log.error("Socket initialization failed, unknown host exception, host: " + host);
				return SOCKET_UNKNOWNHOST_ERROR_CODE;
			} catch (SocketTimeoutException te) {
				log.error("Socket timeout exception");
				return SOCKET_TIMEOUT_ERROR_CODE;
			} catch (IOException e) {
				log.error("Socket unknown error occurred!");
				return SOCKET_UNKNOWN_ERROR_CODE;
			}
		} else {
			return SOCKET_CLIENT_CLOSE_ERROR_CODE;
		}
	}
	
	// 执行加密机指令
	public synchronized static String execute(HSMCommand command, boolean unpack, Map<String, String> inputMap) throws Exception {
		if(!isOpenClient) {
			return null; 
		} if (socket == null) {
			String newIns = newInstance(host, port, timeout); 
			if (newIns != null && !newIns.equals(SOCKET_SUCCESS_CODE)) {
				return newIns;
			}
		}
		
		OutputStream os = null;
		InputStream is = null;
		try {
			// 封装报文消息
			os = socket.getOutputStream();
			String message = command.packageInputField(os, inputMap);
			byte[] bytelst=StringUtil.ASCII_To_BCD(message.getBytes(),message.length());
			os.write(bytelst);
			os.flush();

			// 从加密机端接收响应消息
			is = socket.getInputStream();
			byte[] bytes = new byte[maxPacket];
			int n = is.read(bytes);
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
		} finally {
			// 切断连接
			try {
				if (os != null) {
					os.close();
					os = null;
				}
				if (is != null) {
					is.close();
					is = null;
				}
				if (socket != null) {
					socket.close();
					socket = null;
				}
			} catch (Exception ex) {
				// TODO: Log exceptions and return error code
			}
		}
		return SOCKET_UNKNOWN_ERROR_CODE;
	}

	public static String getHost() {
		return host;
	}

	public static int getPort() {
		return port;
	}

}
