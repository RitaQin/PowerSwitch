package com.ncr.powerswitch.processor;

import java.util.Map;

import org.apache.camel.Exchange;

import com.ncr.powerswitch.dataObject.TerminalKey;

public class NormalProcessor {
	public void getTerminalIdProcess(Exchange exchange) throws Exception{		
		System.out.println("NormalProcessor:" + exchange.getProperties().get("terminalId"));
		@SuppressWarnings("unchecked")
		Map<String, Object> requestMap =  exchange.getIn().getBody(Map.class);
		exchange.getProperties().putAll(requestMap);
		exchange.getOut().setBody(exchange.getProperties().get("terminalId"));
	}
	
	public void putTerminalKeyProcess(Exchange exchange) throws Exception{
		System.out.println("NormalProcessor: terminalKey");
		TerminalKey terminalKey =  exchange.getIn().getBody(TerminalKey.class);
		exchange.getProperties().put("terminalKey", terminalKey);
	}
}
