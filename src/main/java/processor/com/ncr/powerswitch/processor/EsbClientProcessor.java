package com.ncr.powerswitch.processor;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class EsbClientProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		Map<String, Object> headers = exchange.getIn().getHeaders(); 
		String msg = exchange.getIn().getBody().toString(); 
		
	}

}
