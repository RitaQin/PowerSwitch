package com.ncr.powerswitch.persistIntf;

import java.util.List;

import com.ncr.powerswitch.dataObject.Terminal;

/**
 * TERMINALTABLE操作相关类
 * @author rq185015
 *
 */

public interface TerminalTableMapper extends SqlMapperIntf {
	
	//���������ն���Ϣ
	public List<Terminal> getAllTerminals();
	
	//���������ն˺�
	public List<String> getTerminalIdList(); 
	
	//�����ն˺ŷ����ն���Ϣ
	public Terminal getTerminalById(String terminalId);
	
	/**
	//�����µ��ն�
	public void addTerminal(Terminal newTerminal);
	
	//ɾ���ն�
	public void deleteTerminal(String terminalId);
	
	//�����ն�
	public void updateTerminal(Terminal updatedTerminal);
	**/

}
