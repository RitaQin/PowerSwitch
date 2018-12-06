package com.ncr.powerswitch.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.ncr.powerswitch.hsm.HSMCommand_D106;
import com.ncr.powerswitch.hsm.HSMSocketClient;

public class DataKeyProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		
		System.out.println("Starting command D106");

		HSMCommand_D106 d106 = new HSMCommand_D106();
		String d106_msg = d106.packageInputField();
		
		System.out.println("d106_msg : " + d106_msg);
		
		String returnMsg = HSMSocketClient.sendAndReceivePacket(d106_msg, "8.99.9.91", "3000", false);
		
		System.out.println("HSM returns: " + returnMsg);
		
		exchange.getOut().setBody(returnMsg);
	}

}
