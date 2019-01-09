package com.ncr.powerswitch.esb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.ncr.powerswitch.exception.PowerswitchException;
import com.ncr.powerswitch.utils.LogUtil;
import com.ncr.powerswitch.utils.PowerSwitchConstant;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import com.ncr.powerswitch.exception.PowerswitchException;
import com.ncr.powerswitch.utils.PowerSwitchConstant;

public class ESB_XZ01 implements ESBClientIntf{

	private final static Log log = LogFactory.getLog(ESB_XZ01.class);
	private String errMsg = null;
	
	private final static String EMPTY_STRING = ""; 
	public String[] verifyFields = null; 

	
	@Override
	public String constructRequest(Map<String, Object> requestValues) {
		String templatePath = "/template/esb/";        
		String templateFileName = "xz01.xml"; 
		String requestXml = "";
		
		Configuration config = new Configuration();
		try {
//			config.setDirectoryForTemplateLoading(new File(templatePath));		
//			Template template=config.getTemplate(templateFileName);
			
			config.setClassForTemplateLoading(getClass(), "/");
			Template template = config.getTemplate(templatePath.concat(templateFileName),"UTF-8");// ����ģ��
			
			Map<String,Object> context = new HashMap<String,Object>();
			context.put("consumerId", requestValues.get("consumerId") == null ? EMPTY_STRING : requestValues.get("consumerId"));
			context.put("consumerSeqNo", requestValues.get("consumerSeqNo") == null ? EMPTY_STRING : requestValues.get("consumerSeqNo"));
			context.put("tranDate", requestValues.get("tranDate") == null ? EMPTY_STRING : requestValues.get("tranDate"));
			context.put("tranTime", requestValues.get("tranTime") == null ? EMPTY_STRING : requestValues.get("tranTime"));
			context.put("tranTellerNo", requestValues.get("tranTellerNo") == null ? EMPTY_STRING : requestValues.get("tranTellerNo"));
			context.put("oprlTp", requestValues.get("oprlTp") == null ? EMPTY_STRING : requestValues.get("oprlTp"));
			context.put("lmtLrgKeyVal", requestValues.get("lmtLrgKeyVal") == null ? EMPTY_STRING : requestValues.get("lmtLrgKeyVal"));
			context.put("branchId", requestValues.get("branchId") == null ? EMPTY_STRING : requestValues.get("branchId"));
			context.put("lmtSmlKeyVal", requestValues.get("lmtSmlKeyVal") == null ? EMPTY_STRING : requestValues.get("lmtSmlKeyVal"));
			context.put("cnlInd", requestValues.get("cnlInd") == null ? EMPTY_STRING : requestValues.get("cnlInd"));
			context.put("pdCd", requestValues.get("pdCd") == null ? EMPTY_STRING : requestValues.get("pdCd"));
			
			requestXml = FreeMarkerTemplateUtils.processTemplateIntoString(template,context);
			log.info("XZ01 Request :  " + requestXml);
			//System.out.println(this.getClass()+ " XZ01 Request: \n" + requestXml);
			
			if (requestXml == null || "".equals(requestXml)) {
				errMsg = LogUtil.getClassMethodName() + ":" + "xml content of request is null(empty)";
				log.error(errMsg);
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
	public boolean verifyResMessage(String msg, String receivedMsg,
			String[] verifyFields) {
		// TODO Auto-generated method stub
		return false;
	}

}
