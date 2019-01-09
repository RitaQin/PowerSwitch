package com.ncr.powerswitch.esb.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Qt02 ESB返回消息报文body
 * 
 * @author rq185015
 *
 */

@XStreamAlias("BODY")
public class Qt02Body implements BodyIntf {

	@XStreamAlias("CardNo")
	private String CardNo;
	
	@XStreamAlias("CardSt")
	private String CardSt;
	
	@XStreamAlias("QuryPwdFlg")
	private String QuryPwdFlg;
	
	@XStreamAlias("CnlCstNo")
	private String CnlCstNo;
	
	@XStreamAlias("IdentTp")
	private String IdentTp;
	
	@XStreamAlias("IdentNo")
	private String IdentNo;
	
	@XStreamAlias("CstShrtNm")
	private String CstShrtNm;
	
	@XStreamAlias("OpnAcctInstId")
	private String OpnAcctInstId;
	
	@XStreamAlias("EmailAdr")
	private String EmailAdr;
	
	@XStreamAlias("Gender")
	private String Gender;
	
	@XStreamAlias("BrthDt")
	private String BrthDt;
	
	@XStreamAlias("DayTotExtctAmt")
	private String DayTotExtctAmt;
	
	@XStreamAlias("AcctUseBal")
	private String AcctUseBal;
	
	@XStreamAlias("AcctSt")
	private String AcctSt;
	
	@XStreamAlias("DrawTp")
	private String DrawTp;
	
	@XStreamAlias("CardhoerIdentTp")
	private String CardhoerIdentTp;
	
	@XStreamAlias("CardhoerIdentNo")
	private String CardhoerIdentNo;
	
	@XStreamAlias("CardhoerNm")
	private String CardhoerNm;
	
	@XStreamAlias("IndStrg")
	private String IndStrg;
	
	@XStreamAlias("AcctBal")
	private String AcctBal;
	
	@XStreamAlias("PtvNtvFlg")
	private String PtvNtvFlg;
	
	@XStreamAlias("AcctTp")
	private String AcctTp;
	
	@XStreamAlias("FlgZone")
	private String FlgZone;
	
	@XStreamAlias("CstNmN2")
	private String CstNmN2;

	public String getCardNo() {
		return CardNo;
	}

	public void setCardNo(String cardNo) {
		CardNo = cardNo;
	}

	public String getCardSt() {
		return CardSt;
	}

	public void setCardSt(String cardSt) {
		CardSt = cardSt;
	}

	public String getQuryPwdFlg() {
		return QuryPwdFlg;
	}

	public void setQuryPwdFlg(String quryPwdFlg) {
		QuryPwdFlg = quryPwdFlg;
	}

	public String getCnlCstNo() {
		return CnlCstNo;
	}

	public void setCnlCstNo(String cnlCstNo) {
		CnlCstNo = cnlCstNo;
	}

	public String getIdentTp() {
		return IdentTp;
	}

	public void setIdentTp(String identTp) {
		IdentTp = identTp;
	}

	public String getIdentNo() {
		return IdentNo;
	}

	public void setIdentNo(String identNo) {
		IdentNo = identNo;
	}

	public String getCstShrtNm() {
		return CstShrtNm;
	}

	public void setCstShrtNm(String cstShrtNm) {
		CstShrtNm = cstShrtNm;
	}

	public String getOpnAcctInstId() {
		return OpnAcctInstId;
	}

	public void setOpnAcctInstId(String opnAcctInstId) {
		OpnAcctInstId = opnAcctInstId;
	}

	public String getEmailAdr() {
		return EmailAdr;
	}

	public void setEmailAdr(String emailAdr) {
		EmailAdr = emailAdr;
	}

	public String getGender() {
		return Gender;
	}

	public void setGender(String gender) {
		Gender = gender;
	}

	public String getBrthDt() {
		return BrthDt;
	}

	public void setBrthDt(String brthDt) {
		BrthDt = brthDt;
	}

	public String getDayTotExtctAmt() {
		return DayTotExtctAmt;
	}

	public void setDayTotExtctAmt(String dayTotExtctAmt) {
		DayTotExtctAmt = dayTotExtctAmt;
	}

	public String getAcctUseBal() {
		return AcctUseBal;
	}

	public void setAcctUseBal(String acctUseBal) {
		AcctUseBal = acctUseBal;
	}

	public String getAcctSt() {
		return AcctSt;
	}

	public void setAcctSt(String acctSt) {
		AcctSt = acctSt;
	}

	public String getDrawTp() {
		return DrawTp;
	}

	public void setDrawTp(String drawTp) {
		DrawTp = drawTp;
	}

	public String getCardhoerIdentTp() {
		return CardhoerIdentTp;
	}

	public void setCardhoerIdentTp(String cardhoerIdentTp) {
		CardhoerIdentTp = cardhoerIdentTp;
	}

	public String getCardhoerIdentNo() {
		return CardhoerIdentNo;
	}

	public void setCardhoerIdentNo(String cardhoerIdentNo) {
		CardhoerIdentNo = cardhoerIdentNo;
	}

	public String getCardhoerNm() {
		return CardhoerNm;
	}

	public void setCardhoerNm(String cardhoerNm) {
		CardhoerNm = cardhoerNm;
	}

	public String getIndStrg() {
		return IndStrg;
	}

	public void setIndStrg(String indStrg) {
		IndStrg = indStrg;
	}

	public String getAcctBal() {
		return AcctBal;
	}

	public void setAcctBal(String acctBal) {
		AcctBal = acctBal;
	}

	public String getPtvNtvFlg() {
		return PtvNtvFlg;
	}

	public void setPtvNtvFlg(String ptvNtvFlg) {
		PtvNtvFlg = ptvNtvFlg;
	}

	public String getAcctTp() {
		return AcctTp;
	}

	public void setAcctTp(String acctTp) {
		AcctTp = acctTp;
	}

	public String getFlgZone() {
		return FlgZone;
	}

	public void setFlgZone(String flgZone) {
		FlgZone = flgZone;
	}

	public String getCstNmN2() {
		return CstNmN2;
	}

	public void setCstNmN2(String cstNmN2) {
		CstNmN2 = cstNmN2;
	}

}
