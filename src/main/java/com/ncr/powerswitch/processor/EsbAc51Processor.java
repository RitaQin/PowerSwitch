package com.ncr.powerswitch.processor;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ncr.powerswitch.esb.ESB_AC51;
import com.ncr.powerswitch.esb.model.AppHead;
import com.ncr.powerswitch.esb.model.Body;
import com.ncr.powerswitch.esb.model.LocalHead;
import com.ncr.powerswitch.esb.model.EsbRet;
import com.ncr.powerswitch.esb.model.SysHead;
import com.ncr.powerswitch.esb.model.EsbService;
import com.ncr.powerswitch.utils.FormatUtil;
import com.ncr.powerswitch.utils.XStreamEx;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class EsbAc51Processor implements BaseProcessor {

	/**
	 * Log4j记录日志的工具类
	 */
	private final static Log log = LogFactory.getLog(EsbAc51Processor.class);

	public void formatProcess(Exchange exchange) throws Exception {
		@SuppressWarnings("unchecked")
		Map<String, String> requestMap = exchange.getIn().getBody(Map.class);
		// log.info("QT04 PROCESSING : request msg: " + msg);
		exchange.getProperties().put("channelId", requestMap.get("channelId"));
		exchange.getProperties().put("terminalId", requestMap.get("terminalId"));
		exchange.getProperties().put("traceNumber", requestMap.get("traceNumber"));
		exchange.getProperties().put("transactionDate", requestMap.get("transactionDate"));
		exchange.getProperties().put("transactionTime", requestMap.get("transactionTime"));

		ESB_AC51 ac51 = new ESB_AC51();
		String ac51Text = ac51.constructRequest(requestMap);
		exchange.getOut().setBody(ac51Text.getBytes());
	}

	public void deformatProcess(Exchange exchange) throws Exception {
		String esb_ret = exchange.getIn().getBody(String.class);
		esb_ret = esb_ret.substring(8);
		System.out.println("esb return is: " + esb_ret);
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
		head.put("transactionCode", "AC51");
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
