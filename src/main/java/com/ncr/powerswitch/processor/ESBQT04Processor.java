package com.ncr.powerswitch.processor;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ncr.powerswitch.utils.FormatUtil;
import com.ncr.powerswitch.utils.GeneralUtil;
import com.ncr.powerswitch.utils.PowerSwitchConstant;
import com.ncr.powerswitch.utils.XStreamEx;

import com.ncr.powerswitch.esb.ESB_QT04;
import com.ncr.powerswitch.esb.model.AppHead;
import com.ncr.powerswitch.esb.model.Body;
import com.ncr.powerswitch.esb.model.LocalHead;
import com.ncr.powerswitch.esb.model.EsbRet;
import com.ncr.powerswitch.esb.model.SysHead;
import com.ncr.powerswitch.exception.PowerswitchException;
import com.ncr.powerswitch.esb.model.EsbService;
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

	public void formatProcess(Exchange exchange) throws Exception{		
		@SuppressWarnings("unchecked")
		Map<String, Object> requestMap =  exchange.getIn().getBody(Map.class);
		//log.info("QT04 PROCESSING : request msg: " + msg);
		exchange.getProperties().put("channelId", requestMap.get("channelId"));
		exchange.getProperties().put("terminalId", requestMap.get("terminalId"));
		exchange.getProperties().put("traceNumber", requestMap.get("traceNumber"));
		exchange.getProperties().put("transactionDate", requestMap.get("transactionDate"));
		exchange.getProperties().put("transactionTime", requestMap.get("transactionTime"));
				
		ESB_QT04 qt04 = new ESB_QT04();
		String qt04Text = qt04.constructRequest(requestMap);
		StringBuffer qt04Buffer = new StringBuffer(); 
		qt04Buffer.append("000000000000000000000000000000000000000000000000"); //mac key + mac value 000 for testing
		qt04Buffer.append(qt04Text);
		String length = GeneralUtil.generatePayloadLength(qt04Buffer.toString());
		byte[] qt04Bytes = length.concat(qt04Buffer.toString()).getBytes();
		exchange.getOut().setBody(qt04Bytes);
	}
	
	public void deformatProcess(Exchange exchange) throws Exception{
		byte[] retBytes = exchange.getIn().getBody(byte[].class);
		if (retBytes == null) {
			log.error("ESB return message empty.");			
			throw new PowerswitchException(PowerSwitchConstant.ESB_RETURNEMPTY,"ESB return message empty.");
		}
		String esb_ret = FormatUtil.byteArray2Str(retBytes);
		log.debug("received msg: " + esb_ret);
		esb_ret = esb_ret.substring(56);
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
		head.put("transactionCode", "QT04");
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

	@Override
	public void process(Exchange exchange) throws Exception {
		
	}	

}
