package com.ncr.powerswitch.processor;

import java.io.InputStream;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.ncr.powerswitch.utils.FormatUtil;

public class FormatProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		
		//将收到的请求转化为map向后传递
		String message = FormatUtil.stream2Str((InputStream) exchange.getIn().getBody()); 
		Map<String, Object> payloadMap = FormatUtil.json2Map(message);
		
		exchange.getIn().setBody(payloadMap);			
	}
}
