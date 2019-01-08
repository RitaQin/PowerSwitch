package com.ncr.powerswitch.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ncr.powerswitch.DAO.RemoteDAO;
import com.ncr.powerswitch.dataObject.TerminalKey;
import com.ncr.powerswitch.exception.PowerswitchException;
import com.ncr.powerswitch.hsm.HSMCommand_D132;
import com.ncr.powerswitch.hsm.HSMCommand_D134;
import com.ncr.powerswitch.utils.FormatUtil;
import com.ncr.powerswitch.utils.PowerSwitchConstant;
import com.ncr.powerswitch.utils.StringUtil;

public class MacVerificationProcessor implements BaseProcessor {
	
	private final static Log log = LogFactory.getLog(MacVerificationProcessor.class);
	public RemoteDAO remoteDao;

	@Override
	public void process(Exchange exchange) throws Exception {
		
		String msg = exchange.getIn().getBody(String.class);
		Map<String, Object> payloadMap = FormatUtil.json2Map(msg);
		
		//全报文进行MAC验证，拼接报文阈值
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
	
	public void requestHsmD132Process(Exchange exchange) throws Exception{
		
		TerminalKey terminalKey = (TerminalKey)exchange.getProperties().get("terminalKey");	
		Map<String, String> inputMap = new HashMap<String, String>();
		
		inputMap.put("macKeyLen", "HSM_KEYLEN_2");
		inputMap.put("macKey", terminalKey.getMacKey());
		inputMap.put("macData", exchange.getProperty("macData", String.class));
		
		HSMCommand_D132 d132 = new HSMCommand_D132(inputMap);
		String d132msg = d132.packageInputField();
		System.out.println("d132 message: " + d132msg + " Starting command D132");
		byte[] bytelst = StringUtil.ASCII_To_BCD(d132msg.getBytes(), d132msg.length());		
		exchange.getOut().setBody(bytelst);
	}
	
	public void responseHsmD132Process(Exchange exchange) throws Exception{
		
		byte[] bytes = exchange.getIn().getBody(byte[].class);;
		String d132Res = StringUtil.bcd2Str(bytes, bytes.length);
		//System.out.println("d132 returns: " + d132Res);
		log.info("d132 returns: " + d132Res);
		String mac=null;
		if (d132Res != null && d132Res.length() > 2 && d132Res.substring(0, 2).equals("41")) {
			mac = d132Res.substring(2);
		}
		
		if (mac.equals(null)){
			//System.out.println("D132 failed .... ");
			log.error("d132 failed .... ");
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR,"D132 return error, " + d132Res);
		}
		
		exchange.setProperty("mac", mac);
	}
	
	public void requestHsmD134Process(Exchange exchange) throws Exception{
		
		TerminalKey terminalKey = (TerminalKey)exchange.getProperties().get("terminalKey");	
		Map<String, String> inputMap = new HashMap<String, String>();
		
		inputMap.put("macKeyLen", "HSM_KEYLEN_2");
		inputMap.put("macKey", terminalKey.getMacKey());
		inputMap.put("mac", exchange.getProperty("mac", String.class));
		inputMap.put("macData", exchange.getProperty("macData", String.class));		
		
		HSMCommand_D134 d134 = new HSMCommand_D134(inputMap);
		String d134msg = d134.packageInputField();
		System.out.println("d134 message: " + d134msg + " Starting command D134");
		byte[] bytelst = StringUtil.ASCII_To_BCD(d134msg.getBytes(), d134msg.length());		
		exchange.getOut().setBody(bytelst);
	}
	
	public void responseHsmD134Process(Exchange exchange) throws Exception{
		
		byte[] bytes = exchange.getIn().getBody(byte[].class);;
		String d134Res = StringUtil.bcd2Str(bytes, bytes.length);
		log.info("d134 returns: " + d134Res);
		String mac=null;
		if (!(d134Res != null && d134Res.length() >= 2 && d134Res.substring(0, 2).equals("41"))) {
			log.error("d134 failed .... ");
			throw new PowerswitchException(PowerSwitchConstant.HSM_ERROR,"D134 return error, " + d134Res);
		}
	}
}
