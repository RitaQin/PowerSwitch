package com.ncr.powerswitch.processor;

import static com.ncr.powerswitch.utils.ResourcesTool.IL8N_REMOTEKEYLOAD_DEFAULT;
import static com.ncr.powerswitch.utils.ResourcesTool.IL8N_RESOURCES_DEFAULT;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.GLOBAL_ERROR_CODE_PARAMS_NOT_NULL;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.GLOBAL_ERROR_CODE_TERMINAL_ID_INVALID;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.GLOBAL_ERROR_CODE_TERMID_ILLEGAL;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.GLOBAL_ERROR_CODE_TERMID_IP_FALSE;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.REMOTEKEYLOAD_ERROR_PK_DATA_ILLEGAL;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.REMOTEKEYLOAD_ERROR_SIGN_DATA_ILLEGAL;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.REMOTEKEYLOAD_ERROR_UPTATE_EPPID;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.REMOTEKEYLOAD_ERROR_EPPID_COMPARE_FALSE;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.REMOTEKEYLOAD_ERROR_CODE_HSMSET_ERROR;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.REMOTEKEYLOAD_ERROR_MANU_PK_DATA;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.REMOTEKEYLOAD_ERROR_MANU_PK_DATA_LENGTH;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.REMOTEKEYLOAD_ERROR_CODE_HSM_DATA_ERROR;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.SOCKET_TIMEOUT_ERROR_CODE;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.SOCKET_UNKNOWNHOST_ERROR_CODE;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.SOCKET_CLIENT_CLOSE_ERROR_CODE;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.SOCKET_UNKNOWN_ERROR_CODE;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.REMOTEKEYLOAD_INFO_CODE_VAL_SIGN_FALSE;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.GLOBAL_ERROR_CODE_SOCKET_ERROR;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.GLOBAL_INFO_CODE_UPDATE_ATM_KEY_FALSE;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.GLOBAL_INFO_CODE_SOCKET_RETURN_DATA_FALSE;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.REMOTEKEYLOAD_ERROR_ATM_PK_DATA;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.REMOTEKEYLOAD_ERROR_SK__DATA;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.REMOTEKEYLOAD_ERROR_BANK_PK_DATA;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.REMOTEKEYLOAD_ERROR_BANK_PK_MANU_SIGN_DATA;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.REMOTEKEYLOAD_INFO_CODE_VAL_SIGN_SUCCESS;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.GLOBAL_INFO_CODE_SOCKET_RETURN_DATA_SUCCESS;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.GLOBAL_INFO_CODE_UPDATE_ATM_KEY_SUCCESS;

import static com.ncr.powerswitch.utils.PowerSwitchConstant.VAL_OPEN;
import static com.ncr.powerswitch.utils.ResourcesTool.getText;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ncr.powerswitch.hsm.HSMSocketClient;
import com.ncr.powerswitch.utils.FormatUtil;
import com.ncr.powerswitch.utils.ReturnMsgUtil;
import com.ncr.powerswitch.utils.StringUtil;

import com.ncr.powerswitch.hsm.HSMCommand;
import com.ncr.powerswitch.hsm.HSMCommand_C046;
import com.ncr.powerswitch.hsm.HSMCommand_C047;
import com.ncr.powerswitch.hsm.HSMCommand_C049;
import com.ncr.powerswitch.hsm.HSMCommand_D106;
import com.ncr.powerswitch.DAO.RemoteDAO;

/***
 * 远程密钥下载处理器 Remote Key Loading Using Signatures
 * 
 * 处理流程
 * 
 * 1. 主机接收终端发送的PK以及相关安全验证签名
 * 
 * 2. 主机接收终端发送的终端序列号以及相关安全验证签名
 * 
 * 3. 主机向加密机发送命令验证签名(C047)
 * 
 * 4. 签名验证成功后，主机向加密机发送命令产生随机工作密钥(D106)
 * 
 * 5. 主机向加密机发送命令将产生的工作密钥通过终端银行私钥进行加密(C049)
 * 
 * 6. 转加密成功后，主机向加密机发送命令执行密文签名（C046）
 * 
 * 7. 主机将加密后的工作密钥和签名返回终端
 * 
 * @author rq185015
 *
 */

public class RKLProcessor implements BaseProcessor {

	/**
	 * Log4j记录日志的工具类
	 */
	private final static Log log = LogFactory.getLog(RKLProcessor.class);
	public RemoteDAO remoteDao;
	public static String DEFAULT_USER_RESERVED_STR = "0000000000000000"; // 默认用户保留字

	@Override
	public void process(Exchange exchange) throws Exception {

		String msg = exchange.getIn(String.class);
		Map<String, Object> requestMap = FormatUtil.json2Map(msg);
		log.info("RKL PROCESSING : request msg: " + msg);

		// 从请求报文中提取远程密钥下载所需参数
		String ip = requestMap.get("ip") != null ? requestMap.get("ip").toString() : null; // IP地址
		String strTerminalID = requestMap.get("TerminalID") != null ? requestMap.get("TerminalID").toString() : null; // 终端号 
		String strEppSerialNo = requestMap.get("EppSerialNo") != null ? requestMap.get("EppSerialNo").toString() : null; // EPP序列号
		String strEppSerialNoSign = requestMap.get("EppSerialNoSign") != null
				? requestMap.get("EppSerialNoSign").toString()
				: null; // EPP序列号签名
		String strEppPublicKey = requestMap.get("EppPublicKey") != null ? requestMap.get("EppPublicKey").toString()
				: null; // 存储在EPP里面的银行公钥
		String strEppPublicKeySign = requestMap.get("EppPublicKeySign") != null
				? requestMap.get("EppPublicKeySign").toString()
				: null; // 存储在EPP里面的银行公钥签名
		// 验证参数有效性开始
		String errorMsg = null;
		if (StringUtil.isNull(strTerminalID)) {
			log.info(getText(
					IL8N_RESOURCES_DEFAULT, GLOBAL_ERROR_CODE_PARAMS_NOT_NULL, new Object[] { "strTerminalID" }));
			errorMsg = ReturnMsgUtil.generateErrorMessage(GLOBAL_ERROR_CODE_PARAMS_NOT_NULL, getText(
					IL8N_RESOURCES_DEFAULT, GLOBAL_ERROR_CODE_PARAMS_NOT_NULL, new Object[] { "strTerminalID" }));
			
			exchange.getOut().setBody(generateErrorJson(requestMap, GLOBAL_ERROR_CODE_PARAMS_NOT_NULL, errorMsg));
			return;
		}
		// 验证终端号长度范围
		else if (strTerminalID.length() < 1 || strTerminalID.length() > 16) {
			log.info(getText(
					IL8N_RESOURCES_DEFAULT, GLOBAL_ERROR_CODE_TERMINAL_ID_INVALID, new Object[] { "strTerminalID" }));
			errorMsg = ReturnMsgUtil.generateErrorMessage(GLOBAL_ERROR_CODE_TERMINAL_ID_INVALID, getText(
					IL8N_RESOURCES_DEFAULT, GLOBAL_ERROR_CODE_TERMINAL_ID_INVALID, new Object[] { "strTerminalID" }));
			
			exchange.getOut().setBody(generateErrorJson(requestMap, GLOBAL_ERROR_CODE_TERMINAL_ID_INVALID, errorMsg));
			return;
		}
		// 验证端机号合法性
		else if (getText(IL8N_RESOURCES_DEFAULT, VAL_OPEN).equals("true")) {
			String result = valTermIDAndIp(strTerminalID, ip, IL8N_RESOURCES_DEFAULT, GLOBAL_ERROR_CODE_TERMID_ILLEGAL,
					GLOBAL_ERROR_CODE_TERMID_IP_FALSE);
			if (result != null) {
				log.info(result);
				exchange.getOut().setBody(generateErrorJson(requestMap, GLOBAL_ERROR_CODE_TERMID_ILLEGAL, result));
				return;
			}
		}
		// 验证EPP序列号是否为空
		else if (StringUtil.isNull(strEppSerialNo)) {
			log.info(getText(
					IL8N_RESOURCES_DEFAULT, GLOBAL_ERROR_CODE_PARAMS_NOT_NULL, new Object[] { "strEppSerialNo" }));
			errorMsg = ReturnMsgUtil.generateErrorMessage(GLOBAL_ERROR_CODE_PARAMS_NOT_NULL, getText(
					IL8N_RESOURCES_DEFAULT, GLOBAL_ERROR_CODE_PARAMS_NOT_NULL, new Object[] { "strEppSerialNo" }));
			exchange.getOut().setBody(generateErrorJson(requestMap, GLOBAL_ERROR_CODE_PARAMS_NOT_NULL, errorMsg));
			return;
		}
		// 验证存储在EPP里面的银行公钥是否为空
		else if (StringUtil.isNull(strEppPublicKey)) {
			log.info(getText(
					IL8N_RESOURCES_DEFAULT, GLOBAL_ERROR_CODE_PARAMS_NOT_NULL, new Object[] { "strEppPublicKey" }));
			errorMsg = ReturnMsgUtil.generateErrorMessage(GLOBAL_ERROR_CODE_PARAMS_NOT_NULL, getText(
					IL8N_RESOURCES_DEFAULT, GLOBAL_ERROR_CODE_PARAMS_NOT_NULL, new Object[] { "strEppPublicKey" }));
			exchange.getOut().setBody(generateErrorJson(requestMap, GLOBAL_ERROR_CODE_PARAMS_NOT_NULL, errorMsg));
			return;
		}
		// 验证存储在EPP里面的银行公钥签名是否为空
		else if (StringUtil.isNull(strEppPublicKeySign)) {
			log.info(getText(
					IL8N_RESOURCES_DEFAULT, GLOBAL_ERROR_CODE_PARAMS_NOT_NULL, new Object[] { "strEppPublicKeySign" }));
			errorMsg = ReturnMsgUtil.generateErrorMessage(GLOBAL_ERROR_CODE_PARAMS_NOT_NULL, getText(
					IL8N_RESOURCES_DEFAULT, GLOBAL_ERROR_CODE_PARAMS_NOT_NULL, new Object[] { "strEppPublicKeySign" }));
			exchange.getOut().setBody(generateErrorJson(requestMap, GLOBAL_ERROR_CODE_PARAMS_NOT_NULL, errorMsg));
			return;
		}
		// 验证存储在EPP里面的银行公钥长度
		else if (strEppPublicKey.length() != 540) {
			log.info(getText(IL8N_REMOTEKEYLOAD_DEFAULT, REMOTEKEYLOAD_ERROR_PK_DATA_ILLEGAL));
			errorMsg = ReturnMsgUtil.generateErrorMessage(REMOTEKEYLOAD_ERROR_PK_DATA_ILLEGAL,
					getText(IL8N_REMOTEKEYLOAD_DEFAULT, REMOTEKEYLOAD_ERROR_PK_DATA_ILLEGAL));
			exchange.getOut().setBody(generateErrorJson(requestMap, REMOTEKEYLOAD_ERROR_PK_DATA_ILLEGAL, errorMsg));
			return;
		}
		// 验证存储在EPP里面的银行公钥长度
		else if (strEppPublicKeySign.length() != 512) {
			log.info(getText(IL8N_REMOTEKEYLOAD_DEFAULT, REMOTEKEYLOAD_ERROR_SIGN_DATA_ILLEGAL));
			errorMsg = ReturnMsgUtil.generateErrorMessage(REMOTEKEYLOAD_ERROR_SIGN_DATA_ILLEGAL,
					getText(IL8N_REMOTEKEYLOAD_DEFAULT, REMOTEKEYLOAD_ERROR_SIGN_DATA_ILLEGAL));
			exchange.getOut().setBody(generateErrorJson(requestMap, REMOTEKEYLOAD_ERROR_SIGN_DATA_ILLEGAL, errorMsg));
			return;
		}
		// 验证参数有效性结束......
		// 将EPP序列号更新 EPPID
		int cnt = remoteDao.updateATMEPPID(strTerminalID, strEppSerialNo);
		if (cnt == 0) {
			log.info(getText(IL8N_REMOTEKEYLOAD_DEFAULT, REMOTEKEYLOAD_ERROR_UPTATE_EPPID));
			errorMsg = ReturnMsgUtil.generateErrorMessage(REMOTEKEYLOAD_ERROR_UPTATE_EPPID,
					getText(IL8N_REMOTEKEYLOAD_DEFAULT, REMOTEKEYLOAD_ERROR_UPTATE_EPPID));
			exchange.getOut().setBody(generateErrorJson(requestMap, REMOTEKEYLOAD_ERROR_UPTATE_EPPID, errorMsg));
			return;
		} else if (cnt == 99) {
			log.info(getText(IL8N_REMOTEKEYLOAD_DEFAULT, REMOTEKEYLOAD_ERROR_EPPID_COMPARE_FALSE));
			errorMsg = ReturnMsgUtil.generateErrorMessage(REMOTEKEYLOAD_ERROR_EPPID_COMPARE_FALSE,
					getText(IL8N_REMOTEKEYLOAD_DEFAULT, REMOTEKEYLOAD_ERROR_EPPID_COMPARE_FALSE));
			exchange.getOut().setBody(generateErrorJson(requestMap, REMOTEKEYLOAD_ERROR_EPPID_COMPARE_FALSE, errorMsg));
			return;
		}
		// 获取加密机的配置参数
		Map<String, Object> mapHsm;
		boolean signVerified = false;
		Map<String, Object> resultMap = new HashMap<String, Object>();// 保存返回的数据

		try {
			mapHsm = remoteDao.findHSMSETAll();
		} catch (Exception e) {
			// 加密机获取数据失败
			e.printStackTrace();
			log.info(getText(IL8N_REMOTEKEYLOAD_DEFAULT, REMOTEKEYLOAD_ERROR_CODE_HSMSET_ERROR));
			errorMsg = ReturnMsgUtil.generateErrorMessage(REMOTEKEYLOAD_ERROR_CODE_HSMSET_ERROR,
					getText(IL8N_REMOTEKEYLOAD_DEFAULT, REMOTEKEYLOAD_ERROR_CODE_HSMSET_ERROR));
			exchange.getOut().setBody(generateErrorJson(requestMap, REMOTEKEYLOAD_ERROR_CODE_HSMSET_ERROR, errorMsg));
			return;
		}
		// 加密机配置参数获取失败参数
		if (mapHsm == null) {
			log.info(getText(IL8N_REMOTEKEYLOAD_DEFAULT, REMOTEKEYLOAD_ERROR_CODE_HSMSET_ERROR));
			errorMsg = ReturnMsgUtil.generateErrorMessage(REMOTEKEYLOAD_ERROR_CODE_HSMSET_ERROR,
					getText(IL8N_REMOTEKEYLOAD_DEFAULT, REMOTEKEYLOAD_ERROR_CODE_HSMSET_ERROR));
			exchange.getOut().setBody(generateErrorJson(requestMap, REMOTEKEYLOAD_ERROR_CODE_HSMSET_ERROR, errorMsg));
			return;
		} else if (StringUtil.isNull(mapHsm.get("IP").toString()) || StringUtil.isNull(mapHsm.get("PORT").toString())) {
			log.info(getText(IL8N_REMOTEKEYLOAD_DEFAULT, REMOTEKEYLOAD_ERROR_CODE_HSMSET_ERROR));
			errorMsg = ReturnMsgUtil.generateErrorMessage(REMOTEKEYLOAD_ERROR_CODE_HSMSET_ERROR,
					getText(IL8N_REMOTEKEYLOAD_DEFAULT, REMOTEKEYLOAD_ERROR_CODE_HSMSET_ERROR));
			exchange.getOut().setBody(generateErrorJson(requestMap, REMOTEKEYLOAD_ERROR_CODE_HSMSET_ERROR, errorMsg));
			return;
		}
		// 获取加密机配置参数
		String ip_hsm = mapHsm.get("IP").toString(); // 加密机IP
		String port_hsm = mapHsm.get("PORT").toString();// 加密机端口
		String rtmsg = ""; // 保存加密机返回数据

		// 发送命令<0XC047> 验证签名
		Map<String, Object> mapmf = null;// 保存厂商密钥配置表
		try {
			// 获取端机厂商密钥配置 *查询语句返回数据 字段对应 待确定
			mapmf = remoteDao.findManufacturerByIPAndTerminalID(ip, strTerminalID);
			if (mapmf != null) {
				// 正确获取厂商密钥配置
				// 组成命令通信加密机
				String userReservedStr = null;
				Map<String, String> inputMap = new HashMap<String, String>();
				// 获取用户保留字
				if (mapmf.get("R1") != null && mapmf.get("R1").toString().length() == 16)
					userReservedStr = mapmf.get("R1").toString();
				else
					userReservedStr = DEFAULT_USER_RESERVED_STR;

				inputMap.put("userReservedStr", userReservedStr);

				// 验证厂商公钥长度
				if (mapmf.get("MANUPKLENGTH") != null) {
					inputMap.put("MANUPKLENGTH", mapmf.get("MANUPKLENGTH").toString());
				} else {
					log.info(getText(IL8N_REMOTEKEYLOAD_DEFAULT, REMOTEKEYLOAD_ERROR_MANU_PK_DATA_LENGTH));
					errorMsg = ReturnMsgUtil.generateErrorMessage(REMOTEKEYLOAD_ERROR_MANU_PK_DATA_LENGTH,
							getText(IL8N_REMOTEKEYLOAD_DEFAULT, REMOTEKEYLOAD_ERROR_MANU_PK_DATA_LENGTH));
					exchange.getOut().setBody(generateErrorJson(requestMap, REMOTEKEYLOAD_ERROR_MANU_PK_DATA_LENGTH, errorMsg));
					return;
				}
				// 验证厂商公钥
				if (mapmf.get("MANUPK") != null && mapmf.get("MANUPK").toString().trim().length() == 540) {
					inputMap.put("MANUPK", mapmf.get("MANUPK").toString().trim());// 公钥长度
				} else {
					log.info(getText(IL8N_REMOTEKEYLOAD_DEFAULT, REMOTEKEYLOAD_ERROR_MANU_PK_DATA));
					errorMsg = ReturnMsgUtil.generateErrorMessage(REMOTEKEYLOAD_ERROR_MANU_PK_DATA,
							getText(IL8N_REMOTEKEYLOAD_DEFAULT, REMOTEKEYLOAD_ERROR_MANU_PK_DATA));
					exchange.getOut().setBody(generateErrorJson(requestMap, REMOTEKEYLOAD_ERROR_MANU_PK_DATA, errorMsg));
					return;
				}
				// 封装命令报文
				HSMCommand c047 = new HSMCommand_C047(inputMap);
				String c047Msg = c047.packageInputField();
				try {
					rtmsg = HSMSocketClient.sendAndReceivePacket(c047Msg.trim(), ip_hsm, port_hsm, false);
					log.info("命令："+ c047Msg +",加密机返回数据："+rtmsg);
					if (rtmsg != null) {
						if (rtmsg.length() == 20 && rtmsg.substring(0, 2).equals("41")
								&& rtmsg.substring(18, 20).equals("00")) {
							// 验证签名成功
							log.info(getText(IL8N_REMOTEKEYLOAD_DEFAULT, REMOTEKEYLOAD_INFO_CODE_VAL_SIGN_SUCCESS));
							signVerified = true;
						} else if (rtmsg.equals(SOCKET_TIMEOUT_ERROR_CODE)) {
							log.info(getText(IL8N_REMOTEKEYLOAD_DEFAULT, SOCKET_TIMEOUT_ERROR_CODE));
							errorMsg = ReturnMsgUtil.generateErrorMessage(SOCKET_TIMEOUT_ERROR_CODE,
									getText(IL8N_REMOTEKEYLOAD_DEFAULT, SOCKET_TIMEOUT_ERROR_CODE));
							exchange.getOut().setBody(generateErrorJson(requestMap, SOCKET_TIMEOUT_ERROR_CODE, errorMsg));
							return;
						} else if (rtmsg.equals(SOCKET_UNKNOWNHOST_ERROR_CODE)) {
							log.info(getText(IL8N_REMOTEKEYLOAD_DEFAULT, SOCKET_UNKNOWNHOST_ERROR_CODE));
							errorMsg = ReturnMsgUtil.generateErrorMessage(SOCKET_UNKNOWNHOST_ERROR_CODE,
									getText(IL8N_REMOTEKEYLOAD_DEFAULT, SOCKET_UNKNOWNHOST_ERROR_CODE));
							exchange.getOut().setBody(generateErrorJson(requestMap, SOCKET_UNKNOWNHOST_ERROR_CODE, errorMsg));
							return;
						} else if (rtmsg.equals(SOCKET_CLIENT_CLOSE_ERROR_CODE)) {
							log.info(getText(IL8N_REMOTEKEYLOAD_DEFAULT, SOCKET_CLIENT_CLOSE_ERROR_CODE));
							errorMsg = ReturnMsgUtil.generateErrorMessage(SOCKET_CLIENT_CLOSE_ERROR_CODE,
									getText(IL8N_REMOTEKEYLOAD_DEFAULT, SOCKET_CLIENT_CLOSE_ERROR_CODE));
							exchange.getOut().setBody(generateErrorJson(requestMap, SOCKET_CLIENT_CLOSE_ERROR_CODE, errorMsg));
							return;
						} else if (rtmsg.equals(SOCKET_UNKNOWN_ERROR_CODE)) {
							log.info(getText(IL8N_REMOTEKEYLOAD_DEFAULT, SOCKET_UNKNOWN_ERROR_CODE));
							errorMsg = ReturnMsgUtil.generateErrorMessage(SOCKET_UNKNOWN_ERROR_CODE,
									getText(IL8N_REMOTEKEYLOAD_DEFAULT, SOCKET_UNKNOWN_ERROR_CODE));
							exchange.getOut().setBody(generateErrorJson(requestMap, SOCKET_UNKNOWN_ERROR_CODE, errorMsg));
							return;
						} else {
							// 验证签名失败
							log.info(getText(IL8N_REMOTEKEYLOAD_DEFAULT, REMOTEKEYLOAD_INFO_CODE_VAL_SIGN_FALSE));
							errorMsg = ReturnMsgUtil.generateErrorMessage(REMOTEKEYLOAD_INFO_CODE_VAL_SIGN_FALSE,
									getText(IL8N_REMOTEKEYLOAD_DEFAULT, REMOTEKEYLOAD_INFO_CODE_VAL_SIGN_FALSE));
							exchange.getOut().setBody(generateErrorJson(requestMap, REMOTEKEYLOAD_INFO_CODE_VAL_SIGN_FALSE, errorMsg));
							return;
						}
					}
				} catch (Exception e) {
					// 通信异常
					e.printStackTrace();
					log.error(getText(IL8N_REMOTEKEYLOAD_DEFAULT, GLOBAL_ERROR_CODE_SOCKET_ERROR));
					errorMsg = ReturnMsgUtil.generateErrorMessage(GLOBAL_ERROR_CODE_SOCKET_ERROR,
							getText(IL8N_REMOTEKEYLOAD_DEFAULT, GLOBAL_ERROR_CODE_SOCKET_ERROR));
					exchange.getOut().setBody(generateErrorJson(requestMap, GLOBAL_ERROR_CODE_SOCKET_ERROR, errorMsg));
					return;
				}

			} else {
				log.info(getText(IL8N_REMOTEKEYLOAD_DEFAULT, REMOTEKEYLOAD_ERROR_CODE_HSM_DATA_ERROR));
				errorMsg = ReturnMsgUtil.generateErrorMessage(REMOTEKEYLOAD_ERROR_CODE_HSM_DATA_ERROR,
						getText(IL8N_REMOTEKEYLOAD_DEFAULT, REMOTEKEYLOAD_ERROR_CODE_HSM_DATA_ERROR));
				exchange.getOut().setBody(generateErrorJson(requestMap, REMOTEKEYLOAD_ERROR_CODE_HSM_DATA_ERROR, errorMsg));
				return;
			}
		} catch (Exception e1) {
			// 数据库获取数据异常
			e1.printStackTrace();
			log.error(getText(IL8N_REMOTEKEYLOAD_DEFAULT, REMOTEKEYLOAD_ERROR_CODE_HSM_DATA_ERROR));
			errorMsg = ReturnMsgUtil.generateErrorMessage(REMOTEKEYLOAD_ERROR_CODE_HSM_DATA_ERROR,
					getText(IL8N_REMOTEKEYLOAD_DEFAULT, REMOTEKEYLOAD_ERROR_CODE_HSM_DATA_ERROR));
			exchange.getOut().setBody(generateErrorJson(requestMap, REMOTEKEYLOAD_ERROR_CODE_HSM_DATA_ERROR, errorMsg));
			return;
		} // C047命令发送结束

		// 如果签名验证成功，产生随机工作密钥(D106)
		if (signVerified) {
			signVerified = false;
			HSMCommand d106 = new HSMCommand_D106();
			String d106Msg = d106.packageInputField();
			try {
				// 开始通信加密机
				rtmsg = HSMSocketClient.sendAndReceivePacket(d106Msg.trim(), ip_hsm, port_hsm, false);
				log.info("命令：" + d106Msg.trim() + ",加密机返回数据：" + rtmsg);
				// 返回有效数据，定长 52(除错误外),前两位为应答码 41：正确 45：错误
				if (rtmsg != null) {
					if (rtmsg.length() == 52 && rtmsg.substring(0, 2).equals("41")) {
						// 加密机返回正确数据,并获取 数据密钥（32位），数据密钥校验码（16位）
						log.info(getText(IL8N_REMOTEKEYLOAD_DEFAULT, GLOBAL_INFO_CODE_SOCKET_RETURN_DATA_SUCCESS));
						String keyText = rtmsg.substring(4, 36);// 获取数据密钥
						String keyCheck = rtmsg.substring(36, 52);// 获取数据密钥校验码
						// 将数据密钥与数据密钥校验码更新到ATM
						if (remoteDao.updateATMKEY(strTerminalID, ip, keyText, keyCheck)) {
							resultMap.put("keyCheck", keyCheck);
							// 更新成功
							log.info(getText(IL8N_REMOTEKEYLOAD_DEFAULT, GLOBAL_INFO_CODE_UPDATE_ATM_KEY_SUCCESS));
							signVerified = true;
						} else {
							// 更新失败
							log.info(getText(IL8N_REMOTEKEYLOAD_DEFAULT, GLOBAL_INFO_CODE_UPDATE_ATM_KEY_FALSE));
							errorMsg = ReturnMsgUtil.generateErrorMessage(GLOBAL_INFO_CODE_UPDATE_ATM_KEY_FALSE,
									getText(IL8N_REMOTEKEYLOAD_DEFAULT, GLOBAL_INFO_CODE_UPDATE_ATM_KEY_FALSE));
							exchange.getOut().setBody(generateErrorJson(requestMap, GLOBAL_INFO_CODE_UPDATE_ATM_KEY_FALSE, errorMsg));
							return;
						}
					} else if (rtmsg.equals(SOCKET_TIMEOUT_ERROR_CODE)) {
						log.info(getText(IL8N_REMOTEKEYLOAD_DEFAULT, SOCKET_TIMEOUT_ERROR_CODE));
						errorMsg = ReturnMsgUtil.generateErrorMessage(SOCKET_TIMEOUT_ERROR_CODE,
								getText(IL8N_REMOTEKEYLOAD_DEFAULT, SOCKET_TIMEOUT_ERROR_CODE));
						exchange.getOut().setBody(generateErrorJson(requestMap, SOCKET_TIMEOUT_ERROR_CODE, errorMsg));
						return;
					} else if (rtmsg.equals(SOCKET_UNKNOWNHOST_ERROR_CODE)) {
						log.info(getText(IL8N_REMOTEKEYLOAD_DEFAULT, SOCKET_UNKNOWNHOST_ERROR_CODE));
						errorMsg = ReturnMsgUtil.generateErrorMessage(SOCKET_UNKNOWNHOST_ERROR_CODE,
								getText(IL8N_REMOTEKEYLOAD_DEFAULT, SOCKET_UNKNOWNHOST_ERROR_CODE));
						exchange.getOut().setBody(generateErrorJson(requestMap, SOCKET_UNKNOWNHOST_ERROR_CODE, errorMsg));
						return;
					} else if (rtmsg.equals(SOCKET_CLIENT_CLOSE_ERROR_CODE)) {
						log.info(getText(IL8N_REMOTEKEYLOAD_DEFAULT, SOCKET_CLIENT_CLOSE_ERROR_CODE));
						errorMsg = ReturnMsgUtil.generateErrorMessage(SOCKET_CLIENT_CLOSE_ERROR_CODE,
								getText(IL8N_REMOTEKEYLOAD_DEFAULT, SOCKET_CLIENT_CLOSE_ERROR_CODE));
						exchange.getOut().setBody(generateErrorJson(requestMap, SOCKET_CLIENT_CLOSE_ERROR_CODE, errorMsg));
						return;
					} else if (rtmsg.equals(SOCKET_UNKNOWN_ERROR_CODE)) {
						log.info(getText(IL8N_REMOTEKEYLOAD_DEFAULT, SOCKET_UNKNOWN_ERROR_CODE));
						errorMsg = ReturnMsgUtil.generateErrorMessage(SOCKET_UNKNOWN_ERROR_CODE,
								getText(IL8N_REMOTEKEYLOAD_DEFAULT, SOCKET_UNKNOWN_ERROR_CODE));
						exchange.getOut().setBody(generateErrorJson(requestMap, SOCKET_UNKNOWN_ERROR_CODE, errorMsg));
						return;
					} else {
						// 加密机返回错误数据
						log.info(getText(IL8N_REMOTEKEYLOAD_DEFAULT, GLOBAL_INFO_CODE_SOCKET_RETURN_DATA_FALSE));
						errorMsg = ReturnMsgUtil.generateErrorMessage(GLOBAL_INFO_CODE_SOCKET_RETURN_DATA_FALSE,
								getText(IL8N_REMOTEKEYLOAD_DEFAULT, GLOBAL_INFO_CODE_SOCKET_RETURN_DATA_FALSE));
						exchange.getOut().setBody(generateErrorJson(requestMap, GLOBAL_INFO_CODE_SOCKET_RETURN_DATA_FALSE, errorMsg));
						return;
					}
				} else {
					// 加密机返回错误数据
					log.info(getText(IL8N_REMOTEKEYLOAD_DEFAULT, GLOBAL_INFO_CODE_SOCKET_RETURN_DATA_FALSE));
					errorMsg = ReturnMsgUtil.generateErrorMessage(GLOBAL_INFO_CODE_SOCKET_RETURN_DATA_FALSE,
							getText(IL8N_REMOTEKEYLOAD_DEFAULT, GLOBAL_INFO_CODE_SOCKET_RETURN_DATA_FALSE));
					exchange.getOut().setBody(generateErrorJson(requestMap, GLOBAL_INFO_CODE_SOCKET_RETURN_DATA_FALSE, errorMsg));
					return;
				}
			} catch (Exception e) {
				// 通信异常
				e.printStackTrace();
				log.error(getText(IL8N_REMOTEKEYLOAD_DEFAULT, GLOBAL_ERROR_CODE_SOCKET_ERROR));
				errorMsg = ReturnMsgUtil.generateErrorMessage(GLOBAL_ERROR_CODE_SOCKET_ERROR,
						getText(IL8N_REMOTEKEYLOAD_DEFAULT, GLOBAL_ERROR_CODE_SOCKET_ERROR));
				exchange.getOut().setBody(generateErrorJson(requestMap, GLOBAL_ERROR_CODE_SOCKET_ERROR, errorMsg));
				return;
			}
			// D106命令发送结束

			// 开始转加密KEY(C049)
			if (signVerified) {
				Map<String, String> inputMap = new HashMap<String, String>();
				if (mapmf.get("R1") != null && mapmf.get("R1").toString().length() == 16) {
					inputMap.put("userReservedStr", mapmf.get("R1").toString());// 用户保留字
				} else {
					inputMap.put("userReservedStr", DEFAULT_USER_RESERVED_STR);// 用户保留字，暂用16位"0"
				}
				Map<String, Object> terminalMap = remoteDao.findSysTermByTermIdAndIP(strTerminalID, ip_hsm);
				if (terminalMap.get("KEY_TEXT") != null
						&& terminalMap.get("KEY_TEXT").toString().trim().length() == 32) {
					inputMap.put("KEY_TEXT", terminalMap.get("KEY_TEXT").toString().trim());// KEY
				} else {
					log.info(getText(IL8N_REMOTEKEYLOAD_DEFAULT, REMOTEKEYLOAD_ERROR_ATM_PK_DATA));
					errorMsg = ReturnMsgUtil.generateErrorMessage(REMOTEKEYLOAD_ERROR_ATM_PK_DATA,
							getText(IL8N_REMOTEKEYLOAD_DEFAULT, REMOTEKEYLOAD_ERROR_ATM_PK_DATA));
					exchange.getOut().setBody(generateErrorJson(requestMap, REMOTEKEYLOAD_ERROR_ATM_PK_DATA, errorMsg));
					return;
				}
				HSMCommand c049 = new HSMCommand_C049(inputMap);
				String c049Msg = c049.packageInputField();
				// 开始通信
				try {
					rtmsg = HSMSocketClient.sendAndReceivePacket(c049Msg.trim(), ip_hsm, port_hsm, false);
					log.info("命令：" + c049Msg.toString().trim() + ",加密机返回数据：" + rtmsg);
					if (rtmsg != null && rtmsg.substring(0, 2).equals("41")) {
						// 保存转加密密文数据
						resultMap.put("encryption", rtmsg.substring(22, rtmsg.length()));
						signVerified = true;
					} else if (rtmsg.equals(SOCKET_TIMEOUT_ERROR_CODE)) {
						log.info(getText(IL8N_REMOTEKEYLOAD_DEFAULT, SOCKET_TIMEOUT_ERROR_CODE));
						errorMsg = ReturnMsgUtil.generateErrorMessage(SOCKET_TIMEOUT_ERROR_CODE,
								getText(IL8N_REMOTEKEYLOAD_DEFAULT, SOCKET_TIMEOUT_ERROR_CODE));
						exchange.getOut().setBody(generateErrorJson(requestMap, SOCKET_TIMEOUT_ERROR_CODE, errorMsg));
						return;
					} else if (rtmsg.equals(SOCKET_UNKNOWNHOST_ERROR_CODE)) {
						log.info(getText(IL8N_REMOTEKEYLOAD_DEFAULT, SOCKET_UNKNOWNHOST_ERROR_CODE));
						errorMsg = ReturnMsgUtil.generateErrorMessage(SOCKET_UNKNOWNHOST_ERROR_CODE,
								getText(IL8N_REMOTEKEYLOAD_DEFAULT, SOCKET_UNKNOWNHOST_ERROR_CODE));
						exchange.getOut().setBody(generateErrorJson(requestMap, SOCKET_UNKNOWNHOST_ERROR_CODE, errorMsg));
						return;
					} else if (rtmsg.equals(SOCKET_CLIENT_CLOSE_ERROR_CODE)) {
						log.info(getText(IL8N_REMOTEKEYLOAD_DEFAULT, SOCKET_CLIENT_CLOSE_ERROR_CODE));
						errorMsg = ReturnMsgUtil.generateErrorMessage(SOCKET_CLIENT_CLOSE_ERROR_CODE,
								getText(IL8N_REMOTEKEYLOAD_DEFAULT, SOCKET_CLIENT_CLOSE_ERROR_CODE));
						exchange.getOut().setBody(generateErrorJson(requestMap, SOCKET_CLIENT_CLOSE_ERROR_CODE, errorMsg));
						return;
					} else if (rtmsg.equals(SOCKET_UNKNOWN_ERROR_CODE)) {
						log.info(getText(IL8N_REMOTEKEYLOAD_DEFAULT, SOCKET_UNKNOWN_ERROR_CODE));
						errorMsg = ReturnMsgUtil.generateErrorMessage(SOCKET_UNKNOWN_ERROR_CODE,
								getText(IL8N_REMOTEKEYLOAD_DEFAULT, SOCKET_UNKNOWN_ERROR_CODE));
						exchange.getOut().setBody(generateErrorJson(requestMap, SOCKET_UNKNOWN_ERROR_CODE, errorMsg));
						return;
					} else {
						// 加密机返回错误数据
						log.info(getText(IL8N_REMOTEKEYLOAD_DEFAULT, GLOBAL_INFO_CODE_SOCKET_RETURN_DATA_FALSE));
						errorMsg = ReturnMsgUtil.generateErrorMessage(GLOBAL_INFO_CODE_SOCKET_RETURN_DATA_FALSE,
								getText(IL8N_REMOTEKEYLOAD_DEFAULT, GLOBAL_INFO_CODE_SOCKET_RETURN_DATA_FALSE));
						exchange.getOut().setBody(generateErrorJson(requestMap, GLOBAL_INFO_CODE_SOCKET_RETURN_DATA_FALSE, errorMsg));
						return;
					}

				} catch (Exception e) {
					// 通信异常
					log.error(getText(IL8N_REMOTEKEYLOAD_DEFAULT, GLOBAL_ERROR_CODE_SOCKET_ERROR));
					errorMsg = ReturnMsgUtil.generateErrorMessage(GLOBAL_ERROR_CODE_SOCKET_ERROR,
							getText(IL8N_REMOTEKEYLOAD_DEFAULT, GLOBAL_ERROR_CODE_SOCKET_ERROR));
					exchange.getOut().setBody(generateErrorJson(requestMap, GLOBAL_ERROR_CODE_SOCKET_ERROR, errorMsg));
					return;
				}
			}

			// 如果密文转加密成功，执行密文签名(C046)
			if (signVerified) {
				Map<String, String> inputMap = new HashMap<String, String>();
				if (mapmf.get("R1") != null && mapmf.get("R1").toString().length() == 16) {
					inputMap.put("userReservedStr", mapmf.get("R1").toString());// 用户保留字
				} else {
					inputMap.put("userReservedStr", DEFAULT_USER_RESERVED_STR);// 用户保留字，暂用16位"0"
				}
				if (mapmf.get("SK") != null) {
					inputMap.put("SK", mapmf.get("SK").toString());// 私钥
				} else {
					log.error(getText(IL8N_REMOTEKEYLOAD_DEFAULT, REMOTEKEYLOAD_ERROR_SK__DATA));
					errorMsg = ReturnMsgUtil.generateErrorMessage(REMOTEKEYLOAD_ERROR_SK__DATA,
							getText(IL8N_REMOTEKEYLOAD_DEFAULT, REMOTEKEYLOAD_ERROR_SK__DATA));
					exchange.getOut().setBody(generateErrorJson(requestMap, REMOTEKEYLOAD_ERROR_SK__DATA, errorMsg));
					return;
				}
				inputMap.put("encryption", resultMap.get("encryption").toString());
				HSMCommand c046 = new HSMCommand_C046(inputMap);
				String c046Msg = c046.packageInputField();
				// 开始通信
				try {
					rtmsg = HSMSocketClient.sendAndReceivePacket(c046Msg, ip_hsm, port_hsm, false);
					log.info("命令：" + c046Msg + ",加密机返回数据：" + rtmsg);
					if (rtmsg != null && rtmsg.substring(0, 2).equals("41")) {
						// 保存转加密密文数据
						log.info( "命令：" + c046Msg.toString().trim() + ",返回数据："+rtmsg);
						resultMap.put("encryptionSignature", rtmsg.substring(22, rtmsg.length()));
					} else if (rtmsg.equals(SOCKET_TIMEOUT_ERROR_CODE)) {
						log.info(getText(IL8N_REMOTEKEYLOAD_DEFAULT, SOCKET_TIMEOUT_ERROR_CODE));
						errorMsg = ReturnMsgUtil.generateErrorMessage(SOCKET_TIMEOUT_ERROR_CODE,
								getText(IL8N_REMOTEKEYLOAD_DEFAULT, SOCKET_TIMEOUT_ERROR_CODE));
						exchange.getOut().setBody(generateErrorJson(requestMap, SOCKET_TIMEOUT_ERROR_CODE, errorMsg));
						return;
					} else if (rtmsg.equals(SOCKET_UNKNOWNHOST_ERROR_CODE)) {
						log.info(getText(IL8N_REMOTEKEYLOAD_DEFAULT, SOCKET_UNKNOWNHOST_ERROR_CODE));
						errorMsg = ReturnMsgUtil.generateErrorMessage(SOCKET_UNKNOWNHOST_ERROR_CODE,
								getText(IL8N_REMOTEKEYLOAD_DEFAULT, SOCKET_UNKNOWNHOST_ERROR_CODE));
						exchange.getOut().setBody(generateErrorJson(requestMap, SOCKET_UNKNOWNHOST_ERROR_CODE, errorMsg));
						return;
					} else if (rtmsg.equals(SOCKET_CLIENT_CLOSE_ERROR_CODE)) {
						log.info(getText(IL8N_REMOTEKEYLOAD_DEFAULT, SOCKET_CLIENT_CLOSE_ERROR_CODE));
						errorMsg = ReturnMsgUtil.generateErrorMessage(SOCKET_CLIENT_CLOSE_ERROR_CODE,
								getText(IL8N_REMOTEKEYLOAD_DEFAULT, SOCKET_CLIENT_CLOSE_ERROR_CODE));
						exchange.getOut().setBody(generateErrorJson(requestMap, SOCKET_CLIENT_CLOSE_ERROR_CODE, errorMsg));
						return;
					} else if (rtmsg.equals(SOCKET_UNKNOWN_ERROR_CODE)) {
						log.info(getText(IL8N_REMOTEKEYLOAD_DEFAULT, SOCKET_UNKNOWN_ERROR_CODE));
						errorMsg = ReturnMsgUtil.generateErrorMessage(SOCKET_UNKNOWN_ERROR_CODE,
								getText(IL8N_REMOTEKEYLOAD_DEFAULT, SOCKET_UNKNOWN_ERROR_CODE));
						exchange.getOut().setBody(generateErrorJson(requestMap, SOCKET_UNKNOWN_ERROR_CODE, errorMsg));
						return;
					} else {
						// 加密机返回错误数据
						log.info("命令："+ c046Msg + ",返回数据：" + rtmsg);
						log.info(getText(IL8N_REMOTEKEYLOAD_DEFAULT, GLOBAL_INFO_CODE_SOCKET_RETURN_DATA_FALSE));
						errorMsg = ReturnMsgUtil.generateErrorMessage(GLOBAL_INFO_CODE_SOCKET_RETURN_DATA_FALSE,
								getText(IL8N_REMOTEKEYLOAD_DEFAULT, GLOBAL_INFO_CODE_SOCKET_RETURN_DATA_FALSE));
						exchange.getOut().setBody(generateErrorJson(requestMap, GLOBAL_INFO_CODE_SOCKET_RETURN_DATA_FALSE, errorMsg));
						return;
					}

				} catch (Exception e) {
					// 通信异常
					e.printStackTrace();
					log.info("命令：" + c046Msg + ",加密机返回数据： "+ rtmsg+",异常信息："+e.getMessage()+"======="+e.getStackTrace());
					log.info("命令："+ c046Msg + ",返回数据："+rtmsg);
					log.error(getText(IL8N_REMOTEKEYLOAD_DEFAULT,GLOBAL_ERROR_CODE_SOCKET_ERROR));
					errorMsg = ReturnMsgUtil.generateErrorMessage(GLOBAL_ERROR_CODE_SOCKET_ERROR,
							getText(IL8N_REMOTEKEYLOAD_DEFAULT, GLOBAL_ERROR_CODE_SOCKET_ERROR));
					exchange.getOut().setBody(generateErrorJson(requestMap, GLOBAL_ERROR_CODE_SOCKET_ERROR, errorMsg));
					return;
				}
			}

			try {
				if (mapmf.get("BANKPKSIGNATURE") == null
						|| mapmf.get("BANKPKSIGNATURE").toString().trim().length() != 540) {

					log.info(getText(IL8N_REMOTEKEYLOAD_DEFAULT, REMOTEKEYLOAD_ERROR_BANK_PK_DATA));
					errorMsg = ReturnMsgUtil.generateErrorMessage(REMOTEKEYLOAD_ERROR_BANK_PK_DATA,
							getText(IL8N_REMOTEKEYLOAD_DEFAULT, REMOTEKEYLOAD_ERROR_BANK_PK_DATA));
					exchange.getOut().setBody(generateErrorJson(requestMap, REMOTEKEYLOAD_ERROR_BANK_PK_DATA, errorMsg));
					return;
				} else if (mapmf.get("MANUSIGNATUREBANK") == null
						|| mapmf.get("MANUSIGNATUREBANK").toString().trim().length() != 512) {
					log.info(getText(IL8N_REMOTEKEYLOAD_DEFAULT, REMOTEKEYLOAD_ERROR_BANK_PK_MANU_SIGN_DATA));
					errorMsg = ReturnMsgUtil.generateErrorMessage(REMOTEKEYLOAD_ERROR_BANK_PK_MANU_SIGN_DATA,
							getText(IL8N_REMOTEKEYLOAD_DEFAULT, REMOTEKEYLOAD_ERROR_BANK_PK_MANU_SIGN_DATA));
					exchange.getOut().setBody(generateErrorJson(requestMap, REMOTEKEYLOAD_ERROR_BANK_PK_MANU_SIGN_DATA, errorMsg));
					return;
				} else {
					// 创建返回的对象
					Map<String, Object> mapatm = remoteDao.findSysTermByTermIdAndIP(strTerminalID, ip);
					// 拼接返回字符串

					/***
					 * "MasterKeyCypher" 主密钥密文 "MasterKeyCypherSign" 主密钥密文签名 "BankPublicKey" 银行公钥
					 * "BankPublicKeySign" 银行公钥厂商签 "MasterKeyCheckCode" KEY_CHECK
					 */
					Map<String, Object> returnMap = new HashMap<String, Object>();
					returnMap.put("channelId", requestMap.get("channelId"));
					returnMap.put("transactionCode", requestMap.get("transactionCode"));
					returnMap.put("terminalId", requestMap.get("terminalId"));
					returnMap.put("channelId", requestMap.get("channelId"));
					returnMap.put("traceNumber", requestMap.get("traceNumber"));
					returnMap.put("transactionDate", requestMap.get("transactionDate"));
					returnMap.put("transactionTime", requestMap.get("transactionTime"));
					returnMap.put("MasterKeyCypher", resultMap.get("encryption").toString().trim());
					returnMap.put("MasterKeyCypherSign", resultMap.get("encryptionSignature").toString().trim());
					returnMap.put("BankPublicKey", mapmf.get("BANKPKSIGNATURE").toString().trim());
					returnMap.put("BankPublicKeySign", mapmf.get("MANUSIGNATUREBANK").toString().trim());
					returnMap.put("MasterKeyCheckCode", mapatm.get("KEY_CHECK").toString().trim());

					// 构造JSON并放入上下文
					exchange.getOut().setBody(FormatUtil.map2Json(returnMap));
				}
			} catch (Exception e) {
				// 加密机返回错误数据
				log.error(getText(IL8N_REMOTEKEYLOAD_DEFAULT, GLOBAL_INFO_CODE_SOCKET_RETURN_DATA_FALSE));
				errorMsg = ReturnMsgUtil.generateErrorMessage(GLOBAL_INFO_CODE_SOCKET_RETURN_DATA_FALSE,
						getText(IL8N_REMOTEKEYLOAD_DEFAULT, GLOBAL_INFO_CODE_SOCKET_RETURN_DATA_FALSE));
				exchange.getOut().setBody(generateErrorJson(requestMap, GLOBAL_INFO_CODE_SOCKET_RETURN_DATA_FALSE, errorMsg));
				return;
			}
			// 结束
		}
	}

	/**
	 * 验证端机ip是否匹配
	 * 
	 * @param termId
	 * @param ip
	 * @param resourceName
	 * @param conName
	 * @return
	 */
	private String valTermIDAndIp(String termId, String ip, String resourceName, String resTerminalID, String resip) {
		int valcnt = remoteDao.terminalIDIsValid(ip, termId);
		if (valcnt == 2) {
			log.info("非法终端号： " + resTerminalID);
			return ReturnMsgUtil.generateErrorMessage(resTerminalID,
					getText(resourceName, resTerminalID, new Object[] { "strTerminalID" }));
		} else if (valcnt == 3) {
			log.info("端机号与IP地址不匹配 " + resTerminalID);
			return ReturnMsgUtil.generateErrorMessage(resip, getText(resourceName, resip));
		} else {
			return null;
		}
	}
	
	private String generateErrorJson(Map<String, Object> requestMap, String errorCode,
			String errorMessage) {
		Map<String, Object> errorMap = new HashMap<String, Object>();
		errorMap.put("channelId", requestMap.get("channelId"));
		errorMap.put("transactionCode", requestMap.get("transactionCode"));
		errorMap.put("terminalId", requestMap.get("terminalId"));
		errorMap.put("channelId", requestMap.get("channelId"));
		errorMap.put("traceNumber", requestMap.get("traceNumber"));
		errorMap.put("transactionDate", requestMap.get("transactionDate"));
		errorMap.put("transactionTime", requestMap.get("transactionTime"));
		errorMap.put("errorCode", errorCode);
		errorMap.put("errorMessage", errorMessage);

		return FormatUtil.map2Json(errorMap);
	}

}
