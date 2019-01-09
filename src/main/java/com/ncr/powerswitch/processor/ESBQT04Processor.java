package com.ncr.powerswitch.processor;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ncr.powerswitch.utils.FormatUtil;
import com.ncr.powerswitch.utils.GeneralUtil;
import com.ncr.powerswitch.utils.LogUtil;
import com.ncr.powerswitch.utils.PowerSwitchConstant;
import com.ncr.powerswitch.utils.XStreamEx;
import com.ncr.powerswitch.dataObject.TerminalKey;
import com.ncr.powerswitch.esb.ESB_QT04;
import com.ncr.powerswitch.exception.PowerswitchException;
import com.ncr.powerswitch.esb.model.AppHead;
import com.ncr.powerswitch.esb.model.Body;
import com.ncr.powerswitch.esb.model.EsbRet;
import com.ncr.powerswitch.esb.model.EsbService;
import com.ncr.powerswitch.esb.model.LocalHead;
import com.ncr.powerswitch.esb.model.SysHead;
import com.thoughtworks.xstream.io.xml.DomDriver;

/***
 * 
 * @author cc185015
 *
 */

public class EsbQt04Processor implements Processor {

	/**
	 * Log4j记录日志的工具类
	 */
	private final static Log log = LogFactory.getLog(EsbQt04Processor.class);
	private String errMsg = null;

	public void formatProcess(Exchange exchange) throws Exception {
	
		ESB_QT04 qt04 = new ESB_QT04();
		String qt04Text = qt04.constructRequest(exchange.getProperties());
		exchange.setProperty("formattedMessage", qt04Text);
		exchange.setProperty("macData", qt04Text);
	}

	public void appendHeadProcess(Exchange exchange) throws Exception {

		String formattedMessage = exchange.getProperty("formattedMessage", String.class);
		if (formattedMessage == null || formattedMessage.isEmpty()) {
			errMsg = LogUtil.getClassMethodName() + ":" + "formattedMessage is null(empty)";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}

		String mac = exchange.getProperty("mac", String.class);
		if (mac == null || mac.isEmpty()) {
			errMsg = LogUtil.getClassMethodName() + ":" + "mac is null(empty)";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}

		String macKeyHsm = null;
		if (!mac.equals("0000000000000000")) {
			TerminalKey terminalKey = exchange.getProperty("TerminalKey", TerminalKey.class);
			if (terminalKey == null) {
				errMsg = LogUtil.getClassMethodName() + ":" + "terminalKey is null";
				log.error(errMsg);
				throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
			}
			macKeyHsm = terminalKey.getMacKeyHsm();
		} else {
			macKeyHsm = "00000000000000000000000000000000";
		}

		if (macKeyHsm == null || macKeyHsm.isEmpty()) {
			errMsg = LogUtil.getClassMethodName() + ":" + "mac is null(empty)";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}

		StringBuffer qt04Buffer = new StringBuffer();
		qt04Buffer.append(macKeyHsm); // mac key + mac value 000 for testing
		qt04Buffer.append(mac);
		qt04Buffer.append(formattedMessage);

		String length = GeneralUtil.generatePayloadLength(qt04Buffer.toString());
		byte[] qt04Bytes = length.concat(qt04Buffer.toString()).getBytes();
		exchange.getOut().setBody(qt04Bytes);
	}

	public void deformatProcess(Exchange exchange) throws Exception {
		byte[] retBytes = exchange.getIn().getBody(byte[].class);
		if (retBytes==null){
			errMsg = LogUtil.getClassMethodName() + ":" + "response byte[] is null";
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}
		
		String esb_ret = FormatUtil.byteArray2Str(retBytes);
		if (esb_ret==null||esb_ret.isEmpty()||esb_ret.length()<56){
			errMsg = LogUtil.getClassMethodName() + ":" + "return error and esb_ret:" + esb_ret;
			log.error(errMsg);
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR, errMsg);
		}
		
		log.debug("received msg: " + esb_ret);		
		esb_ret = esb_ret.substring(56);
		esb_ret = esb_ret.replace("<BODY>", "<BODY class='com.ncr.powerswitch.esb.model.Body'>"); // bind the body instance 
		log.debug("esb return is: " + esb_ret);
		
		XStreamEx  xstream = new XStreamEx(new DomDriver("utf-8"));
		xstream.alias("service", EsbService.class);
		xstream.alias("SYS_HEAD", SysHead.class);
		xstream.alias("Ret", EsbRet.class);
		xstream.alias("APP_HEAD", AppHead.class);
		xstream.alias("LOCAL_HEAD", LocalHead.class);
		xstream.alias("BODY", Body.class);
		EsbService esbMsg = (EsbService) xstream.fromXML(esb_ret);
		
		Map<String, Object> head = new HashMap<String, Object>(); 
		
		head.put("channelId", (String)exchange.getProperties().get("channelId"));
		head.put("transactionCode", (String)exchange.getProperties().get("transactionCode"));
		head.put("terminalId", (String)exchange.getProperties().get("terminalId"));
		head.put("traceNumber", (String)exchange.getProperties().get("traceNumber"));
		head.put("transactionDate", (String)exchange.getProperties().get("transactionDate"));
		head.put("transactionTime", (String)exchange.getProperties().get("transactionTime"));
		head.put("errorCode", esbMsg.getSYS_HEAD().getArray().get(0).getReturnCode());
		head.put("errorMessage", esbMsg.getSYS_HEAD().getArray().get(0).getReturnMsg());
		
		Map<String, Object> body = new HashMap<String, Object>();
		
		Map<String, Object> retJsonMap = new HashMap<String, Object>();		
		
		retJsonMap.put("head", head);
		retJsonMap.put("body", body);
		exchange.getOut().setBody(FormatUtil.map2Json(retJsonMap));	
	}
	
	public void responseError(Exchange exchange) {
		Exception e = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);		
		//String endPoint = exchange.getProperty(Exchange.FAILURE_ENDPOINT,String.class);
		
		Map<String, Object> head = new HashMap<String, Object>(); 
		
		head.put("channelId", exchange.getProperties().get("channelId"));
		head.put("transactionCode", exchange.getProperties().get("transactionCode"));
		head.put("terminalId", exchange.getProperties().get("terminalId"));
		head.put("traceNumber", exchange.getProperties().get("traceNumber"));
		head.put("transactionDate", exchange.getProperties().get("transactionDate"));
		head.put("transactionTime", exchange.getProperties().get("transactionTime"));
		head.put("responseCode", "9999");
		head.put("errorMessage", e.getMessage());
		
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("errorDescription", e.getMessage());
		body.put("errorType", e.getClass().toString());	
		
		Map<String, Object> retJsonMap = new HashMap<String, Object>();
		retJsonMap.put("head", head);
		retJsonMap.put("body", body);
		
		exchange.getOut().setBody(FormatUtil.map2Json(retJsonMap));	
	}

	@Override
	public void process(Exchange exchange) throws Exception {

	}

}
