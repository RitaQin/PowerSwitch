package com.ncr.powerswitch.persistIntf;

import java.util.List;

import com.ncr.powerswitch.dataObject.HsmKey;

/**
 * HSMKEYTABLE��ز���
 * @author rq185015
 *
 */

public interface HsmKeyTableMapper extends SqlMapperIntf {
	
	//ͨ������������Hsm�����Կ
	public HsmKey getHsmKeySetByManu(String manu);

	//ͨ��EPP����Hsm�����Կ
	public HsmKey getHsmKeySetByEpp(String epp);

	//ͨ����������Epp����Hsm�����Կ
	public HsmKey getHsmKeySetByManuAndEpp(String manu, String epp);

	//��������Hsm�����Կ
	public List<HsmKey> getAllHsmKey();

}
