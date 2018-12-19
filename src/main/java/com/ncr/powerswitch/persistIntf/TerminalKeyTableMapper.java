package com.ncr.powerswitch.persistIntf;


import java.util.List;

import com.ncr.powerswitch.dataObject.TerminalKey;
/**
 * HOSTKEYTABLE��ز���
 * @author rq185015
 *
 */

public interface TerminalKeyTableMapper extends SqlMapperIntf {
	
	//�������е��ն˺�
	public List<String> getTerminalsIds(); 
	
	//��terminalId����key 
	public TerminalKey getTerminalKeyByTerminalId(String terminalId);
	
	//����һ���µ��ն���Կ��Ϣ
	public void addTerminalKey(TerminalKey terminalKey);

}
