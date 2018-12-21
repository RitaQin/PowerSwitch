package com.ncr.powerswitch.DAO;

import java.util.Map;

import com.ncr.powerswitch.dataObject.Terminal;
import com.ncr.powerswitch.dataObject.TerminalKey;
import com.ncr.powerswitch.persistIntf.EppTableMapper;
import com.ncr.powerswitch.persistIntf.TerminalKeyTableMapper;
import com.ncr.powerswitch.persistIntf.TerminalTableMapper;

public class HsmDaoImpl implements HsmDao {
	
	private EppTableMapper hsmMapper; 
	private TerminalKeyTableMapper tkMapper; 
	private TerminalTableMapper termMapper; 
	
	public HsmDaoImpl(EppTableMapper hsmMapper, TerminalKeyTableMapper tkMapper, TerminalTableMapper termMapper ) {
		this.hsmMapper = hsmMapper; 
		this.tkMapper = tkMapper; 
		this.termMapper = termMapper;	
	}

	@Override
	public int terminalIDIsValid(String ip, String terminalId) {
		try {
			Terminal terminal = termMapper.getTerminalById(terminalId);
			// 非法端机号
			if (terminal == null) {
				return 2;
			}
			// 端机号通过校验
			if (terminal.ip.equals(ip)) {
				return 1;
			}
			// 端机号与IP地址不匹配
			return 3;
		} catch (Exception e) {
			e.printStackTrace();
			//验证报错
			return 4; 
		}
	}

	@Override
	public Map<String, Object> findManufacturerByIPAndTerminalID(String ip, String terminalId) {
		
		return null;
	}

	@Override
	public boolean updateTerminalKey(TerminalKey terminalKey) {
		return false;
	}

	@Override
	public Map<String, Object> findSysTermByTermIdAndIP(String strTerminalID, String ip) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateTerminalEPPID(String strTerminalID, String eppId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean insertMasterKey(TerminalKey terminalKey) {
		try {
			tkMapper.addTerminalKey(terminalKey);
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
