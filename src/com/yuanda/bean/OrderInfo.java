package com.yuanda.bean;

/**
 * 教练信息
 * @author zhangying2
 *
 */
public class OrderInfo {
	private String name;
	/**
	 * 预约日期
	 */
	private String appointDate;
	/**
	 * 预约时段<br/>
	 * <p>812:上午</p>
	 * <p>15:下午</p>
	 * <p>58:晚上</p>
	 * <p>-1:全天</p>
	 */
	private String appingPart;
	/**
	 * 剩余数量
	 */
	private int num;
	/**
	 * 是否有车
	 */
	private boolean isCar;
	
	/**
	 * （未知）
	 */
	private String id;
	/**
	 * 约车类型
	 */
	private String type;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAppointDate() {
		return appointDate;
	}
	public void setAppointDate(String appointDate) {
		this.appointDate = appointDate;
	}
	public String getAppingPart() {
		return appingPart;
	}
	public void setAppingPart(String appingPart) {
		this.appingPart = appingPart;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public boolean isCar() {
		return isCar;
	}
	public void setCar(boolean isCar) {
		this.isCar = isCar;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "约车日期:"+appointDate+"   约车时间段:"+appingPart+"    剩余数量:"+num+"    id:"+id;
	}
	
}
