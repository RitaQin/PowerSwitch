package com.ncr.powerswitch.dataObject;

/**
 * PV_SYS_EPP±Ì POJO¿‡
 * 
 * @author rq185015
 *
 */

public class EppKey implements DataObject {

	public long eppid;
	public String eppName;
	public long mfgCompanyId;
	public String r1;
	public String keyIndex;
	public String mLength;
	public String flag1;
	public String flag2;
	public String index1;
	public String index2;
	public String bankPKLen;
	public String bankPK;
	public String skLen;
	public String sk;
	public String bankPkSignatureLength;
	public String bankPkSignature;
	public String manuSignatureBankLength;
	public String manuSignatureBank;
	public String manuPkLength;
	public String manupk;
	public String mpkSignatureLength;
	public String mpkSignature;
	public String hashType;

	public long getEppid() {
		return eppid;
	}

	public void setEppid(long eppid) {
		this.eppid = eppid;
	}

	public String getEppName() {
		return eppName;
	}

	public void setEppName(String eppName) {
		this.eppName = eppName;
	}

	public long getMfgCompanyId() {
		return mfgCompanyId;
	}

	public void setMfgCompanyId(long mfgCompanyId) {
		this.mfgCompanyId = mfgCompanyId;
	}

	public String getR1() {
		return r1;
	}

	public void setR1(String r1) {
		this.r1 = r1;
	}

	public String getKeyIndex() {
		return keyIndex;
	}

	public void setKeyIndex(String keyIndex) {
		this.keyIndex = keyIndex;
	}

	public String getmLength() {
		return mLength;
	}

	public void setmLength(String mLength) {
		this.mLength = mLength;
	}

	public String getFlag1() {
		return flag1;
	}

	public void setFlag1(String flag1) {
		this.flag1 = flag1;
	}

	public String getFlag2() {
		return flag2;
	}

	public void setFlag2(String flag2) {
		this.flag2 = flag2;
	}

	public String getIndex1() {
		return index1;
	}

	public void setIndex1(String index1) {
		this.index1 = index1;
	}

	public String getIndex2() {
		return index2;
	}

	public void setIndex2(String index2) {
		this.index2 = index2;
	}

	public String getBankPKLen() {
		return bankPKLen;
	}

	public void setBankPKLen(String bankPKLen) {
		this.bankPKLen = bankPKLen;
	}

	public String getBankPK() {
		return bankPK;
	}

	public void setBankPK(String bankPK) {
		this.bankPK = bankPK;
	}

	public String getSkLen() {
		return skLen;
	}

	public void setSkLen(String skLen) {
		this.skLen = skLen;
	}

	public String getSk() {
		return sk;
	}

	public void setSk(String sk) {
		this.sk = sk;
	}

	public String getBankPkSignatureLength() {
		return bankPkSignatureLength;
	}

	public void setBankPkSignatureLength(String bankPkSignatureLength) {
		this.bankPkSignatureLength = bankPkSignatureLength;
	}

	public String getBankPkSignature() {
		return bankPkSignature;
	}

	public void setBankPkSignature(String bankPkSignature) {
		this.bankPkSignature = bankPkSignature;
	}

	public String getManuSignatureBankLength() {
		return manuSignatureBankLength;
	}

	public void setManuSignatureBankLength(String manuSignatureBankLength) {
		this.manuSignatureBankLength = manuSignatureBankLength;
	}

	public String getManuSignatureBank() {
		return manuSignatureBank;
	}

	public void setManuSignatureBank(String manuSignatureBank) {
		this.manuSignatureBank = manuSignatureBank;
	}

	public String getManuPkLength() {
		return manuPkLength;
	}

	public void setManuPkLength(String manuPkLength) {
		this.manuPkLength = manuPkLength;
	}

	public String getManupk() {
		return manupk;
	}

	public void setManupk(String manupk) {
		this.manupk = manupk;
	}

	public String getMpkSignatureLength() {
		return mpkSignatureLength;
	}

	public void setMpkSignatureLength(String mpkSignatureLength) {
		this.mpkSignatureLength = mpkSignatureLength;
	}

	public String getMpkSignature() {
		return mpkSignature;
	}

	public void setMpkSignature(String mpkSignature) {
		this.mpkSignature = mpkSignature;
	}

	public String getHashType() {
		return hashType;
	}

	public void setHashType(String hashType) {
		this.hashType = hashType;
	}

}
