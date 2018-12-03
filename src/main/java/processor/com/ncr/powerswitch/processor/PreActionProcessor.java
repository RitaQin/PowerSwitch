package com.ncr.powerswitch.processor;

import java.io.InputStream;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ncr.powerswitch.utils.FormatUtil;

/***
 * pre-action processor
 * 报文格式预处理，在所有action之前进行
 * @author rq185015
 *
 */

public class PreActionProcessor implements Processor {
	
	private final static Log log = LogFactory.getLog(PreActionProcessor.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		
		//将收到的请求转化为map向后传递
		String message = FormatUtil.stream2Str((InputStream) exchange.getIn().getBody()); 
		
		exchange.getIn().setBody(message);			
	}
}
