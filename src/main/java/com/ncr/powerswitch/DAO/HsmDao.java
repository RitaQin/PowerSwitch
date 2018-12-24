package com.ncr.powerswitch.DAO;

import java.util.List;
import java.util.Map;

import com.ncr.powerswitch.dataObject.EppKey;
import com.ncr.powerswitch.dataObject.Terminal;
import com.ncr.powerswitch.dataObject.TerminalKey;
import com.ncr.powerswitch.persistIntf.SqlMapperIntf;

public interface HsmDao {
	
	/**
	 * ��֤�ն˺��Ƿ���Ч
	 * 
	 * @param terminalID   �ն˺�
	 * @param ip   IP��ַ
	 * @return  1����Ч���ݣ�2���Ƿ��˻���  3���˻�����IP��ַ��ƥ��
	 */
	int terminalIDIsValid(String ip, String terminalId);
	
	/**
	 * ��ѯATM������
	 * @param ip IP��ַ
	 * @param terminalID �˻���
	 * @return
	 */
	Map<String,Object> findManufacturerByIPAndTerminalID(String ip,String terminalId);
	
	/**
	 * ͨ�����ܻ����ص�������Կ����ԿУ���룬����terminalKeyTable
	 * @param terminalKey ��Ҫ���µ�terminalKey
	 * @return
	 */
	boolean updateTerminalKey(TerminalKey terminalKey);
	
	/**
	 * ͨ�����ܻ����ص�������Կ����ԿУ���룬����terminalKeyTable
	 * @param terminalKey ��Ҫ��ӵ�terminalKey
	 * @return
	 */
	boolean insertMasterKey(TerminalKey terminalKey);
	
	/**
	 * ���˻���IP��ѯATM����
	 * @param strTerminalID �˻���
	 * @param ip IP��ַ
	 * @return
	 */
	Map<String,Object> findSysTermByTermIdAndIP(String strTerminalID,String ip);
	
	/**
	 * ����ATM EPPID
	 * @param strTerminalID �˻���
	 * @param eppId EPPID
	 * @return
	 */
	int updateTerminalEPPID(String strTerminalID,String eppId);
	
	/**
	 * �����ն˺Ų��Ҷ�ӦEppKey����
	 * @param strTerminalID �˻���
	 * @param eppId EPPID
	 * @return
	 */
	EppKey getEppInfoByTerminalId(String terminalId); 
	
	
	/**
	 * �����ն˺Ų���Master Key
	 * @param terminalId
	 * @return
	 */
	String getMasterKeyByTerminalId(String terminalId); 

}
