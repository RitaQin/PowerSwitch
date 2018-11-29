package com.ncr.powerswitch.processor;

import static com.ncr.powerswitch.utils.ResourcesTool.IL8N_REMOTEKEYLOAD_DEFAULT;
import static com.ncr.powerswitch.utils.ResourcesTool.IL8N_RESOURCES_DEFAULT;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.REMOTEKEYLOAD_DEBUG_PARAM_LOG;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.GLOBAL_ERROR_CODE_PARAMS_NOT_NULL;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.GLOBAL_ERROR_CODE_TERMINAL_ID_INVALID;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.GLOBAL_ERROR_CODE_TERMID_ILLEGAL;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.GLOBAL_ERROR_CODE_TERMID_IP_FALSE;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.REMOTEKEYLOAD_ERROR_PK_DATA_ILLEGAL;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.REMOTEKEYLOAD_ERROR_SIGN_DATA_ILLEGAL;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.REMOTEKEYLOAD_ERROR_UPTATE_EPPID;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.REMOTEKEYLOAD_ERROR_EPPID_COMPARE_FALSE;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.REMOTEKEYLOAD_ERROR_CODE_HSMSET_ERROR;

import static com.ncr.powerswitch.utils.PowerSwitchConstant.VAL_OPEN;
import static com.ncr.powerswitch.utils.ResourcesTool.getText;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.lf5.LogLevel;

import com.ncr.powerswitch.util.hsm.HSMSocketClient;
import com.ncr.powerswitch.utils.FormatUtil;
import com.ncr.powerswitch.utils.LogUtil;
import com.ncr.powerswitch.utils.ReturnMsgUtil;
import com.ncr.powerswitch.utils.StringUtil;
import com.ncr.powerswitch.remote.atm.RemoteDAO;

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
 * 5. 主机向加密机发送命令将产生的工作密钥通过终端PK进行加密(C049)
 * 
 * 6. 转加密成功后，主机向加密机发送命令执行密文签名（C046）
 * 
 * 7. 主机将加密后的工作密钥和签名返回终端
 * 
 * @author rq185015
 *
 */

public class RKLProcessor implements Processor {

	/**
	 * Log4j记录日志的工具类
	 */
	private final static Log log = LogFactory.getLog(RKLProcessor.class);
	public RemoteDAO remoteDao;

	@Override
	public void process(Exchange exchange) throws Exception {

		String msg = exchange.getIn(String.class);
		Map<String, Object> requestMap = FormatUtil.json2Map(msg);

		// 从请求报文中提取远程密钥下载所需参数
		String ip = requestMap.get("ip") != null ? requestMap.get("ip").toString() : null; // IP地址
		String strTerminalID = requestMap.get("TerminalID") != null ? requestMap.get("TerminalID").toString() : null; // 终端号
		String strBatch = requestMap.get("Batch") != null ? requestMap.get("Batch").toString() : null; // 批次
		String strEppSerialNo = requestMap.get("EppSerialNo") != null ? requestMap.get("EppSerialNo").toString() : null; // EPP序列号
		String strEppSerialNoSign = requestMap.get("EppSerialNoSign") != null
				? requestMap.get("EppSerialNoSign").toString()
				: null; // EPP序列号签名
		String strEppPublicKey = requestMap.get("EppPublicKey") != null ? requestMap.get("EppPublicKey").toString()
				: null; // 存储在EPP里面的银行公钥
		String strEppPublicKeySign = requestMap.get("EppPublicKeySign") != null
				? requestMap.get("EppPublicKeySign").toString()
				: null; // 存储在EPP里面的银行公钥签名

		// TODO:记录远程申请密钥接口参数

		// 验证参数有效性开始
		String errorMsg = null;
		if (StringUtil.isNull(strTerminalID)) {
			// TODO: 记录错误
			errorMsg = ReturnMsgUtil.generateErrorMessage(GLOBAL_ERROR_CODE_PARAMS_NOT_NULL, getText(
					IL8N_RESOURCES_DEFAULT, GLOBAL_ERROR_CODE_PARAMS_NOT_NULL, new Object[] { "strTerminalID" }));
			exchange.getIn().setBody(errorMsg); // 将错误信息放入上下文中
			return;
		}
		// 验证终端号长度范围
		else if (strTerminalID.length() < 1 || strTerminalID.length() > 16) {
			// TODO: 记录错误
			errorMsg = ReturnMsgUtil.generateErrorMessage(GLOBAL_ERROR_CODE_TERMINAL_ID_INVALID, getText(
					IL8N_RESOURCES_DEFAULT, GLOBAL_ERROR_CODE_TERMINAL_ID_INVALID, new Object[] { "strTerminalID" }));
			exchange.getIn().setBody(errorMsg);
			return;
		}
		// 验证端机号合法性
		else if (getText(IL8N_RESOURCES_DEFAULT, VAL_OPEN).equals("true")) {
			String result = valTermIDAndIp(strTerminalID, ip, IL8N_RESOURCES_DEFAULT, GLOBAL_ERROR_CODE_TERMID_ILLEGAL,
					GLOBAL_ERROR_CODE_TERMID_IP_FALSE);
			if (result != null) {
				exchange.getIn().setBody(result);
				return;
			}
		}
		// 验证批次号是否为空
		else if (StringUtil.isNull(strBatch)) {
			// TODO: 记录错误
			errorMsg = ReturnMsgUtil.generateErrorMessage(GLOBAL_ERROR_CODE_PARAMS_NOT_NULL,
					getText(IL8N_RESOURCES_DEFAULT, GLOBAL_ERROR_CODE_PARAMS_NOT_NULL, new Object[] { "strBatch" }));
			exchange.getIn().setBody(errorMsg);
			return;
		}
		// 验证EPP序列号是否为空
		else if (StringUtil.isNull(strEppSerialNo)) {
			// TODO: 记录错误
			errorMsg = ReturnMsgUtil.generateErrorMessage(GLOBAL_ERROR_CODE_PARAMS_NOT_NULL, getText(
					IL8N_RESOURCES_DEFAULT, GLOBAL_ERROR_CODE_PARAMS_NOT_NULL, new Object[] { "strEppSerialNo" }));
			exchange.getIn().setBody(errorMsg);
			return;
		}
		// 验证存储在EPP里面的银行公钥是否为空
		else if (StringUtil.isNull(strEppPublicKey)) {
			// TODO: 记录错误
			errorMsg = ReturnMsgUtil.generateErrorMessage(GLOBAL_ERROR_CODE_PARAMS_NOT_NULL, getText(
					IL8N_RESOURCES_DEFAULT, GLOBAL_ERROR_CODE_PARAMS_NOT_NULL, new Object[] { "strEppPublicKey" }));
			exchange.getIn().setBody(errorMsg);
			return;
		}
		// 验证存储在EPP里面的银行公钥签名是否为空
		else if (StringUtil.isNull(strEppPublicKeySign)) {
			// TODO: 记录错误
			errorMsg = ReturnMsgUtil.generateErrorMessage(GLOBAL_ERROR_CODE_PARAMS_NOT_NULL, getText(
					IL8N_RESOURCES_DEFAULT, GLOBAL_ERROR_CODE_PARAMS_NOT_NULL, new Object[] { "strEppPublicKeySign" }));
			exchange.getIn().setBody(errorMsg);
			return;
		}
		// 验证存储在EPP里面的银行公钥长度
		else if (strEppPublicKey.length() != 540) {
			// TODO: 记录错误
			errorMsg = ReturnMsgUtil.generateErrorMessage(REMOTEKEYLOAD_ERROR_PK_DATA_ILLEGAL,
					getText(IL8N_REMOTEKEYLOAD_DEFAULT, REMOTEKEYLOAD_ERROR_PK_DATA_ILLEGAL));
			exchange.getIn().setBody(errorMsg);
			return;
		}
		// 验证存储在EPP里面的银行公钥长度
		else if (strEppPublicKeySign.length() != 512) {
			// TODO: 记录错误
			errorMsg = ReturnMsgUtil.generateErrorMessage(REMOTEKEYLOAD_ERROR_SIGN_DATA_ILLEGAL,
					getText(IL8N_REMOTEKEYLOAD_DEFAULT, REMOTEKEYLOAD_ERROR_SIGN_DATA_ILLEGAL));
			exchange.getIn().setBody(errorMsg);
			return;
		}
		// 验证参数有效性结束......
		// 将EPP序列号更新 EPPID
		int cnt = remoteDao.updateATMEPPID(strTerminalID, strEppSerialNo);
		if (cnt == 0) {
			// TODO: 日志记录错误
			errorMsg = ReturnMsgUtil.generateErrorMessage(REMOTEKEYLOAD_ERROR_UPTATE_EPPID,
					getText(IL8N_REMOTEKEYLOAD_DEFAULT, REMOTEKEYLOAD_ERROR_UPTATE_EPPID));
			exchange.getIn().setBody(errorMsg);
			return;
		} else if (cnt == 99) {
			// TODO: 日志记录错误
			errorMsg = ReturnMsgUtil.generateErrorMessage(REMOTEKEYLOAD_ERROR_EPPID_COMPARE_FALSE,
					getText(IL8N_REMOTEKEYLOAD_DEFAULT, REMOTEKEYLOAD_ERROR_EPPID_COMPARE_FALSE));
			exchange.getIn().setBody(errorMsg);
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
			// TODO: 日志记录错误
			errorMsg = ReturnMsgUtil.generateErrorMessage(REMOTEKEYLOAD_ERROR_CODE_HSMSET_ERROR,
					getText(IL8N_REMOTEKEYLOAD_DEFAULT, REMOTEKEYLOAD_ERROR_CODE_HSMSET_ERROR));
			exchange.getIn().setBody(errorMsg);
			return;
		}
		// 加密机配置参数获取失败参数
		if (mapHsm == null) {
			// TODO: 日志记录错误
			errorMsg = ReturnMsgUtil.generateErrorMessage(REMOTEKEYLOAD_ERROR_CODE_HSMSET_ERROR,
					getText(IL8N_REMOTEKEYLOAD_DEFAULT, REMOTEKEYLOAD_ERROR_CODE_HSMSET_ERROR));
			exchange.getIn().setBody(errorMsg);
			return;
		} else if (StringUtil.isNull(mapHsm.get("IP").toString()) || StringUtil.isNull(mapHsm.get("PORT").toString())) {
			// TODO: 日志记录错误
			errorMsg = ReturnMsgUtil.generateErrorMessage(REMOTEKEYLOAD_ERROR_CODE_HSMSET_ERROR,
					getText(IL8N_REMOTEKEYLOAD_DEFAULT, REMOTEKEYLOAD_ERROR_CODE_HSMSET_ERROR));
			exchange.getIn().setBody(errorMsg);
			return;
		}
		// 获取加密机配置参数
		String ip_hsm = mapHsm.get("IP").toString(); // 加密机IP
		String port_hsm = mapHsm.get("PORT").toString();// 加密机端口
		StringBuffer msgfmt;
		String rtmsg = ""; // 保存加密机返回数据
		
		// 发送命令<0XC047> 验证签名
		
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
			// TODO: 日志记录
			return ReturnMsgUtil.generateErrorMessage(resTerminalID,
					getText(resourceName, resTerminalID, new Object[] { "strTerminalID" }));
		} else if (valcnt == 3) {
			// TODO: 日志记录
			return ReturnMsgUtil.generateErrorMessage(resip, getText(resourceName, resip));
		} else {
			return null;
		}
	}

}
