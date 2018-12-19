package com.ncr.powerswitch.persistIntf;


import java.util.List;

import com.ncr.powerswitch.dataObject.HostKey;
/**
 * HOSTKEYTABLE��ز���
 * @author rq185015
 *
 */

public interface HostKeyTableMapper extends SqlMapperIntf {
	
	//�������е��ն˺�
	public List<String> getTerminalsIds(); 
	
	//��terminalId����key 
	public HostKey getHostKeyByTerminalId();

}
