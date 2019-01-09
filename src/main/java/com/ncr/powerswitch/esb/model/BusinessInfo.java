package com.ncr.powerswitch.esb.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BsnInf")
public class BusinessInfo{
	
	@XStreamAlias("LmtLrgCd")
	private String LmtLrgCd;
	
	@XStreamAlias("LmtSmlCd")
	private String  LmtSmlCd;
	
	@XStreamAlias("CnlInd")
	private String  CnlInd;
	
	@XStreamAlias("AreaCd")
	private String  AreaCd;
	
	@XStreamAlias("PdCd")
	private String PdCd;
	
	@XStreamAlias("Cmnt")
	private String Cmnt;
	
	@XStreamAlias("TermTp")
	private String TermTp;
	
	@XStreamAlias("CashTfrFlg")
	private String CashTfrFlg;
	
	@XStreamAlias("UvslLmt")
	private String UvslLmt;
	
	@XStreamAlias("FtrLmt")
	private String FtrLmt;
	
	@XStreamAlias("Lmt")
	private String Lmt;
	
	@XStreamAlias("EfftDt")
	private String EfftDt;
	
	@XStreamAlias("FailDt")
	private String FailDt;
	/**
	 * @return the lmtLrgCd
	 */
	public String getLmtLrgCd() {
		return LmtLrgCd;
	}
	/**
	 * @param lmtLrgCd the lmtLrgCd to set
	 */
	public void setLmtLrgCd(String lmtLrgCd) {
		LmtLrgCd = lmtLrgCd;
	}
	/**
	 * @return the lmtSmlCd
	 */
	public String getLmtSmlCd() {
		return LmtSmlCd;
	}
	/**
	 * @param lmtSmlCd the lmtSmlCd to set
	 */
	public void setLmtSmlCd(String lmtSmlCd) {
		LmtSmlCd = lmtSmlCd;
	}
	/**
	 * @return the cnlInd
	 */
	public String getCnlInd() {
		return CnlInd;
	}
	/**
	 * @param cnlInd the cnlInd to set
	 */
	public void setCnlInd(String cnlInd) {
		CnlInd = cnlInd;
	}
	/**
	 * @return the areaCd
	 */
	public String getAreaCd() {
		return AreaCd;
	}
	/**
	 * @param areaCd the areaCd to set
	 */
	public void setAreaCd(String areaCd) {
		AreaCd = areaCd;
	}
	/**
	 * @return the pdCd
	 */
	public String getPdCd() {
		return PdCd;
	}
	/**
	 * @param pdCd the pdCd to set
	 */
	public void setPdCd(String pdCd) {
		PdCd = pdCd;
	}
	/**
	 * @return the cmnt
	 */
	public String getCmnt() {
		return Cmnt;
	}
	/**
	 * @param cmnt the cmnt to set
	 */
	public void setCmnt(String cmnt) {
		Cmnt = cmnt;
	}
	/**
	 * @return the termTp
	 */
	public String getTermTp() {
		return TermTp;
	}
	/**
	 * @param termTp the termTp to set
	 */
	public void setTermTp(String termTp) {
		TermTp = termTp;
	}
	/**
	 * @return the cashTfrFlg
	 */
	public String getCashTfrFlg() {
		return CashTfrFlg;
	}
	/**
	 * @param cashTfrFlg the cashTfrFlg to set
	 */
	public void setCashTfrFlg(String cashTfrFlg) {
		CashTfrFlg = cashTfrFlg;
	}
	/**
	 * @return the uvslLmt
	 */
	public String getUvslLmt() {
		return UvslLmt;
	}
	/**
	 * @param uvslLmt the uvslLmt to set
	 */
	public void setUvslLmt(String uvslLmt) {
		UvslLmt = uvslLmt;
	}
	/**
	 * @return the ftrLmt
	 */
	public String getFtrLmt() {
		return FtrLmt;
	}
	/**
	 * @param ftrLmt the ftrLmt to set
	 */
	public void setFtrLmt(String ftrLmt) {
		FtrLmt = ftrLmt;
	}
	/**
	 * @return the lmt
	 */
	public String getLmt() {
		return Lmt;
	}
	/**
	 * @param lmt the lmt to set
	 */
	public void setLmt(String lmt) {
		Lmt = lmt;
	}
	/**
	 * @return the efftDt
	 */
	public String getEfftDt() {
		return EfftDt;
	}
	/**
	 * @param efftDt the efftDt to set
	 */
	public void setEfftDt(String efftDt) {
		EfftDt = efftDt;
	}
	/**
	 * @return the failDt
	 */
	public String getFailDt() {
		return FailDt;
	}
	/**
	 * @param failDt the failDt to set
	 */
	public void setFailDt(String failDt) {
		FailDt = failDt;
	}
}
