package com.ncr.powerswitch.DAO;

import java.util.List;
import java.util.Map;

import com.ncr.powerswitch.dataObject.EppKey;
import com.ncr.powerswitch.dataObject.Terminal;
import com.ncr.powerswitch.dataObject.TerminalKey;
import com.ncr.powerswitch.persistIntf.SqlMapperIntf;

public interface HsmDao {
	
	/**
	 * 验证终端号是否有效
	 * 
	 * @param terminalID   终端号
	 * @param ip   IP地址
	 * @return  1：有效数据，2：非法端机号  3：端机号与IP地址不匹配
	 */
	int terminalIDIsValid(String ip, String terminalId);
	
	/**
	 * 查询ATM厂商名
	 * @param ip IP地址
	 * @param terminalID 端机号
	 * @return
	 */
	Map<String,Object> findManufacturerByIPAndTerminalID(String ip,String terminalId);
	
	/**
	 * 通过加密机返回的数据密钥，密钥校验码，更新terminalKeyTable
	 * @param terminalKey 需要更新的terminalKey
	 * @return
	 */
	boolean updateTerminalKey(TerminalKey terminalKey);
	
	/**
	 * 通过加密机返回的数据密钥，密钥校验码，更新terminalKeyTable
	 * @param terminalKey 需要添加的terminalKey
	 * @return
	 */
	boolean insertMasterKey(TerminalKey terminalKey);
	
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
	int updateTerminalEPPID(String strTerminalID,String eppId);
	
	/**
	 * 根据终端号查找对应EppKey对象
	 * @param strTerminalID 端机号
	 * @param eppId EPPID
	 * @return
	 */
	EppKey getEppInfoByTerminalId(String terminalId); 
	
	
	/**
	 * 根据终端号查找Master Key
	 * @param terminalId
	 * @return
	 */
	String getMasterKeyByTerminalId(String terminalId); 

}
