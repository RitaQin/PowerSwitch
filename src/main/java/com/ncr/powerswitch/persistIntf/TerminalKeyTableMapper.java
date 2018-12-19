package com.ncr.powerswitch.persistIntf;


import java.util.List;

import com.ncr.powerswitch.dataObject.TerminalKey;
/**
 * HOSTKEYTABLE相关操作
 * @author rq185015
 *
 */

public interface TerminalKeyTableMapper extends SqlMapperIntf {
	
	//返回所有的终端号
	public List<String> getTerminalsIds(); 
	
	//用terminalId搜索key 
	public TerminalKey getTerminalKeyByTerminalId(String terminalId);
	
	//增加一个新的终端密钥信息
	public void addTerminalKey(TerminalKey terminalKey);

}
