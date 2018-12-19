package com.ncr.powerswitch.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class EchoProcessor implements BaseProcessor{

	@Override
	public void process(Exchange exchange) throws Exception {
		System.out.println("PowerSwitch Running");
		exchange.getOut().setBody("PowerSwitch Connected!");
	}
	
}
