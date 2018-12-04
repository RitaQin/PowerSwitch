package com.ncr.powerswitch.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ncr.powerswitch.utils.FormatUtil;

public class MacVerificationProcessor implements Processor {
	
	private final static Log log = LogFactory.getLog(MacVerificationProcessor.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		
		String msg = exchange.getIn().getBody(String.class);
		Map<String, Object> payloadMap = FormatUtil.json2Map(msg);
		
		StringBuffer buffer = new StringBuffer();
		List<String> keys = new ArrayList<String>(payloadMap.keySet());
		int size = keys.size();
		for (int i = 0; i < size; i++) {
			String value = payloadMap.get(keys.get(i)).toString();
			buffer.append(value.trim());
		}
		String data = buffer.toString(); 
		int dataSize = data.length();
		
		Map<String, String> inputMap = new HashMap<String, String>();
		inputMap.put("data", data);
		
		
	}
}
