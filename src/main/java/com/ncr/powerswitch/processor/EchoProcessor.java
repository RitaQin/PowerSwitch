package com.ncr.powerswitch.processor;

import org.apache.camel.Exchange;
import org.apache.log4j.Logger;

public class EchoProcessor implements BaseProcessor{

	@Override
	public void process(Exchange exchange) throws Exception {
		System.out.println("PowerSwitch Running");
		
		Logger log = Logger.getLogger(EchoProcessor.class);
	
	}
	
}
