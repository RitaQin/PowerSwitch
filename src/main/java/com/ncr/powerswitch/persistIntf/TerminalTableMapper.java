package com.ncr.powerswitch.persistIntf;

import java.util.List;

import com.ncr.powerswitch.dataObject.Terminal;

/**
 * TERMINALTABLE相关操作
 * @author rq185015
 *
 */

public interface TerminalTableMapper extends SqlMapperIntf {
	
	//返回所有终端信息
	public List<Terminal> getAllTerminals();
	
	//返回所有终端号
	public List<String> getTerminalIdList(); 
	
	//根据终端号返回终端信息
	public Terminal getTerminalById(String terminalId);
	
	/**
	//增加新的终端
	public void addTerminal(Terminal newTerminal);
	
	//删除终端
	public void deleteTerminal(String terminalId);
	
	//更新终端
	public void updateTerminal(Terminal updatedTerminal);
	**/

}
