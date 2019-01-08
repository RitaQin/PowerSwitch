package com.ncr.powerswitch.processor;

import java.util.Map;

import org.apache.camel.Exchange;

public class EsbClientProcessor implements BaseProcessor {

	@Override
	public void process(Exchange exchange) throws Exception {
		Map<String, Object> headers = exchange.getIn().getHeaders(); 
		String msg = exchange.getIn().getBody().toString(); 
		
	}

}
