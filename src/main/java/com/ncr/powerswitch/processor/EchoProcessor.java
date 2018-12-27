package com.ncr.powerswitch.processor;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.ncr.powerswitch.DAO.HsmDao;
import com.ncr.powerswitch.DAO.HsmDaoImpl;
import com.ncr.powerswitch.persistIntf.EppTableMapper;
import com.ncr.powerswitch.persistIntf.TerminalKeyTableMapper;
import com.ncr.powerswitch.persistIntf.TerminalTableMapper;


public class EchoProcessor implements BaseProcessor{

	@Override
	public void process(Exchange exchange) throws Exception {
		System.out.println("PowerSwitch Running");
		
		CamelContext context = exchange.getContext();
		SqlSessionFactory sessionFactory = (SqlSessionFactory) context.getRegistry().lookupByName("sqlSessionFactory");
		SqlSession session = sessionFactory.openSession();
		EppTableMapper eppMapper = session.getMapper(EppTableMapper.class);
		TerminalKeyTableMapper tkMapper = session.getMapper(TerminalKeyTableMapper.class);
		TerminalTableMapper termMapper = session.getMapper(TerminalTableMapper.class);
		
		HsmDao hsmDao = new HsmDaoImpl(eppMapper, tkMapper, termMapper);
		
		
		exchange.getOut().setBody("PowerSwitch Connected!");
	}
	
}
