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
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ncr.powerswitch.utils.FormatUtil;
import com.ncr.powerswitch.utils.ReturnMsgUtil;


/***
 * pre-action processor
 * 报文格式预处理，验证报文头信息
 * 报文头实例
 * {
	"head":
		{
			"channelId":"BS",
			"transactionCode":"BSJC001",
			"terminalId":"20001234",
			"traceNumber":"00000001",
			"transactionDate":"20180906",
			"transactionTime":"104405"
		}
}
 * @author rq185015
 *
 */

public class RequestHeaderProcessor implements Processor {
	
	private final static Log log = LogFactory.getLog(RequestHeaderProcessor.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		
		// 将收到的请求转化为map向后传递
		Map<String, Object> headers = exchange.getIn().getHeaders();
		String body = FormatUtil.stream2Str((InputStream) exchange.getIn().getBody());
		
		for (int i = 0; i < headers.size(); i++) {
			System.out.println("key: " + headers.keySet().toArray()[i] + 
					"value: " + headers.get(headers.keySet().toArray()[i]).toString());
		}

		// 开始验证报文头的有效性
		String channelId = headers.get("channelId") == null ? null : headers.get("channelId").toString();
		System.out.println("channel Id: " + channelId);
		String transactionCode = headers.get("transactionCode") == null ? null
				: headers.get("transactionCode").toString();
		String terminalId = headers.get("terminalId") == null ? null : headers.get("terminalId").toString();
		String traceNumber = headers.get("traceNumber") == null ? null : headers.get("traceNumber").toString();
		String transactionDate = headers.get("transactionDate") == null ? null
				: headers.get("transactionDate").toString();
		String transactionTime = headers.get("transactionTime") == null ? null
				: headers.get("transactionTime").toString();
		
		String errorMsg = null;
		
		if (channelId == null) {
			errorMsg = ReturnMsgUtil.generateErrorMessage(CHANNEL_ID_NULL_ERROR,
					getText(IL8N_RESOURCES_DEFAULT, CHANNEL_ID_NULL_ERROR));
			exchange.getOut().setBody(errorMsg);
			return;
		} if (transactionCode == null) {
			errorMsg = ReturnMsgUtil.generateErrorMessage(TRANSACTION_CODE_NULL_ERROR,
					getText(IL8N_RESOURCES_DEFAULT, TRANSACTION_CODE_NULL_ERROR));
			exchange.getOut().setBody(errorMsg);
			return;
		} if (terminalId == null) {
			errorMsg = ReturnMsgUtil.generateErrorMessage(TERMINAL_ID_NULL_ERROR,
					getText(IL8N_RESOURCES_DEFAULT, TERMINAL_ID_NULL_ERROR));
			exchange.getOut().setBody(errorMsg);
			return;
		} if (traceNumber == null) {
			errorMsg = ReturnMsgUtil.generateErrorMessage(TRACE_NUMBER_NULL_ERROR,
					getText(IL8N_RESOURCES_DEFAULT, TRACE_NUMBER_NULL_ERROR));
			exchange.getOut().setBody(errorMsg);
			return;
		} if (transactionDate == null) {
			errorMsg = ReturnMsgUtil.generateErrorMessage(TRANSACTION_DATE_NULL_ERROR,
					getText(IL8N_RESOURCES_DEFAULT, TRANSACTION_DATE_NULL_ERROR));
			exchange.getOut().setBody(errorMsg);
			return;
		} if (transactionTime == null) {
			errorMsg = ReturnMsgUtil.generateErrorMessage(TRANSACTION_TIME_NULL_ERROR,
					getText(IL8N_RESOURCES_DEFAULT, TRANSACTION_TIME_NULL_ERROR));
			exchange.getOut().setBody(errorMsg);
			return;
		}
		
		//TODO:验证terminalId有效性 连接数据库
		
		//校验完毕向后传递
		exchange.getIn().setBody(body);
	}
}
