package com.ncr.powerswitch.processor;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;

import com.ncr.powerswitch.hsm.HSMCommand_D107;
import com.ncr.powerswitch.hsm.HSMCommand_D104;
import com.ncr.powerswitch.hsm.HSMSocketClient;
import com.ncr.powerswitch.utils.FormatUtil;
import com.ncr.powerswitch.utils.TestUtil;

/**
 * 生成工作密钥
 * 每台终端对应3组工作密钥
 * MACKEY MACKEYNEW
 * PINKEY PINKEYNEW
 * TRACEKEY TRACEKEYNEW 
 * 
 * 
 * STEPS: 
 * 1. 生成MACKEY  - D107
 * 2. 对MACKEY进行转加密（索引0110) -D104
 * 3. 将转加密后的主机MACKEY存入数据库，MASTERKEY加密后的MAC返回终端
 * 
 * 重复以上步骤获取两组PINKEY和TRACEKEY 
 *  
 * @author rq185015
 *
 */

public class RQKProcessor implements BaseProcessor {

	@Override
	public void process(Exchange exchange) throws Exception {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> requestMap =  exchange.getIn().getBody(Map.class);
		System.out.println("master Key is: " + TestUtil.MASTERKEY);
		
		Map<String, String> pinKeySet = obtainWorkKeySets( TestUtil.MASTERKEY, "HSM_KEYTYPE_PIN");
		requestMap.put("pinKey", pinKeySet.get("keyByTsf"));
		requestMap.put("pinKeyCheck", pinKeySet.get("checkVal"));
		
		Map<String, String> traceKeySet = obtainWorkKeySets( TestUtil.MASTERKEY, "HSM_KEYTYPE_TRACE");
		requestMap.put("traceKey", traceKeySet.get("keyByTsf"));
		requestMap.put("traceKeyCheck", traceKeySet.get("checkVal"));
		
		Map<String, String> macKeySet = obtainWorkKeySets( TestUtil.MASTERKEY, "HSM_KEYTYPE_MAC");
		requestMap.put("macKey", macKeySet.get("keyByTsf"));
		requestMap.put("macKeyCheck", macKeySet.get("checkVal"));
		
		exchange.getOut().setBody(FormatUtil.map2Json(requestMap));	
	}
	
	private Map<String, String> obtainWorkKeySets(String masterKey, String keyType) throws Exception{
		Map<String, String> keySet = new HashMap<String, String>();
		Map<String, String> inputMap = new HashMap<String, String>();
		inputMap.put("masterKey", masterKey);
		inputMap.put("keyType", keyType);
		HSMCommand_D107 d107 = new HSMCommand_D107(inputMap);
		String d107msg = d107.packageInputField();
		System.out.println("d107 message: " + d107msg + " Starting command D107");
		String d107Res = HSMSocketClient.sendAndReceivePacket(d107msg,  "8.99.9.91", "3000", false);
		System.out.println("d107 returns: " + d107Res);
		String keyByHsm = null; 
		String keyByTsf = null;
		String checkVal = null;
		if (d107Res != null && d107Res.length() > 2 && d107Res.substring(0, 2).equals("41")) {
			int keyLen = Integer.parseInt(d107Res.substring(2, 4), 16); 
			keyByHsm = d107Res.substring(4, 4 + keyLen * 2);
			System.out.println(inputMap.get("keyType") + " key by hsm " + keyByHsm);
			keyByTsf = d107Res.substring(4 + keyLen * 2, 4 + keyLen * 4);
			System.out.println(inputMap.get("keyType") + " key by tfs " + keyByTsf);
			checkVal = d107Res.substring(4 + keyLen * 4, 20 + keyLen * 4);
		}
		if (keyByHsm.equals(null) || keyByTsf.equals(null) || checkVal.equals(null)) {
			System.out.println("D107 failed..");
			return null;
		}
		
		inputMap.put("workKey", keyByHsm);
		inputMap.put("keyIndex", "0110");
		
		HSMCommand_D104 d104 = new HSMCommand_D104(inputMap);
		String d104msg = d104.packageInputField();
		System.out.println("d104 message: " + d104msg + " Starting command D104");
		String d104Res = HSMSocketClient.sendAndReceivePacket(d104msg,  "8.99.9.91", "3000", false);
		System.out.println("d104 returns: " + d104Res);
		String keyByHost = null;
		if (d104Res != null && d104Res.length() > 2 && d104Res.substring(0, 2).equals("41")) {
			int keyLen = Integer.parseInt(d104Res.substring(2, 4), 16); 
			keyByHost = d104Res.substring(4, 4 + keyLen * 2);
		}
		
		if(keyByHost.equals(null)) {
			System.out.println("D104 failed .... ");
			return null;
		}
		keySet.put("keyByTsf", keyByTsf);
		keySet.put("keyByHost", keyByHost);
		keySet.put("checkVal", checkVal);
		return keySet;	
	}
}
