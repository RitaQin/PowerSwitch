package com.ncr.powerswitch.esb;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.ncr.powerswitch.utils.LogUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class ESB_QT01 {
	private final static Log log = LogFactory.getLog(ESB_QT01.class);
	private String errMsg = null;
	private final static String EMPTY_STRING = "";

	public String[] verifyFields = null;

	public ESB_QT01() {

	}

	public String constructRequest(Map<String, Object> requestValues) {
		String templatePath = "/template/esb/";
		String templateFileName = "qt01.xml";
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
			
			context.put("branchId",	requestValues.get("branchId") == null ? EMPTY_STRING : requestValues.get("branchId"));
			context.put("vrfyTp", requestValues.get("vrfyTp") == null ? EMPTY_STRING : requestValues.get("vrfyTp"));
			context.put("identNo", requestValues.get("identNo") == null ? EMPTY_STRING : requestValues.get("identNo"));
			context.put("identTp", requestValues.get("identTp") == null ? EMPTY_STRING : requestValues.get("identTp"));
			context.put("pwd", requestValues.get("pwd") == null ? EMPTY_STRING : requestValues.get("pwd"));
			context.put("cardNo", requestValues.get("cardNo") == null ? EMPTY_STRING : requestValues.get("cardNo"));
			context.put("scdTrackMsg", requestValues.get("scdTrackMsg") == null ? EMPTY_STRING : requestValues.get("scdTrackMsg"));
			context.put("cardRdMode", requestValues.get("cardRdMode") == null ? EMPTY_STRING : requestValues.get("cardRdMode"));
			context.put("wrkSrtKey", requestValues.get("wrkSrtKey") == null ? EMPTY_STRING : requestValues.get("wrkSrtKey"));
			context.put("trackSrtKey", requestValues.get("trackSrtKey") == null ? EMPTY_STRING : requestValues.get("trackSrtKey"));
			context.put("threeTrackMsg", requestValues.get("threeTrackMsg") == null ? EMPTY_STRING : requestValues.get("threeTrackMsg"));
			context.put("icAuthtnInf", requestValues.get("icAuthtnInf") == null ? EMPTY_STRING : requestValues.get("icAuthtnInf"));
			context.put("cardSerlNo", requestValues.get("cardSerlNo") == null ? EMPTY_STRING : requestValues.get("cardSerlNo"));
			context.put("icVerfFlg", requestValues.get("icVerfFlg") == null ? EMPTY_STRING : requestValues.get("icVerfFlg"));			

			requestXml = FreeMarkerTemplateUtils.processTemplateIntoString(template, context);
			log.info("QT01 Request :  " + requestXml);

			if (requestXml == null || "".equals(requestXml)) {
				errMsg = LogUtil.getClassMethodName() + ":" + "xml content of request is null(empty)";
				log.error(errMsg);
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
