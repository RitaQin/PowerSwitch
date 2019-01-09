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

public class ESB_AC51 implements ESBClientIntf {

	public ESB_AC51() {
		super();
	}

	private final static Log log = LogFactory.getLog(ESB_AC51.class);
	private final static String EMPTY_STRING = ""; 

	// 鏍￠獙鍩�
	public String[] verifyFields = null;

	@Override
	public String constructRequest(Map<String, Object> requestValues) {
		String templatePath = "/template/esb/"; // template folder
		String templateFileName = "ac51.xml"; // AC51 template
		String requestXml = "";

		Configuration config = new Configuration();
		try {
			// config.setDirectoryForTemplateLoading(new File(templatePath));
			// Template template=config.getTemplate(templateFileName);

			config.setClassForTemplateLoading(getClass(), "/");
			Template template = config.getTemplate(templatePath.concat(templateFileName), "UTF-8");

			Map<String, Object> context = new HashMap<String, Object>();
			// SYS_HEAD
			context.put("consumerId", requestValues.get("consumerId") == null ? EMPTY_STRING : requestValues.get("consumerId"));
			context.put("consumerSeqNo",
					requestValues.get("consumerSeqNo") == null ? EMPTY_STRING : requestValues.get("consumerSeqNo"));
			context.put("orgConsumerSeqNo",
					requestValues.get("orgConsumerSeqNo") == null ? EMPTY_STRING : requestValues.get("orgConsumerSeqNo"));
			context.put("tranDate",
					requestValues.get("transactionDate") == null ? EMPTY_STRING : requestValues.get("transactionDate"));
			context.put("tranTime",
					requestValues.get("transactionTime") == null ? EMPTY_STRING : requestValues.get("transactionTime"));
			context.put("fileFlag", requestValues.get("fileFlag") == null ? EMPTY_STRING : requestValues.get("fileFlag"));
			// APP_HEAD
			context.put("tranTellerNo",
					requestValues.get("tranTellerNo") == null ? EMPTY_STRING : requestValues.get("tranTellerNo"));
			// BODY
			context.put("pcdFee", requestValues.get("pcdFee") == null ? EMPTY_STRING : requestValues.get("pcdFee"));
			context.put("pwdChkFlg", requestValues.get("pwdChkFlg") == null ? EMPTY_STRING : requestValues.get("pwdChkFlg"));
			context.put("branchId", requestValues.get("branchId") == null ? EMPTY_STRING : requestValues.get("branchId"));
			context.put("dbAcctNo", requestValues.get("dbAcctNo") == null ? EMPTY_STRING : requestValues.get("dbAcctNo"));
			context.put("cnlInd", requestValues.get("cnlInd") == null ? EMPTY_STRING : requestValues.get("cnlInd"));
			context.put("pwd", requestValues.get("pwd") == null ? EMPTY_STRING : requestValues.get("pwd"));
			context.put("ccy", requestValues.get("ccy") == null ? EMPTY_STRING : requestValues.get("ccy"));
			context.put("cashPayFlg", requestValues.get("cashPayFlg") == null ? EMPTY_STRING : requestValues.get("cashPayFlg"));
			context.put("txnAmt", requestValues.get("txnAmt") == null ? EMPTY_STRING : requestValues.get("txnAmt"));
			context.put("cashTfrFlg", requestValues.get("cashTfrFlg") == null ? EMPTY_STRING : requestValues.get("cashTfrFlg"));
			context.put("acgCntFlg", requestValues.get("acgCntFlg") == null ? EMPTY_STRING : requestValues.get("acgCntFlg"));
			context.put("crAcctNo", requestValues.get("crAcctNo") == null ? EMPTY_STRING : requestValues.get("crAcctNo"));
			context.put("note", requestValues.get("note") == null ? EMPTY_STRING : requestValues.get("note"));
			context.put("pyrNm", requestValues.get("pyrNm") == null ? EMPTY_STRING : requestValues.get("pyrNm"));
			context.put("pyeAcctNm",
					requestValues.get("pyeAcctNm") == null ? EMPTY_STRING : requestValues.get("pyeAcctNm"));
			context.put("smyDsc", requestValues.get("smyDsc") == null ? EMPTY_STRING : requestValues.get("smyDsc"));
			context.put("crSubAcctOrdrNo",
					requestValues.get("crSubAcctOrdrNo") == null ? EMPTY_STRING : requestValues.get("crSubAcctOrdrNo"));
			context.put("txnSrcTp", requestValues.get("txnSrcTp") == null ? EMPTY_STRING : requestValues.get("txnSrcTp"));
			context.put("retSnglInf", requestValues.get("retSnglInf") == null ? EMPTY_STRING : requestValues.get("retSnglInf"));
			context.put("wrkSrtKey", requestValues.get("wrkSrtKey") == null ? EMPTY_STRING : requestValues.get("wrkSrtKey"));
			context.put("feeRateTp", requestValues.get("feeRateTp") == null ? EMPTY_STRING : requestValues.get("feeRateTp"));
			context.put("dbSubAcctOrdrNo",
					requestValues.get("dbSubAcctOrdrNo") == null ? EMPTY_STRING : requestValues.get("dbSubAcctOrdrNo"));
			context.put("agrmNo", requestValues.get("agrmNo") == null ? EMPTY_STRING : requestValues.get("agrmNo"));
			context.put("vchrNo", requestValues.get("vchrNo") == null ? EMPTY_STRING : requestValues.get("vchrNo"));
			context.put("pcdFeeCasTfrFlg",
					requestValues.get("pcdFeeCasTfrFlg") == null ? EMPTY_STRING : requestValues.get("pcdFeeCasTfrFlg"));
			context.put("oppAcctNo", requestValues.get("oppAcctNo") == null ? EMPTY_STRING : requestValues.get("oppAcctNo"));
			context.put("vchrTp", requestValues.get("vchrTp") == null ? EMPTY_STRING : requestValues.get("vchrTp"));
			context.put("issuDt", requestValues.get("issuDt") == null ? EMPTY_STRING : requestValues.get("issuDt"));
			context.put("oppBnkNo", requestValues.get("oppBnkNo") == null ? EMPTY_STRING : requestValues.get("oppBnkNo"));
			context.put("hngAcgSbjNo",
					requestValues.get("hngAcgSbjNo") == null ? EMPTY_STRING : requestValues.get("hngAcgSbjNo"));
			context.put("oppBnkNm", requestValues.get("oppBnkNm") == null ? EMPTY_STRING : requestValues.get("oppBnkNm"));
			context.put("apprTellerNo",
					requestValues.get("apprTellerNo") == null ? EMPTY_STRING : requestValues.get("apprTellerNo"));
			context.put("agrmTpDsc", requestValues.get("agrmTpDsc") == null ? EMPTY_STRING : requestValues.get("agrmTpDsc"));
			context.put("smyCd", requestValues.get("smyCd") == null ? EMPTY_STRING : requestValues.get("smyCd"));
			context.put("pplBnkWrkDt",
					requestValues.get("pplBnkWrkDt") == null ? EMPTY_STRING : requestValues.get("pplBnkWrkDt"));
			context.put("pcdFeeSmyCd",
					requestValues.get("pcdFeeSmyCd") == null ? EMPTY_STRING : requestValues.get("pcdFeeSmyCd"));
			context.put("flowBnkCntlFlg",
					requestValues.get("flowBnkCntlFlg") == null ? EMPTY_STRING : requestValues.get("flowBnkCntlFlg"));
			context.put("flowBnkTskNo",
					requestValues.get("flowBnkTskNo") == null ? EMPTY_STRING : requestValues.get("flowBnkTskNo"));
			context.put("formrSeqNo", requestValues.get("formrSeqNo") == null ? EMPTY_STRING : requestValues.get("formrSeqNo"));
			context.put("formrTranDate",
					requestValues.get("formrTranDate") == null ? EMPTY_STRING : requestValues.get("formrTranDate"));
			context.put("mrchId", requestValues.get("mrchId") == null ? EMPTY_STRING : requestValues.get("mrchId"));
			context.put("hngAcctBrId",
					requestValues.get("hngAcctBrId") == null ? EMPTY_STRING : requestValues.get("hngAcctBrId"));

			requestXml = FreeMarkerTemplateUtils.processTemplateIntoString(template, context);
			log.info("AC51 Request :  " + requestXml);

			if (requestXml == null || "".equals(requestXml)) {
				log.error("填充模板出错。模板路径" + templatePath + templateFileName);
				return null;
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		
		return requestXml;
	}

	public String[] getVerifyFields() {
		return verifyFields;
	}

	public void setVerifyFields(String[] verifyFields) {
		this.verifyFields = verifyFields;
	}

	@Override
	public boolean verifyResMessage(String msg, String receivedMsg, String[] verifyFields) {
		// TODO Auto-generated method stub
		return false;
	}

}
