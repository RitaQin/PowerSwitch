package com.ncr.powerswitch.processor;

import static com.ncr.powerswitch.utils.ResourcesTool.IL8N_RESOURCES_DEFAULT;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.CHANNEL_ID_NULL_ERROR;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.TRANSACTION_CODE_NULL_ERROR;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.TERMINAL_ID_NULL_ERROR;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.TRACE_NUMBER_NULL_ERROR;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.TRANSACTION_DATE_NULL_ERROR;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.TRANSACTION_TIME_NULL_ERROR;

import static com.ncr.powerswitch.utils.ResourcesTool.getText;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.restlet.engine.header.Header;
import org.restlet.util.Series;

import com.ncr.powerswitch.utils.FormatUtil;
import com.ncr.powerswitch.utils.ReturnMsgUtil;
import com.ncr.powerswitch.utils.SnowflakeIdGenerator;

/***
 * 报文格式预处理，验证报文头信息 报文头实例 { "head": { "channelId":"BS",
 * "transactionCode":"BSJC001", "terminalId":"20001234",
 * "traceNumber":"00000001", "transactionDate":"20180906",
 * "transactionTime":"104405" } }
 * 
 * @author rq185015
 *
 */

public class RequestHeaderProcessor implements Processor {

	/** 工作机器ID(0~31) */
	public long workerId = 1;
	/** 数据中心ID(0~31) */
	public long datacenterId = 1;

	private final static Log log = LogFactory.getLog(RequestHeaderProcessor.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		
		String jsonStr = FormatUtil.stream2Str((InputStream) exchange.getIn().getBody());
		
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		// 将请求报文转为map存入context
		jsonMap = FormatUtil.json2Map(jsonStr);
		
		@SuppressWarnings("unchecked")
		Map<String, String> head = (Map<String, String>) jsonMap.get("head"); 
		@SuppressWarnings("unchecked")
		Map<String, String> body = (Map<String, String>) jsonMap.get("body");
		// 开始验证报文头的有效性
		String channelId = head.get("channelId") == null ? null
				: head.get("channelId").toString();
		String transactionCode = head.get("transactionCode") == null ? null
				: head.get("transactionCode").toString();
		String terminalId = head.get("terminalId") == null ? null
				: head.get("terminalId").toString();
		String traceNumber = head.get("traceNumber") == null ? null
				: head.get("traceNumber").toString();
		String transactionDate = head.get("transactionDate") == null ? null
				: head.get("transactionDate").toString();
		String transactionTime = head.get("transactionTime") == null ? null
				: head.get("transactionTime").toString();

		String errorMsg = null;

		if (channelId == null) {
			errorMsg = ReturnMsgUtil.generateErrorMessage(CHANNEL_ID_NULL_ERROR,
					getText(IL8N_RESOURCES_DEFAULT, CHANNEL_ID_NULL_ERROR));
			exchange.getOut().setBody(errorMsg);
			return;
		}
		if (transactionCode == null) {
			errorMsg = ReturnMsgUtil.generateErrorMessage(TRANSACTION_CODE_NULL_ERROR,
					getText(IL8N_RESOURCES_DEFAULT, TRANSACTION_CODE_NULL_ERROR));
			exchange.getOut().setBody(errorMsg);
			return;
		}
		if (terminalId == null) {
			errorMsg = ReturnMsgUtil.generateErrorMessage(TERMINAL_ID_NULL_ERROR,
					getText(IL8N_RESOURCES_DEFAULT, TERMINAL_ID_NULL_ERROR));
			exchange.getOut().setBody(errorMsg);
			return;
		}
		if (traceNumber == null) {
			errorMsg = ReturnMsgUtil.generateErrorMessage(TRACE_NUMBER_NULL_ERROR,
					getText(IL8N_RESOURCES_DEFAULT, TRACE_NUMBER_NULL_ERROR));
			exchange.getOut().setBody(errorMsg);
			return;
		}
		if (transactionDate == null) {
			errorMsg = ReturnMsgUtil.generateErrorMessage(TRANSACTION_DATE_NULL_ERROR,
					getText(IL8N_RESOURCES_DEFAULT, TRANSACTION_DATE_NULL_ERROR));
			exchange.getOut().setBody(errorMsg);
			return;
		}
		if (transactionTime == null) {
			errorMsg = ReturnMsgUtil.generateErrorMessage(TRANSACTION_TIME_NULL_ERROR,
					getText(IL8N_RESOURCES_DEFAULT, TRANSACTION_TIME_NULL_ERROR));
			exchange.getOut().setBody(errorMsg);
			return;
		}

		// TODO:验证terminalId

		// 生成唯一的powerswitch流水号
		SnowflakeIdGenerator generator = new SnowflakeIdGenerator(workerId, datacenterId);
		long snowId = generator.nextId();
		
		Map<String, Object> requestMap = new HashMap<String, Object>();

		// 添加必输报文头
		requestMap.put("CnlInd", channelId);
		requestMap.put("TranCode", transactionCode);
		requestMap.put("ConsumerId", terminalId); // TODO: 需要确认
		requestMap.put("ConsumerSeqNo", snowId); // TODO: 需要确认
		requestMap.put("TranDate", transactionDate);
		requestMap.put("TranTime", transactionTime);
		requestMap.put("FileFlag", "0");
		requestMap.putAll(body);

		//将交易标识放入上下文的header中
		exchange.getIn().setHeader("TranCode", transactionCode); 
		// 校验完毕向后传递
		exchange.getIn().setBody(requestMap);
	}
}
