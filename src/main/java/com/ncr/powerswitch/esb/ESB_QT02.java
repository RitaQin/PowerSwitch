package com.ncr.powerswitch.esb;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class ESB_QT02 {
	private final static Log log = LogFactory.getLog(ESB_QT02.class);
	private final static String EMPTY_STRING = "";

	public String[] verifyFields = null;

	public ESB_QT02() {

	}

	public String constructRequest(Map<String, Object> requestValues) {
		String templatePath = "/template/esb/";
		String templateFileName = "qt02.xml";
		String requestXml = "";

		Configuration config = new Configuration();
		try {
			// config.setDirectoryForTemplateLoading(new File(templatePath));
			// Template template=config.getTemplate(templateFileName);

			config.setClassForTemplateLoading(this.getClass(), "/");
			Template template = config.getTemplate(templatePath.concat(templateFileName), "UTF-8");

			Map<String, Object> context = new HashMap<String, Object>();

			// SYS_HEAD
			context.put("consumerId",
					requestValues.get("consumerId") == null ? EMPTY_STRING : requestValues.get("consumerId"));
			context.put("consumerSeqNo",
					requestValues.get("consumerSeqNo") == null ? EMPTY_STRING : requestValues.get("consumerSeqNo"));
			context.put("orgConsumerSeqNo", requestValues.get("orgConsumerSeqNo") == null ? EMPTY_STRING
					: requestValues.get("orgConsumerSeqNo"));
			context.put("servSeqNo",
					requestValues.get("servSeqNo") == null ? EMPTY_STRING : requestValues.get("servSeqNo"));
			context.put("tranDate",
					requestValues.get("tranDate") == null ? EMPTY_STRING : requestValues.get("tranDate"));
			context.put("tranTime",
					requestValues.get("tranTime") == null ? EMPTY_STRING : requestValues.get("tranTime"));
			context.put("fileFlag",
					requestValues.get("fileFlag") == null ? EMPTY_STRING : requestValues.get("fileFlag"));
			// APP_HEAD
			context.put("tranTellerNo",
					requestValues.get("tranTellerNo") == null ? EMPTY_STRING : requestValues.get("tranTellerNo"));
			// BODY
			context.put("cardNo", requestValues.get("cardNo") == null ? EMPTY_STRING : requestValues.get("cardNo"));
			context.put("branchId",
					requestValues.get("branchId") == null ? EMPTY_STRING : requestValues.get("branchId"));
			context.put("cnlInd", requestValues.get("cnlInd") == null ? EMPTY_STRING : requestValues.get("cnlInd"));
			context.put("subAcctNo",
					requestValues.get("subAcctNo") == null ? EMPTY_STRING : requestValues.get("subAcctNo"));
			context.put("identNo", requestValues.get("identNo") == null ? EMPTY_STRING : requestValues.get("identNo"));
			context.put("identTp", requestValues.get("identTp") == null ? EMPTY_STRING : requestValues.get("identTp"));

			requestXml = FreeMarkerTemplateUtils.processTemplateIntoString(template, context);
			log.info("QT02 Request :  " + requestXml);

			if (requestXml == null || "".equals(requestXml)) {
				log.error("填充模板出错。模板路径：" + templatePath + templateFileName);
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		// 返回报文字符串
		return requestXml;
	}

	public String[] getVerifyFields() {
		return verifyFields;
	}

	public void setVerifyFields(String[] verifyFields) {
		this.verifyFields = verifyFields;
	}

}
