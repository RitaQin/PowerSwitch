package com.ncr.powerswitch.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;

import com.ncr.powerswitch.DAO.HsmDao;
import com.ncr.powerswitch.DAO.HsmDaoImpl;
import com.ncr.powerswitch.dataObject.EppKey;
import com.ncr.powerswitch.dataObject.Terminal;
import com.ncr.powerswitch.persistIntf.EppTableMapper;
import com.ncr.powerswitch.persistIntf.TerminalKeyTableMapper;
import com.ncr.powerswitch.persistIntf.TerminalTableMapper;
import com.ncr.powerswitch.utils.FormatUtil;
import com.ncr.powerswitch.utils.ResponseCode;


public class EchoProcessor implements BaseProcessor{

	@Override
	public void process(Exchange exchange) throws Exception {
		System.out.println("PowerSwitch Running");
		
		Logger log = Logger.getLogger(EchoProcessor.class);
		
		CamelContext context = exchange.getContext();
		SqlSessionFactory sessionFactory = (SqlSessionFactory) context.getRegistry().lookupByName("sqlSessionFactory");
		SqlSession session = sessionFactory.openSession();
		EppTableMapper eppMapper = session.getMapper(EppTableMapper.class);
		TerminalKeyTableMapper tkMapper = session.getMapper(TerminalKeyTableMapper.class);
		TerminalTableMapper termMapper = session.getMapper(TerminalTableMapper.class);
		
		Map<String, String> head = new HashMap<String, String>();
		head.put("TerminalId", "1234567"); 
		head.put("BranchId", "54321");
		head.put("ResponseCode", ResponseCode.RESPONSE_SUCCESS);
		
		Map<String, String> body = new HashMap<String, String>();
		HsmDao hsmDao = new HsmDaoImpl(eppMapper, tkMapper, termMapper);
		EppKey eppInfo = hsmDao.getEppInfoByTerminalId("1234567");

		body.put("bankPk", eppInfo.getBankPK());
		body.put("BankPublicKeySign", eppInfo.getBankPkSignature());
		
		Map<String, Object> retJsonMap = new HashMap<String, Object>();
		retJsonMap.put("head", head);
		retJsonMap.put("body", body);
		exchange.getOut().setBody(FormatUtil.map2Json(retJsonMap));
	}
	
}
