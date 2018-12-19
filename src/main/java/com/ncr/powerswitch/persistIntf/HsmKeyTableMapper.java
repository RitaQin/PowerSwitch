package com.ncr.powerswitch.persistIntf;

import java.util.List;

import com.ncr.powerswitch.dataObject.HsmKey;

/**
 * HSMKEYTABLE相关操作
 * @author rq185015
 *
 */

public interface HsmKeyTableMapper extends SqlMapperIntf {
	
	//通过厂商名查找Hsm相关密钥
	public HsmKey getHsmKeySetByManu(String manu);

	//通过EPP查找Hsm相关密钥
	public HsmKey getHsmKeySetByEpp(String epp);

	//通过厂商名和Epp查找Hsm相关密钥
	public HsmKey getHsmKeySetByManuAndEpp(String manu, String epp);

	//返回所有Hsm相关密钥
	public List<HsmKey> getAllHsmKey();

}
