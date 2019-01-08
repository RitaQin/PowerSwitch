package com.ncr.powerswitch.DAO;

import java.util.List;
import java.util.Map;

/***
 * 加密机相关数据库访问接口
 * @author rq185015
 *
 */
public interface RemoteDAO {
	
	/**
	 * 验证终端号是否有效
	 * 
	 * @param strTerminalID   终端号
	 * @param IP   IP地址
	 * @return  1：有效数据，2：非法端机号  3：端机号与IP地址不匹配
	 */
	int terminalIDIsValid(String IP,String strTerminalID);
	
	/**
	 * 查询ATM厂商名
	 * @param IP IP地址
	 * @param strTerminalID 端机号
	 * @return
	 */
	Map<String,Object> findManufacturerByIPAndTerminalID(String IP,String strTerminalID);
	
	/**
	 * 获取加密机配置参数（IP地址，端口号）
	 * @return 加密机配置参数
	 */
	Map findHSMSETAll();
	
	/**
	 * 验证终端号和批次是否有效
	 * 
	 * @param strTerminalID  终端号
	 * @param strBatch       批 次 
	 * @return  true:有效  false:无效
	 */
	boolean batchIsValid(String strTerminalID,String strBatch);
	
	/**
	 * 初始化新增一台终端机签到记录
	 * @param strTerminalID 终端号
	 * @param ip IP地址
	 * @param strAreaCode 地区号
	 * @return 1：成功 0：失败
	 */
	int addEJLogon(String strTerminalID,String ip,String strAreaCode);
	
	/**
	 * 更新终端机最后上传时间和行号
	 * @param strTerminalID 终端号
	 * @param ip IP地址
	 * @param strDate 最后上传时间
	 * @param lastLine 最后上传行号
	 * @return 1：成功 0：失败
	 */
	int updateEJLogon(String strTerminalID,String ip,String strDate,String lastLine);
	
	/**
	 * 参数版本号查询
	 * @param strTerminalID 终端号
	 * @param ip IP地址
	 * @param strArea 地区号
	 * @return
	 */
	List<Object> findParametersVersionByStrTerminalIDAndStrArea(String strTerminalID,String ip,String strArea);
	
	/**
	 * 参数版本号查询(通用参数)
	 * @param strTerminalID 终端号
	 * @param strArea 地区号
	 * @return 
	 */
	List<Object> findParametersVersionByTerminalIDANDareaCode(String strTerminalID,String areaCode);
	
	/**
	 * 参数版本下载
	 * @param strTerminalID 终端号
	 * @param ip IP地址
	 * @param strArea 地区号
	 * @param parameType 参数版本类型
	 * @return 参数版本详细信息
	 */
	Map<String,Object> findParameTextByStrTerminalIDAndStrAreaAndParameType(String strTerminalID,String ip,String strArea,String parameType);
	
	/**
	 * 通过加密机返回的数据密钥，密钥校验码，更新到ATM
	 * @param strTerminalID 端机号
	 * @param ip  IP地址
	 * @param keyText 数据密钥
	 * @param keyCheck 数据密钥校验码
	 * @return
	 */
	boolean updateATMKEY(String strTerminalID,String ip,String keyText,String keyCheck);
	
	/**
	 * 按端机号IP查询ATM数据
	 * @param strTerminalID 端机号
	 * @param ip IP地址
	 * @return
	 */
	Map<String,Object> findSysTermByTermIdAndIP(String strTerminalID,String ip);
	
	/**
	 * 更新ATM EPPID
	 * @param strTerminalID 端机号
	 * @param eppId EPPID
	 * @return
	 */
	int updateATMEPPID(String strTerminalID,String eppId);
}
