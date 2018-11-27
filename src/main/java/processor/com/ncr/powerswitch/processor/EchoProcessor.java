package com.ncr.powerswitch.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class EchoProcessor implements Processor{

	@Override
	public void process(Exchange exchange) throws Exception {
		
		exchange.getOut().setBody("PowerSwitch Connected!");
		
	}
	
}
