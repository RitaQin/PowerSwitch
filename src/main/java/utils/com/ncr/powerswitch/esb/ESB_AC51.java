package com.ncr.powerswitch.esb;

import static com.ncr.powerswitch.utils.PowerSwitchConstant.SOCKET_SUCCESS_CODE;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.SOCKET_UNKNOWN_ERROR_CODE;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.ncr.powerswitch.utils.StringUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class ESB_AC51 extends ESBClient {
	
	private final static Log log = LogFactory.getLog(ESB_AC51.class);
	
	//需要验证的域
	public String[] verifyFields = null; 

	public ESB_AC51(String serverIp_, int serverPort_, boolean isOpenClient_, int maxPacket_) {
		super(serverIp_, serverPort_, isOpenClient_, maxPacket_);
	}
	
	@Override
	public String constructRequest(Map<String, String> requestValues) {
		String templatePath = "/template/esb/";        // 报文模板路径
		String templateFileName = "ESB_AC51.xml";  // 模板文件名
		String requestXml = "";
		
		Configuration config = new Configuration();
		try {
//			config.setDirectoryForTemplateLoading(new File(templatePath));		
//			Template template=config.getTemplate(templateFileName);
			
			config.setClassForTemplateLoading(getClass(), "/");
			Template template = config.getTemplate(templatePath.concat(templateFileName),"GBK");// 报文模板
 
			// 设置模板参数
			Map<String,Object> context = new HashMap<String,Object>();
			context.put("consumerId", requestValues.get("consumerId").equals(null) ? null : requestValues.get("consumerId"));
			context.put("consumerSeqNo", requestValues.get("consumerSeqNo").equals(null) ? null : requestValues.get("consumerSeqNo"));
			context.put("orgConsumerSeqNo", requestValues.get("orgConsumerSeqNo").equals(null) ? null : requestValues.get("orgConsumerSeqNo"));
			context.put("tranDate", requestValues.get("tranDate").equals(null) ? null : requestValues.get("tranDate"));
			context.put("tranTime", requestValues.get("tranTime").equals(null) ? null : requestValues.get("tranTime"));
			context.put("fileFlag", requestValues.get("fileFlag").equals(null) ? null : requestValues.get("fileFlag"));
			context.put("tranTellerNo", requestValues.get("tranTellerNo").equals(null) ? null : requestValues.get("tranTellerNo"));
			context.put("pcdFee", requestValues.get("pcdFee").equals(null) ? null : requestValues.get("pcdFee"));
			context.put("pwdChkFlg", requestValues.get("pwdChkFlg").equals(null) ? null : requestValues.get("pwdChkFlg"));
			context.put("branchId", requestValues.get("branchId").equals(null) ? null : requestValues.get("branchId"));
			context.put("dbAcctNo", requestValues.get("dbAcctNo").equals(null) ? null : requestValues.get("dbAcctNo"));
			context.put("cnlInd", requestValues.get("cnlInd").equals(null) ? null : requestValues.get("cnlInd"));
			context.put("pwd", requestValues.get("pwd").equals(null) ? null : requestValues.get("pwd"));
			context.put("ccy", requestValues.get("ccy").equals(null) ? null : requestValues.get("ccy"));
			context.put("cashPayFlg", requestValues.get("cashPayFlg").equals(null) ? null : requestValues.get("cashPayFlg"));
			context.put("txnAmt", requestValues.get("txnAmt").equals(null) ? null : requestValues.get("txnAmt"));
			context.put("cashTfrFlg", requestValues.get("cashTfrFlg").equals(null) ? null : requestValues.get("cashTfrFlg"));
			context.put("acgCntFlg", requestValues.get("acgCntFlg").equals(null) ? null : requestValues.get("acgCntFlg"));
			context.put("crAcctNo", requestValues.get("crAcctNo").equals(null) ? null : requestValues.get("crAcctNo"));
			context.put("note", requestValues.get("note").equals(null) ? null : requestValues.get("note"));
			context.put("pyrNm", requestValues.get("pyrNm").equals(null) ? null : requestValues.get("pyrNm"));
			context.put("pypyeAcctNmrNm", requestValues.get("pyeAcctNm").equals(null) ? null : requestValues.get("pyeAcctNm"));
			context.put("smyDsc", requestValues.get("smyDsc").equals(null) ? null : requestValues.get("smyDsc"));
			context.put("crSubAcctOrdrNo", requestValues.get("crSubAcctOrdrNo").equals(null) ? null : requestValues.get("crSubAcctOrdrNo"));
			context.put("txnSrcTp", requestValues.get("txnSrcTp").equals(null) ? null : requestValues.get("txnSrcTp"));
			context.put("retSnglInf", requestValues.get("retSnglInf").equals(null) ? null : requestValues.get("retSnglInf"));
			context.put("wrkSrtKey", requestValues.get("wrkSrtKey").equals(null) ? null : requestValues.get("wrkSrtKey"));
			context.put("feeRateTp", requestValues.get("feeRateTp").equals(null) ? null : requestValues.get("feeRateTp"));
			context.put("dbSubAcctOrdrNo", requestValues.get("dbSubAcctOrdrNo").equals(null) ? null : requestValues.get("dbSubAcctOrdrNo"));
			context.put("agrmNo", requestValues.get("agrmNo").equals(null) ? null : requestValues.get("agrmNo"));
			context.put("vchrNo", requestValues.get("vchrNo").equals(null) ? null : requestValues.get("vchrNo"));
			context.put("pcdFeeCasTfrFlg", requestValues.get("pcdFeeCasTfrFlg").equals(null) ? null : requestValues.get("pcdFeeCasTfrFlg"));
			context.put("oppAcctNo", requestValues.get("oppAcctNo").equals(null) ? null : requestValues.get("oppAcctNo"));
			context.put("vchrTp", requestValues.get("vchrTp").equals(null) ? null : requestValues.get("vchrTp"));
			context.put("issuDt", requestValues.get("issuDt").equals(null) ? null : requestValues.get("issuDt"));
			context.put("oppBnkNo", requestValues.get("oppBnkNo").equals(null) ? null : requestValues.get("oppBnkNo"));
			context.put("hngAcgSbjNo", requestValues.get("hngAcgSbjNo").equals(null) ? null : requestValues.get("hngAcgSbjNo"));
			context.put("oppBnkNm", requestValues.get("oppBnkNm").equals(null) ? null : requestValues.get("oppBnkNm"));
			context.put("apprTellerNo", requestValues.get("apprTellerNo").equals(null) ? null : requestValues.get("apprTellerNo"));
			context.put("agrmTpDsc", requestValues.get("agrmTpDsc").equals(null) ? null : requestValues.get("agrmTpDsc"));
			context.put("smyCd", requestValues.get("smyCd").equals(null) ? null : requestValues.get("smyCd"));
			context.put("pplBnkWrkDt", requestValues.get("pplBnkWrkDt").equals(null) ? null : requestValues.get("pplBnkWrkDt"));
			context.put("pcdFeeSmyCd", requestValues.get("pcdFeeSmyCd").equals(null) ? null : requestValues.get("pcdFeeSmyCd"));
			context.put("flowBnkCntlFlg", requestValues.get("flowBnkCntlFlg").equals(null) ? null : requestValues.get("flowBnkCntlFlg"));
			context.put("flowBnkTskNo", requestValues.get("flowBnkTskNo").equals(null) ? null : requestValues.get("flowBnkTskNo"));
			context.put("formrSeqNo", requestValues.get("formrSeqNo").equals(null) ? null : requestValues.get("formrSeqNo"));
			context.put("formrTranDate", requestValues.get("formrTranDate").equals(null) ? null : requestValues.get("formrTranDate"));
			context.put("mrchId", requestValues.get("mrchId").equals(null) ? null : requestValues.get("mrchId"));
			context.put("hngAcctBrId", requestValues.get("hngAcctBrId").equals(null) ? null : requestValues.get("hngAcctBrId"));
			
			requestXml = FreeMarkerTemplateUtils.processTemplateIntoString(template,context);
			log.info("AC51 Request :  " + requestXml);
			System.out.println(this.getClass()+ " AC51 Request: \n" + requestXml);
			
			if (requestXml == null || "".equals(requestXml)) {
				log.error("填充模板出错，模板路径：" + templatePath
						+ templateFileName);
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

	@Override
	public String sendAndReceivePackets(String msg, String ip, String port, boolean unpack) {
		
		System.out.println("starting send and receive packet....");
		if (!isOpenClient) {
			System.out.println("not open client");
			return null;
		}
		if (socket == null) {
			String result = newInstance(ip, Integer.parseInt(port), timeout);
			if (result != null && !result.equals(SOCKET_SUCCESS_CODE)) {
				System.out.println("Error occurred: " + result);
				return result;
			}
		}
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			outputStream = socket.getOutputStream();
			byte[] bytelst = StringUtil.ASCII_To_BCD(msg.getBytes(), msg.length());
			outputStream.write(bytelst);
			outputStream.flush();

			inputStream = socket.getInputStream();
			byte[] bytes = new byte[maxPacket];
			int n = inputStream.read(bytes);
			if (!unpack) {
				return StringUtil.bcd2Str(bytes, n);
			} else {
				String retmsg = StringUtil.bcd2Str(bytes, n);
				if (retmsg != null) {
					byte[] bs = StringUtil.ASCII_To_BCD(retmsg.getBytes(), retmsg.length());
					if (bs != null) {
						String retmsg_unpack = "";
						for (int i = 0; i < bs.length; i++) {
							retmsg_unpack += StringUtil.asc_to_bcd(bs[i]);
						}
					}
				}
			}
		} catch (IOException e) {
			log.error("socket client receive packet error, message is" + e.getMessage() + ".");
			System.out.println("socket client receive packet error, message is" + e.getMessage() + ".");
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					log.error(
							"socket client receive packet,InputStream close error, message is" + e.getMessage() + ".");
					System.out.println("socket client receive packet,InputStream close error, message is" + e.getMessage() + ".");
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					log.error(
							"socket client receive packet,outputStream close error, message is" + e.getMessage() + ".");
					System.out.println("socket client receive packet,outputStream close error, message is" + e.getMessage() + ".");
				}
			}
			if (socket != null) {
				try {
					socket.close();
					socket = null;
					// if(socket == null){
					// newInstance(serverIp,serverPort,timeout);
					// }
				} catch (IOException e) {
					log.error("socket client receive packet,socket close error, message is" + e.getMessage() + ".");
					System.out.println("socket client receive packet,outputStream close error, message is" + e.getMessage() + ".");
				}
			}
		}
		return SOCKET_UNKNOWN_ERROR_CODE;
	}
	
	public String[] getVerifyFields() {
		return verifyFields;
	}

	public void setVerifyFields(String[] verifyFields) {
		this.verifyFields = verifyFields;
	}

}
