package com.ncr.powerswitch.remote.atm;

import java.util.List;
import java.util.Map;

public class RemoteDaoImpl implements RemoteDAO {

	@Override
	public int terminalIDIsValid(String IP, String strTerminalID) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Map<String, Object> findManufacturerByIPAndTerminalID(String IP, String strTerminalID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map findHSMSETAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean batchIsValid(String strTerminalID, String strBatch) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int addEJLogon(String strTerminalID, String ip, String strAreaCode) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateEJLogon(String strTerminalID, String ip, String strDate, String lastLine) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Object> findParametersVersionByStrTerminalIDAndStrArea(String strTerminalID, String ip,
			String strArea) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object> findParametersVersionByTerminalIDANDareaCode(String strTerminalID, String areaCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> findParameTextByStrTerminalIDAndStrAreaAndParameType(String strTerminalID, String ip,
			String strArea, String parameType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateATMKEY(String strTerminalID, String ip, String keyText, String keyCheck) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map<String, Object> findSysTermByTermIdAndIP(String strTerminalID, String ip) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateATMEPPID(String strTerminalID, String eppId) {
		// TODO Auto-generated method stub
		return 0;
	}

}
