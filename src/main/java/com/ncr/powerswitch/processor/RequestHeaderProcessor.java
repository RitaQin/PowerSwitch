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
 * pre-action processor 报文格式预处理，验证报文头信息 报文头实例 { "head": { "channelId":"BS",
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

		// 将收到的请求转化为map向后传递
		@SuppressWarnings("unchecked")
		Series<Header> headers = (Series<Header>) exchange.getIn().getHeader("org.restlet.http.headers");
		String body = FormatUtil.stream2Str((InputStream) exchange.getIn().getBody());
		// 开始验证报文头的有效性
		String channelId = headers.getFirstValue("channelId", true) == null ? null
				: headers.getFirstValue("channelId", true).toString();
		System.out.println("channel Id: " + channelId);
		String transactionCode = headers.getFirstValue("transactionCode", true) == null ? null
				: headers.getFirstValue("transactionCode", true).toString();
		String terminalId = headers.getFirstValue("terminalId", true) == null ? null
				: headers.getFirstValue("terminalId", true).toString();
		String traceNumber = headers.getFirstValue("traceNumber", true) == null ? null
				: headers.getFirstValue("traceNumber", true).toString();
		String transactionDate = headers.getFirstValue("transactionDate", true) == null ? null
				: headers.getFirstValue("transactionDate", true).toString();
		String transactionTime = headers.getFirstValue("transactionTime", true) == null ? null
				: headers.getFirstValue("transactionTime", true).toString();

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
		// 将请求报文转为map存入context
		requestMap = FormatUtil.json2Map(body);
		// 添加必输报文头
		requestMap.put("CnlInd", channelId);
		requestMap.put("TranCode", transactionCode);
		requestMap.put("ConsumerId", terminalId); // TODO: 需要确认
		requestMap.put("ConsumerSeqNo", traceNumber); // TODO: 需要确认
		requestMap.put("OrgConsumerSeqNo", snowId); // powerswitch 流水号
		requestMap.put("TranDate", transactionDate);
		requestMap.put("TranTime", transactionTime);
		requestMap.put("FileFlag", "0");

		System.out.println(requestMap);
		// 校验完毕向后传递
		exchange.getOut().setBody(requestMap);
	}
}
