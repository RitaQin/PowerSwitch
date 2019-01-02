package com.ncr.powerswitch.processor;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

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

public class UtilProcessor {
	
	public void httpToHsm(Exchange exchange) throws Exception{
		String msg =  exchange.getIn().getBody(String.class);
		byte[] bytelst = StringUtil.ASCII_To_BCD(msg.getBytes(), msg.length());		
		exchange.getOut().setBody(bytelst);
	}
	
	public void hsmToHttp(Exchange exchange) throws Exception{
		byte[] bytes = exchange.getIn().getBody(byte[].class);
		String msg = StringUtil.bcd2Str(bytes, bytes.length);	
		exchange.getOut().setBody(msg);
	}	
}
