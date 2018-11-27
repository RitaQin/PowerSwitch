package com.ncr.powerswitch.processor;

/***
 * 为测试连接用，生产版本删除此类
 */

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class PrintProcessor implements Processor{

	@Override
	public void process(Exchange exchange) throws Exception {
		String message = exchange.getIn().toString();
		System.out.println("Message received " + message);
	}
}
