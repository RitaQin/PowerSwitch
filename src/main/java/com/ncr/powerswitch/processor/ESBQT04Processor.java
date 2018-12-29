package com.ncr.powerswitch.processor;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ncr.powerswitch.utils.FormatUtil;
import com.ncr.powerswitch.utils.XStreamEx;

import com.ncr.powerswitch.esb.ESB_QT04;
import com.ncr.powerswitch.esb.model.AppHead_QT04;
import com.ncr.powerswitch.esb.model.Body_QT04;
import com.ncr.powerswitch.esb.model.LocalHead_QT04;
import com.ncr.powerswitch.esb.model.Ret_QT04;
import com.ncr.powerswitch.esb.model.SysHead_QT04;
import com.ncr.powerswitch.esb.model.service_QT04;
import com.thoughtworks.xstream.io.xml.DomDriver;

/***
 * 
 * @author cc185015
 *
 */

public class ESBQT04Processor {

	/**
	 * Log4j记录日志的工具类
	 */
	private final static Log log = LogFactory.getLog(ESBQT04Processor.class);	

	public void formatProcess(Exchange exchange) throws Exception{		
		@SuppressWarnings("unchecked")
		Map<String, Object> requestMap =  exchange.getIn().getBody(Map.class);
		//log.info("QT04 PROCESSING : request msg: " + msg);
		exchange.getProperties().put("channelId", requestMap.get("channelId"));
		exchange.getProperties().put("terminalId", requestMap.get("terminalId"));
		exchange.getProperties().put("traceNumber", requestMap.get("traceNumber"));
		exchange.getProperties().put("transactionDate", requestMap.get("transactionDate"));
		exchange.getProperties().put("transactionTime", requestMap.get("transactionTime"));
		
		ESB_QT04 testEsbQT04 = new ESB_QT04();
		String requestString = testEsbQT04.constructRequest(requestMap);		
		exchange.getOut().setBody(requestString);		
	}
	
	public void deformatProcess(Exchange exchange) throws Exception{
		
		XStreamEx  xstream = new XStreamEx(new DomDriver("utf-8"));
		xstream.alias("service", service_QT04.class);
		xstream.alias("SYS_HEAD", SysHead_QT04.class);
		xstream.alias("Ret", Ret_QT04.class);
		xstream.alias("APP_HEAD", AppHead_QT04.class);
		xstream.alias("LOCAL_HEAD", LocalHead_QT04.class);
		xstream.alias("BODY", Body_QT04.class);
		service_QT04 esbMsg = (service_QT04) xstream.fromXML(exchange.getIn().getBody(String.class));
		
		Map<String, Object> head = new HashMap<String, Object>(); 
		
		head.put("channelId", (String)exchange.getProperties().get("channelId"));
		head.put("transactionCode", "QT04");
		head.put("terminalId", (String)exchange.getProperties().get("terminalId"));
		head.put("traceNumber", (String)exchange.getProperties().get("traceNumber"));
		head.put("transactionDate", (String)exchange.getProperties().get("transactionDate"));
		head.put("transactionTime", (String)exchange.getProperties().get("transactionTime"));
		head.put("errorCode", esbMsg.getSYS_HEAD().getArray().get(0).getReturnCode());
		head.put("errorMessage", esbMsg.getSYS_HEAD().getArray().get(0).getReturnMsg());
		
		Map<String, Object> body = new HashMap<String, Object>();
		
		Map<String, Object> retJsonMap = new HashMap<String, Object>();		
		
		retJsonMap.put("head", head);
		retJsonMap.put("body", body);
		exchange.getOut().setBody(FormatUtil.map2Json(retJsonMap));	
	}
	
	public void testProcess(Exchange exchange) throws Exception{		
		@SuppressWarnings("unchecked")
		String msg = exchange.getIn().getBody(String.class);		
		
		//Map<String, Object> requestMap = FormatUtil.json2Map(msg);
		String retmsg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><service><SYS_HEAD><ServiceCode>12001000001</ServiceCode><ServiceScene>06</ServiceScene><ConsumerId>10102101</ConsumerId><TargetId></TargetId><ChannelType></ChannelType><OrgConsumerId></OrgConsumerId><ConsumerSeqNo></ConsumerSeqNo><OrgConsumerSeqNo></OrgConsumerSeqNo><TranDate>2018-12-23</TranDate><TranTime></TranTime><ServSeqNo>20223028</ServSeqNo><ReturnStatus>S</ReturnStatus><array><Ret><ReturnCode>000000</ReturnCode><ReturnMsg></ReturnMsg></Ret></array><TerminalCode></TerminalCode><OrgTerminalCode></OrgTerminalCode><ConsumerSvrId></ConsumerSvrId><OrgConsumerSvrId></OrgConsumerSvrId><DestSvrId></DestSvrId><UserLang></UserLang><FileFlag></FileFlag><SrcFilePath></SrcFilePath><SrcFileName></SrcFileName><DestFilePath></DestFilePath><DestFileName></DestFileName></SYS_HEAD><APP_HEAD><TranTellerNo></TranTellerNo><TranBranchId>07705</TranBranchId><TranTellerPassword></TranTellerPassword><TranTellerLevel></TranTellerLevel><TranTellerType></TranTellerType><ApprFlag></ApprFlag><AuthFlag></AuthFlag><array></array><array></array></APP_HEAD><LOCAL_HEAD><TranCode>AC51</TranCode><SendCardPeriod></SendCardPeriod></LOCAL_HEAD><BODY><TxnAmt>0.00</TxnAmt><DbAcgSbjNo>10101       </DbAcgSbjNo><CrAcgSbjNo>21103       </CrAcgSbjNo><TfrOutAcctBal>0.00</TfrOutAcctBal><TfrInAcctBal>0.00</TfrInAcctBal><InfctTxnAmt>3400.00</InfctTxnAmt><AcgBnkNo>03301</AcgBnkNo><ERetStmtNo>2022302820181223</ERetStmtNo><DbCrInst>0770503301</DbCrInst></BODY></service>";
		
		
		//ESB_QT04 testEsbQT04 = new ESB_QT04();	
		exchange.getOut().setBody(retmsg);		
	}
	
//	public void beforeMybatisProcess(Exchange exchange) throws Exception{		
//		Map<String, Object> requestMap =  exchange.getIn().getBody(Map.class);
//		exchange.getProperties().put("channelId", requestMap.get("channelId"));			
//		exchange.getOut().setBody(requestMap.get("terminalId"));		
//	}
//	
//	public void afterMybatisProcess(Exchange exchange) throws Exception{		
//		Terminal terminal =  exchange.getIn().getBody(Terminal.class);			
//		exchange.getOut().setBody(terminal.terminalId);		
//	}	

}
