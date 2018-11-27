package com.ncr.powerswitch.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/***
 * Processor接口
 * @author rq185015
 *
 */

public interface ProcessorIntf extends Processor {
	
	void process(Exchange exchange) throws Exception;
	
}
