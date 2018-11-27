package com.ncr.powerswitch.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.ncr.powerswitch.util.hsm.HSMSocketClient;

/**
 * 加密机连接
 * @author rq185015
 *
 */

public class HSMProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		
		String message = exchange.getIn().getBody().toString();
		
		
	}

}
