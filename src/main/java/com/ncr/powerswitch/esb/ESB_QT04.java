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

public class ESB_QT04 {

	private final static Log log = LogFactory.getLog(ESB_QT04.class);

	public String[] verifyFields = null;

	public ESB_QT04() {

	}

	public String constructRequest(Map<String, Object> requestValues) {
		String templatePath = "/template/esb/";
		String templateFileName = "QT04SBS.xml";
		String requestXml = "";

		Configuration config = new Configuration();
		try {
			// config.setDirectoryForTemplateLoading(new File(templatePath));
			// Template template=config.getTemplate(templateFileName);

			config.setClassForTemplateLoading(this.getClass(), "/");
			Template template = config.getTemplate(templatePath.concat(templateFileName), "UTF-8");// ����ģ��

			Map<String, Object> context = new HashMap<String, Object>();

			// String test = requestValues.get("channelId").toString();

			// Head
			context.put("consumerSeqNo",
					requestValues.get("consumerSeqNo") == null ? "" : requestValues.get("consumerSeqNo"));
			context.put("tranDate", requestValues.get("tranDate") == null ? "" : requestValues.get("tranDate"));

			// APP Head
			context.put("tranTellerNo",
					requestValues.get("tranTellerNo") == null ? "" : requestValues.get("tranTellerNo"));

			// Body
			context.put("branchId", requestValues.get("branchId") == null ? "" : requestValues.get("branchId"));

			context.put("cnlInd", requestValues.get("cnlInd") == null ? "" : requestValues.get("cnlInd"));
			context.put("formrTranDate",
					requestValues.get("formrTranDate") == null ? "" : requestValues.get("formrTranDate"));
			context.put("formrSeqNo", requestValues.get("formrSeqNo") == null ? "" : requestValues.get("formrSeqNo"));

			context.put("agntIdentTp",
					requestValues.get("agntIdentTp") == null ? "" : requestValues.get("agntIdentTp"));
			context.put("agntIdentNo",
					requestValues.get("agntIdentNo") == null ? "" : requestValues.get("agntIdentNo"));
			context.put("agntNm", requestValues.get("agntNm") == null ? "" : requestValues.get("agntNm"));
			context.put("acctNo", requestValues.get("acctNo") == null ? "" : requestValues.get("acctNo"));

			context.put("subAcctNo", requestValues.get("subAcctNo") == null ? "" : requestValues.get("subAcctNo"));
			context.put("txnAmt", requestValues.get("txnAmt") == null ? "" : requestValues.get("txnAmt"));
			context.put("identTp", requestValues.get("identTp") == null ? "" : requestValues.get("identTp"));
			context.put("identNo", requestValues.get("identNo") == null ? "" : requestValues.get("identNo"));

			context.put("acctNm", requestValues.get("acctNm") == null ? "" : requestValues.get("acctNm"));
			context.put("formrTxnCd", requestValues.get("formrTxnCd") == null ? "" : requestValues.get("formrTxnCd"));
			context.put("ocrInstId", requestValues.get("ocrInstId") == null ? "" : requestValues.get("ocrInstId"));
			context.put("formrTxnTlrNo",
					requestValues.get("formrTxnTlrNo") == null ? "" : requestValues.get("formrTxnTlrNo"));

			context.put("formrAhrTlrNo",
					requestValues.get("formrAhrTlrNo") == null ? "" : requestValues.get("formrAhrTlrNo"));
			context.put("rltnpTp", requestValues.get("rltnpTp") == null ? "" : requestValues.get("rltnpTp"));
			context.put("txnMode", requestValues.get("txnMode") == null ? "" : requestValues.get("txnMode"));

			requestXml = FreeMarkerTemplateUtils.processTemplateIntoString(template, context);
			log.info("QT04-SBS Request :  " + requestXml);
			System.out.println(this.getClass() + " QT04-SBS: \n" + requestXml);

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
