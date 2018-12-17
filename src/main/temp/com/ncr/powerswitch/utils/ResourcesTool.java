package com.ncr.powerswitch.utils;

import java.util.Locale;
import java.util.ResourceBundle;

/***
 * 资源文件操作
 * @author rq185015
 *
 */

public class ResourcesTool {
	
	private final static String IL8N_RESOURCES_PREFIX                             = "errorMessageResource";
	public  final static String IL8N_RESOURCES_DEFAULT                            = "global";
	public  final static String IL8N_REMOTEKEYLOAD_DEFAULT                        = "RemoteKeyLoad";
	public  final static String IL8N_UPLOADEJLOGON_DEFAULT                        = "UploadEJLogon";
	public  final static String IL8N_UPLOADEJ_DEFAULT                             = "UploadEJ";
	public  final static String IL8N_QUERYPARAMETERSVERSION_DEFAULT               = "QueryParametersVersion";
	public  final static String IL8N_DOWNLOADPARAMETERS_DEFAULT                   = "DownloadParameters";
	public  final static String IL8N_QUERYNOTESINFO_DEFAULT                       = "QueryNotesInfo";
	public  final static String IL8N_UPLOADNOTESINFO_DEFAULT                      = "UploadNotesInfo";
	
	/**
	 * 获取资源文件值
	 * 
	 * @param il8nName
	 * @param key
	 * @param local
	 * @return
	 */
	private static String getTextTool(String il8nName, String key, Locale local) {
		ResourceBundle rb = null;
		try {
			rb = ResourceBundle.getBundle(IL8N_RESOURCES_PREFIX+ "_"+ ((il8nName == null || il8nName.equals("")) ? IL8N_RESOURCES_DEFAULT: il8nName),local == null ? Locale.CHINA : local);
			return rb.getString(key.toLowerCase());
		} catch (Exception e) {
			try {
				rb = ResourceBundle.getBundle(IL8N_RESOURCES_PREFIX+ "_" + IL8N_RESOURCES_DEFAULT,local == null ? Locale.CHINA : local);
				return rb.getString(key.toLowerCase());
			} catch (Exception e2) {
				return key.toLowerCase();
			}

		}

	}

	/**
	 * 获取资源文件值
	 * 
	 * @param il8nName
	 * @param key
	 * @param local
	 * @return
	 */
	public static String getText(String il8nName, String key, Locale local) {
		return getTextTool(il8nName, key, local);
	}

	/**
	 * 获取资源文件值
	 * 
	 * @param il8nName
	 * @param key
	 * @return
	 */
	public static String getText(String il8nName, String key) {
		return getTextTool(il8nName, key, null);
	}

	/**
	 * 获取资源文件值,带参数
	 * 
	 * @param il8nName
	 * @param key
	 * @param local
	 * @param values
	 * @return
	 */
	public static String getText(String il8nName, String key, Locale local,Object[] values) {

		String str = getTextTool(il8nName, key, local);
		if (str != null) {
			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					str = StringUtil.replace(str, "{" + i + "}", values[i].toString());
				}
			}
		}
		return str;
	}

	/**
	 * 获取资源文件值,带参数
	 * 
	 * @param il8nName
	 * @param key
	 * @param values
	 * @return
	 */
	public static String getText(String il8nName, String key, Object[] values) {
		String str = getTextTool(il8nName, key, null);
		if (str != null) {
			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					str = StringUtil.replace(str, "{" + i + "}", values[i].toString());
				}
			}
		}
		return str;
	}

}