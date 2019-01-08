package com.ncr.powerswitch.processor;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.buffer.SimpleBufferAllocator;

import com.ncr.powerswitch.hsm.HSMCommand_D107;
import com.ncr.powerswitch.DAO.HsmDao;
import com.ncr.powerswitch.DAO.HsmDaoImpl;
import com.ncr.powerswitch.dataObject.TerminalKey;
import com.ncr.powerswitch.hsm.HSMCommand_D104;
import com.ncr.powerswitch.hsm.HSMSocketClient;
import com.ncr.powerswitch.persistIntf.EppTableMapper;
import com.ncr.powerswitch.persistIntf.TerminalKeyTableMapper;
import com.ncr.powerswitch.persistIntf.TerminalTableMapper;
import com.ncr.powerswitch.utils.FormatUtil;
import com.ncr.powerswitch.utils.ResponseCode;
import com.ncr.powerswitch.utils.StringUtil;
import com.ncr.powerswitch.utils.TestUtil;

import com.ncr.powerswitch.dataObject.TerminalKey;

/**
 * ��ɹ�����Կ
 * ÿ̨�ն˶�Ӧ3�鹤����Կ
 * MACKEY MACKEYNEW
 * PINKEY PINKEYNEW
 * TRACEKEY TRACEKEYNEW 
 * 
 * 
 * STEPS: 
 * 1. ���MACKEY  - D107
 * 2. ��MACKEY����ת���ܣ�����0110) -D104
 * 3. ��ת���ܺ������MACKEY������ݿ⣬MASTERKEY���ܺ��MAC�����ն�
 * 
 * �ظ����ϲ����ȡ����PINKEY��TRACEKEY 
 *  
 * @author rq185015
 *
 */

public class RqkNewProcessorTest implements BaseProcessor {
	
	public void responseD107(Exchange exchange) throws Exception{
		byte[] bytes = exchange.getIn().getBody(byte[].class);	
		String d104Res = StringUtil.bcd2Str(bytes, bytes.length);
		System.out.println("d107 test receive: " + d104Res);
		
		String responseD107 = "4110310D5813FB5E44E7813BFA22A3FB47BD65765A985CB680DC846A3DBDB44BFC92E9A53C5D52FFB182";
		byte[] bytelst = StringUtil.ASCII_To_BCD(responseD107.getBytes(), responseD107.length());			
		exchange.getOut().setBody(bytelst);
	}
	
	public void responseD104(Exchange exchange) throws Exception{
		byte[] bytes = exchange.getIn().getBody(byte[].class);
		String d104Res = StringUtil.bcd2Str(bytes, bytes.length);
		System.out.println("d104 test receive: " + d104Res);
		
		String responseD104 = "4110F54D366FDEF91DF3E5A0F1632F4A250FE9A53C5D52FFB182";
		byte[] bytelst = StringUtil.ASCII_To_BCD(responseD104.getBytes(), responseD104.length());		
		exchange.getOut().setBody(bytelst);
	}
	
	public void storeMasterkeyProcess(Exchange exchange) throws Exception{
		TerminalKey terminalKey =  exchange.getIn().getBody(TerminalKey.class);
		exchange.getProperties().put("TerminalKey", terminalKey);
		//exchange.getProperties().put("MasterKey", terminalKey.getMasterKey());
	}
	
	public void requestHsmD107Process(Exchange exchange, String keyType) throws Exception{
		Map<String, String> inputMap = new HashMap<String, String>();
		inputMap.put("masterKey", ((TerminalKey)exchange.getProperties().get("TerminalKey")).getMasterKey());
		inputMap.put("keyType", keyType);
		HSMCommand_D107 d107 = new HSMCommand_D107(inputMap);
		String d107msg = d107.packageInputField();
		System.out.println("d107 message: " + d107msg + " Starting command D107" + " ,KeyType:" + keyType);
		exchange.getOut().setBody(d107msg);
	}
	
	public void responseHsmD107Process(Exchange exchange, String keyType) throws Exception{
		String d107Res = exchange.getIn().getBody(String.class);
		System.out.println("d107 returns: " + d107Res + " ,KeyType:" + keyType);
		String keyByHsm = null; 
		String keyByTsf = null;
		String checkVal = null;
		if (d107Res != null && d107Res.length() > 2 && d107Res.substring(0, 2).equals("41")) {
			int keyLen = Integer.parseInt(d107Res.substring(2, 4), 16); 
			keyByHsm = d107Res.substring(4, 4 + keyLen * 2);
			System.out.println(keyType + " key by hsm " + keyByHsm);
			keyByTsf = d107Res.substring(4 + keyLen * 2, 4 + keyLen * 4);
			System.out.println(keyType + " key by tfs " + keyByTsf);
			checkVal = d107Res.substring(4 + keyLen * 4, 20 + keyLen * 4);
		}
		if (keyByHsm.equals(null) || keyByTsf.equals(null) || checkVal.equals(null)) {
			System.out.println("D107 failed..");			
		}
		
		Map<String, String> keySet = new HashMap<String, String>();		
		keySet.put("keyByHsm", keyByHsm);
		keySet.put("keyByTsf", keyByTsf);
		keySet.put("checkVal", checkVal);		
		exchange.getProperties().put(keyType,keySet);		
	}
	
	public void requestHsmD104Process(Exchange exchange, String keyType) throws Exception{
		
		Map<String, String> keySet = (Map<String, String>)exchange.getProperties().get(keyType);
		Map<String, String> inputMap = new HashMap<String, String>();
		inputMap.put("workKey", keySet.get("keyByHsm"));
		inputMap.put("keyIndex", "0110");
		
		HSMCommand_D104 d104 = new HSMCommand_D104(inputMap);
		String d104msg = d104.packageInputField();
		System.out.println("d104 message: " + d104msg + " Starting command D104" + " ,KeyType:" + keyType);
		exchange.getOut().setBody(d104msg);		
	}
	
	public void responseHsmD104Process(Exchange exchange, String keyType) throws Exception{
		String d104Res = exchange.getIn().getBody(String.class);
		System.out.println("d104 returns: " + d104Res);
		String keyByHost = null;
		if (d104Res != null && d104Res.length() > 2 && d104Res.substring(0, 2).equals("41")) {
			int keyLen = Integer.parseInt(d104Res.substring(2, 4), 16); 
			keyByHost = d104Res.substring(4, 4 + keyLen * 2);
		}
		
		if(keyByHost.equals(null)) {
			System.out.println("D104 failed .... ");
		}
		Map<String, String> keySet = (Map<String, String>)exchange.getProperties().get(keyType);	
		keySet.put("keyByHost", keyByHost);
		exchange.getProperties().put(keyType,keySet);		
	}
	
	public void updateTerminalKeyProcess(Exchange exchange) throws Exception{
		
		Map<String, Object> body = new HashMap<String, Object>();
		TerminalKey terminalKey = (TerminalKey)exchange.getProperties().get("TerminalKey");
		
		Map<String, String> pinKeySet = (Map<String, String>)exchange.getProperties().get("HSM_KEYTYPE_PIN");
		terminalKey.setPinKey(pinKeySet.get("keyByTsf"));
		terminalKey.setPinkeyHsm(pinKeySet.get("keyByHost"));
		
		Map<String, String> traceKeySet = (Map<String, String>)exchange.getProperties().get("HSM_KEYTYPE_TRACE");
		terminalKey.setTraceKey(traceKeySet.get("keyByTsf"));
		terminalKey.setTraceKeyHsm(traceKeySet.get("keyByHost"));
		
		Map<String, String> macKeySet = (Map<String, String>)exchange.getProperties().get("HSM_KEYTYPE_MAC");	
		terminalKey.setMacKey(macKeySet.get("keyByTsf"));
		terminalKey.setMacKeyHsm(macKeySet.get("keyByHost"));
		terminalKey.setKeyIndex("0110");	
		
		exchange.getOut().setBody(terminalKey);
	}
	
	public void responseTerminalKeyProcess(Exchange exchange) throws Exception{
		Map<String, Object> head = new HashMap<String, Object>(); 
		head.put("channelId", exchange.getProperties().get("channelId"));
		head.put("transactionCode", exchange.getProperties().get("transactionCode"));
		head.put("terminalId", exchange.getProperties().get("terminalId"));
		head.put("channelId", exchange.getProperties().get("channelId"));
		head.put("traceNumber", exchange.getProperties().get("traceNumber"));
		head.put("transactionDate", exchange.getProperties().get("transactionDate"));
		head.put("transactionTime", exchange.getProperties().get("transactionTime"));
		head.put("responseCode", ResponseCode.RESPONSE_SUCCESS);
		
		Map<String, Object> body = new HashMap<String, Object>();
		Map<String, String> pinKeySet = (Map<String, String>)exchange.getProperties().get("HSM_KEYTYPE_PIN");
		body.put("pinKey", pinKeySet.get("keyByTsf"));
		body.put("pinKeyCheck", pinKeySet.get("checkVal"));
		
		Map<String, String> traceKeySet = (Map<String, String>)exchange.getProperties().get("HSM_KEYTYPE_TRACE");
		body.put("traceKey", traceKeySet.get("keyByTsf"));
		body.put("traceKeyCheck", traceKeySet.get("checkVal"));
		
		Map<String, String> macKeySet = (Map<String, String>)exchange.getProperties().get("HSM_KEYTYPE_MAC");
		body.put("macKey", macKeySet.get("keyByTsf"));
		body.put("macKeyCheck", macKeySet.get("checkVal"));		
		
		Map<String, Object> retJsonMap = new HashMap<String, Object>();
		retJsonMap.put("head", head);
		retJsonMap.put("body", body);
		
		exchange.getOut().setBody(FormatUtil.map2Json(retJsonMap));	
	}
	
	
	@Override
	public void process(Exchange exchange) throws Exception {
		
		CamelContext context = exchange.getContext();
		SqlSessionFactory sessionFactory = (SqlSessionFactory) context.getRegistry().lookupByName("sqlSessionFactory");
		SqlSession session = sessionFactory.openSession();
		EppTableMapper hsmMapper = session.getMapper(EppTableMapper.class);
		TerminalKeyTableMapper tkMapper = session.getMapper(TerminalKeyTableMapper.class);
		TerminalTableMapper termMapper = session.getMapper(TerminalTableMapper.class);
		
		HsmDao hsmDao = new HsmDaoImpl(hsmMapper, tkMapper, termMapper);
		
		@SuppressWarnings("unchecked")
		Map<String, Object> requestMap =  exchange.getIn().getBody(Map.class);
		Map<String, Object> body = new HashMap<String, Object>();
		TerminalKey terminalKey = new TerminalKey(); 
		terminalKey.setTerminalId(requestMap.get("terminalId").toString());
		
		String masterkey = hsmDao.getMasterKeyByTerminalId(requestMap.get("terminalId").toString());
		Map<String, String> pinKeySet = obtainWorkKeySets( masterkey, "HSM_KEYTYPE_PIN");
		body.put("pinKey", pinKeySet.get("keyByTsf"));
		body.put("pinKeyCheck", pinKeySet.get("checkVal"));
		terminalKey.setPinKey(pinKeySet.get("keyByTsf"));
		terminalKey.setPinkeyHsm(pinKeySet.get("keyByHost"));
		
		Map<String, String> traceKeySet = obtainWorkKeySets( masterkey, "HSM_KEYTYPE_TRACE");
		body.put("traceKey", traceKeySet.get("keyByTsf"));
		body.put("traceKeyCheck", traceKeySet.get("checkVal"));
		terminalKey.setTraceKey(traceKeySet.get("keyByTsf"));
		terminalKey.setTraceKeyHsm(traceKeySet.get("keyByHost"));
		
		Map<String, String> macKeySet = obtainWorkKeySets( masterkey, "HSM_KEYTYPE_MAC");
		body.put("macKey", macKeySet.get("keyByTsf"));
		body.put("macKeyCheck", macKeySet.get("checkVal"));
		terminalKey.setMacKey(macKeySet.get("keyByTsf"));
		terminalKey.setMacKeyHsm(macKeySet.get("keyByHost"));
		terminalKey.setKeyIndex("0110");
		
		if (!hsmDao.updateTerminalKey(terminalKey)) {
			exchange.getOut().setBody("updating terminal key failed..");
		}
		
		Map<String, Object> head = new HashMap<String, Object>(); 
		head.put("channelId", requestMap.get("channelId"));
		head.put("transactionCode", requestMap.get("transactionCode"));
		head.put("terminalId", requestMap.get("terminalId"));
		head.put("channelId", requestMap.get("channelId"));
		head.put("traceNumber", requestMap.get("traceNumber"));
		head.put("transactionDate", requestMap.get("transactionDate"));
		head.put("transactionTime", requestMap.get("transactionTime"));
		head.put("responseCode", ResponseCode.RESPONSE_SUCCESS);
		
		Map<String, Object> retJsonMap = new HashMap<String, Object>();
		retJsonMap.put("head", head);
		retJsonMap.put("body", body);
		
		exchange.getOut().setBody(FormatUtil.map2Json(retJsonMap));	
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
