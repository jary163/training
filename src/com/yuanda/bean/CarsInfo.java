package com.yuanda.bean;

public class CarsInfo {

	private String JLCBH;
	private String CNBH;
	private String YT;
	private String LXBH;
	private String JLYXM;
	private String XNSD;
	private String TCFW;
	private String appointDate;//预约日期
	public String getJLCBH() {
		return JLCBH;
	}
	public void setJLCBH(String jLCBH) {
		JLCBH = jLCBH;
	}
	public String getCNBH() {
		return CNBH;
	}
	public void setCNBH(String cNBH) {
		CNBH = cNBH;
	}
	public String getYT() {
		return YT;
	}
	public void setYT(String yT) {
		YT = yT;
	}
	public String getLXBH() {
		return LXBH;
	}
	public void setLXBH(String lXBH) {
		LXBH = lXBH;
	}
	public String getJLYXM() {
		return JLYXM;
	}
	public void setJLYXM(String jLYXM) {
		JLYXM = jLYXM;
	}
	public String getXNSD() {
		return XNSD;
	}
	public void setXNSD(String xNSD) {
		XNSD = xNSD;
	}
	public String getTCFW() {
		return TCFW;
	}
	public void setTCFW(String tCFW) {
		TCFW = tCFW;
	}
	
	@Override
	public String toString() {
		return"JLCBH(编号):"+JLCBH+"  CNBH:"+CNBH+"  JLYXM:"+JLYXM+"  XNSD（时段）:"+XNSD+"  appointDate:"+appointDate+"  TCFW:"+TCFW+"   YT:"+YT+"  LXBH:"+LXBH;
	}
	public String getAppointDate() {
		return appointDate;
	}
	public void setAppointDate(String appointDate) {
		this.appointDate = appointDate;
	}

	
}
