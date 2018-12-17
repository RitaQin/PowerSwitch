package com.ncr.powerswitch.utils;

import java.util.HashMap;
import java.util.Map;
import static com.ncr.powerswitch.utils.PowerSwitchConstant.GLOBAL_SUCCESS_CODE;
import static com.ncr.powerswitch.utils.ResourcesTool.IL8N_RESOURCES_DEFAULT;

/***
 * 生成web service响应相关辅助
 * @author rq185015
 *
 */

public class ReturnMsgUtil {
	
	/**
	 * 返回Web Services错误信息
	 * @param errorID   错误代码
	 * @param errorMsg  错误信息 
	 * @return
	 */
	public static String generateErrorMessage(String errorID, String errorMsg) {
		Map<String, Object> errorMap = new HashMap<String, Object>(); 
		//将错误代码和错误信息放入k-v map 
		errorMap.put("ErrorID", errorID);  
		errorMap.put("ErrorMsg", errorMsg); 
		//转化为JSON并返回
		String errorJson = FormatUtil.map2Json(errorMap); 
		return errorJson;
	}
	
	/**
	 * 返回Web Services 成功信息
	 * @return
	 */
	public static String generateSuccessMessage() {
		Map<String, Object> successMap = new HashMap<String, Object>();
		//将成功相应信息放入k-v map 
		successMap.put("ErrorID", GLOBAL_SUCCESS_CODE); 
		successMap.put("ErrorMsg", ResourcesTool.getText(IL8N_RESOURCES_DEFAULT, GLOBAL_SUCCESS_CODE));
		//转化为JSON并返回
		String successJson = FormatUtil.map2Json(successMap);
		return successJson;
	}
}
