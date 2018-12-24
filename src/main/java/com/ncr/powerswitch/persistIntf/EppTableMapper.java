package com.ncr.powerswitch.persistIntf;

import java.util.List;

import com.ncr.powerswitch.dataObject.EppKey;

/**
 * PV_SYS_EPP TABLE相关操作
 * @author rq185015
 *
 */

public interface EppTableMapper extends SqlMapperIntf {
	
	//通过厂商名查找Epp相关密钥
	public EppKey getEppKeySetByManu(String manu);

	//通过EppId 查找Epp相关密钥
	public EppKey getEppKeySetByEppId(String eppId);

	//通过厂商名和Epp查找Hsm相关密钥
	public EppKey getEppKeySetByManuAndEppId(String manu, String eppId);

	//返回所有Epp相关密钥
	public List<EppKey> getAllEppKey();
	

}
