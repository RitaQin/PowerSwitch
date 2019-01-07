package com.ncr.powerswitch.persistIntf;

import java.util.List;

import com.ncr.powerswitch.dataObject.EppKey;

/**
 * PV_SYS_EPP TABLE操作相关类
 * @author rq185015
 *
 */

public interface EppTableMapper extends SqlMapperIntf {
	
	public EppKey getEppKeySetByManu(String manu);

	
	public EppKey getEppKeySetByEppId(String eppId);

	//ͨ根据厂商和Epp键盘序号获取密码键盘信息
	public EppKey getEppKeySetByManuAndEppId(String manu, String eppId);

	//返回所有种类的密码键盘信息
	public List<EppKey> getAllEppKey();
	
	//返回终端设备的密钥键盘信息
	public EppKey getEppKeySetByTerminalId(String terminalId);
	

}
