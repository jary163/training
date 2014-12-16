package com.yuanda.utils;

import java.util.ArrayList;
import java.util.List;

import com.yuanda.bean.CarsInfo;

public class SimulationUtils {

	
	/**
	 * 根据每辆车的信息，自动生成上午、下午、晚上的请求对象
	 * @param analysisCarsInfo
	 * @param fileName 
	 * @param timer 约车时间段
	 * @return
	 */
	public synchronized static List<CarsInfo> getCarsInfo(List<CarsInfo> analysisCarsInfo, String fileName,int timer){
		List<CarsInfo> aftercarsInfos = new ArrayList<CarsInfo>();
		for (CarsInfo carsInfo : analysisCarsInfo) {
			setThreeModel(aftercarsInfos,carsInfo.getJLCBH(),carsInfo.getCNBH(),fileName,timer);
		}
		return aftercarsInfos;
	}
	
/*	*//**
	 * 获取手动填写的carsinfo信息
	 * @return
	 *//*
	public static List<CarsInfo> getCarsInfoSimu(long time,int timer) {
		List<CarsInfo> carsInfos = new ArrayList<CarsInfo>();
		setThreeModel(carsInfos,"612519","22071","20141128",timer);
		setThreeModel(carsInfos,"612520","22072","20141128",timer);
		setThreeModel(carsInfos,"612541","22081","20141128",timer);
		setThreeModel(carsInfos,"612545","22085","20141128",timer);
		setThreeModel(carsInfos,"612545","22085","20141129",timer);
		setThreeModel(carsInfos,"612549","22089","20141128",timer);
		setThreeModel(carsInfos,"612549","22089","20141129",timer);
		
		return carsInfos;
	}*/
	
	/**
	 * 添加三种状态
	 * @param carsInfos
	 * @param JLCBH  **编号
	 * @param CNBH   小车牌号
	 * @param date	日期
	 * @param timer 约车时间段
	 */
	public  static void setThreeModel(List<CarsInfo> carsInfos,String JLCBH,String CNBH,String date,int timer){
		if((timer&Style.TIMER_MORNING)==Style.TIMER_MORNING){
			CarsInfo carsInfoMorning = new CarsInfo();
			carsInfoMorning.setAppointDate(date);
			carsInfoMorning.setCNBH(CNBH);
			carsInfoMorning.setJLCBH(JLCBH);
			carsInfoMorning.setXNSD("812");//设置上午约车的时间信息
			carsInfos.add(carsInfoMorning);
		}
		if((timer&Style.TIMER_AFTERNOON)==Style.TIMER_AFTERNOON){
			CarsInfo carsInfoAftterNoon = new CarsInfo();
			carsInfoAftterNoon.setAppointDate(date);
			carsInfoAftterNoon.setCNBH(CNBH);
			carsInfoAftterNoon.setJLCBH(JLCBH);
			carsInfoAftterNoon.setXNSD("15");//设置下午约车的时间信息
			carsInfos.add(carsInfoAftterNoon);
		}
		if((timer&Style.TIMER_EVENING)==Style.TIMER_EVENING){
			CarsInfo carsInfoEvening = new CarsInfo();
			carsInfoEvening.setAppointDate(date);
			carsInfoEvening.setCNBH(CNBH);
			carsInfoEvening.setJLCBH(JLCBH);
			carsInfoEvening.setXNSD("58");//设置晚上约车的时间信息
			carsInfos.add(carsInfoEvening);
		}
		
	}

}
