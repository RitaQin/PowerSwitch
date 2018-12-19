package com.ncr.powerswitch.persistIntf;


import java.util.List;

import com.ncr.powerswitch.dataObject.HostKey;
/**
 * HOSTKEYTABLE相关操作
 * @author rq185015
 *
 */

public interface HostKeyTableMapper extends SqlMapperIntf {
	
	//返回所有的终端号
	public List<String> getTerminalsIds(); 
	
	//用terminalId搜索key 
	public HostKey getHostKeyByTerminalId();

}
