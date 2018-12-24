package com.ncr.powerswitch.persistIntf;

import java.util.List;

import com.ncr.powerswitch.dataObject.EppKey;

/**
 * PV_SYS_EPP TABLE��ز���
 * @author rq185015
 *
 */

public interface EppTableMapper extends SqlMapperIntf {
	
	//ͨ������������Epp�����Կ
	public EppKey getEppKeySetByManu(String manu);

	//ͨ��EppId ����Epp�����Կ
	public EppKey getEppKeySetByEppId(String eppId);

	//ͨ����������Epp����Hsm�����Կ
	public EppKey getEppKeySetByManuAndEppId(String manu, String eppId);

	//��������Epp�����Կ
	public List<EppKey> getAllEppKey();
	

}
