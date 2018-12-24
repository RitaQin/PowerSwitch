package com.ncr.powerswitch.processor;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.ncr.powerswitch.DAO.HsmDao;
import com.ncr.powerswitch.DAO.HsmDaoImpl;
import com.ncr.powerswitch.dataObject.EppKey;
import com.ncr.powerswitch.dataObject.TerminalKey;
import com.ncr.powerswitch.hsm.HSMCommand_C046;
import com.ncr.powerswitch.hsm.HSMCommand_C047;
import com.ncr.powerswitch.hsm.HSMCommand_C049;
import com.ncr.powerswitch.hsm.HSMCommand_D106;
import com.ncr.powerswitch.hsm.HSMSocketClient;
import com.ncr.powerswitch.persistIntf.EppTableMapper;
import com.ncr.powerswitch.persistIntf.TerminalKeyTableMapper;
import com.ncr.powerswitch.persistIntf.TerminalTableMapper;
import com.ncr.powerswitch.utils.FormatUtil;
import com.ncr.powerswitch.utils.ResponseCode;

public class DataKeyProcessor implements BaseProcessor {

	@Override
	public void process(Exchange exchange) throws Exception {
		
		CamelContext context = exchange.getContext();
		SqlSessionFactory sessionFactory = (SqlSessionFactory) context.getRegistry().lookupByName("sqlSessionFactory");
		SqlSession session = sessionFactory.openSession();
		EppTableMapper hsmMapper = session.getMapper(EppTableMapper.class);
		TerminalKeyTableMapper tkMapper = session.getMapper(TerminalKeyTableMapper.class);
		TerminalTableMapper termMapper = session.getMapper(TerminalTableMapper.class);
		
		HsmDao hsmDao = new HsmDaoImpl(hsmMapper, tkMapper, termMapper);
		
		TerminalKey terminalKey = new TerminalKey(); 
		@SuppressWarnings("unchecked")
		Map<String, Object> requestMap = exchange.getIn().getBody(Map.class);
		
		Map<String, String> inputMap = new HashMap<String, String>();
		System.out.println(requestMap);
		EppKey eppInfo = hsmDao.getEppInfoByTerminalId(requestMap.get("terminalId").toString());
		String vendorKey = eppInfo.getManupk();
		inputMap.put("MANUPK", vendorKey);
		inputMap.put("MANUPKLENGTH", Integer.toString(vendorKey.length()));
		inputMap.put("strEppPublicKey", requestMap.get("eppPublicKey").toString());
		inputMap.put("strEppPublicKeySign", requestMap.get("eppPublicKeySign").toString());
		System.out.println("Starting command C047");
		HSMCommand_C047 c047 = new HSMCommand_C047(inputMap);
		String c047_msg = c047.packageInputField();
		String c047Res = HSMSocketClient.sendAndReceivePacket(c047_msg, "8.99.9.91", "3000", false);
		System.out.println("verifying C047 Return " + c047Res);
		if (c047Res != null) {
			if (c047Res.length() == 20 && c047Res.substring(0, 2).equals("41")
					&& c047Res.substring(18, 20).equals("00")) {
				System.out.println("C047  驗證通過" + c047Res);
			} else {
				exchange.getOut().setBody("C047 未通过验证  " + c047Res);
			}
		} else {
			exchange.getOut().setBody("C047 未返回随机密钥");
		}

		System.out.println("Starting command D106");
		HSMCommand_D106 d106 = new HSMCommand_D106();
		String d106_msg = d106.packageInputField();
		String returnMsg = HSMSocketClient.sendAndReceivePacket(d106_msg, "8.99.9.91", "3000", false);
		System.out.println("verifying D106 Return " + returnMsg);
		String masterKey = null;
		String checkCode = null;
		if (returnMsg != null) {
			if (returnMsg.length() == 52 && returnMsg.substring(0, 2).equals("41")) {
				masterKey = returnMsg.substring(4, 36);
				System.out.println("Master Key is " + masterKey);
				terminalKey.setMasterKey(masterKey);
				checkCode = returnMsg.substring(36, 52);
				terminalKey.setMasterKeyCheck(checkCode);
				hsmDao.insertMasterKey(terminalKey);

			} else {
				exchange.getOut().setBody("D106格式未通过验证  " + returnMsg);
			}
		} else {
			exchange.getOut().setBody("D106 未返回随机密钥");
		}
	

		terminalKey.setTerminalId(requestMap.get("terminalId").toString());
		terminalKey.setMasterKey(masterKey);
		terminalKey.setMasterKeyCheck(checkCode);
		hsmDao.insertMasterKey(terminalKey);
		
		
		System.out.println("Starting Command C049");
		inputMap.put("KEY_TEXT", masterKey);
		inputMap.put("userReservedStr", "0000000000000000");
		HSMCommand_C049 c049 = new HSMCommand_C049(inputMap);
		String c049Msg = c049.packageInputField();
		String c049Res = HSMSocketClient.sendAndReceivePacket(c049Msg, "8.99.9.91", "3000", false);
		System.out.println("verifying C049 Return " + c049Res);
		String encryptedMk = null;
		if (c049Res != null) {
			if (c049Res.substring(0, 2).equals("41")) {
				encryptedMk = c049Res.substring(22, c049Res.length());
				System.out.println("C049 轉加密文： " + encryptedMk);
			} else {
				System.out.println("C049 not verified  " + c049Res);
				exchange.getOut().setBody("C049 not verified  " + c049Res);
			}
		} else {
			System.out.println("C049 returns null");
			exchange.getOut().setBody("C049 returns null");
		}

		System.out.println("Starting Command C046");
		String sk = eppInfo.getSk();
		inputMap.put("SK", sk);
		inputMap.put("encryption", encryptedMk);
		HSMCommand_C046 c046 = new HSMCommand_C046(inputMap);
		String c046Msg = c046.packageInputField();
		String encryptSign = null;
		
		String c046Res = HSMSocketClient.sendAndReceivePacket(c046Msg, "8.99.9.91", "3000", false);
		if (c046Res != null && c046Res.substring(0, 2).equals("41")) {
			System.out.println("C046 密文簽名： " + c046Res.substring(22, c046Res.length()));
			encryptSign =  c046Res.substring(22, c046Res.length());
			
		} else {
			exchange.getOut().setBody("C046未通過");
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
		
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("MasterKeyCypher", encryptedMk);
		body.put("MasterKeyCypherSign",encryptSign);
		body.put("MasterKeyCheckCode", checkCode);
		body.put("BankPublicKey", eppInfo.getBankPK());
		body.put("BankPublicKeySign", eppInfo.getBankPkSignature());
		
		Map<String, Object> retJsonMap = new HashMap<String, Object>(); 
		retJsonMap.put("head", head); 
		retJsonMap.put("body", body);
		// 构造JSON并放入上下文
		exchange.getOut().setBody(FormatUtil.map2Json(retJsonMap));
	}
}
