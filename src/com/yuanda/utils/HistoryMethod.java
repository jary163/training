package com.yuanda.utils;

import com.yuanda.Constant;
import com.yuanda.bean.OrderInfo;

public class HistoryMethod {

	/**
	 * Class Training.java
	 * 选择性提交预约信息
	 */
	private  void sendTraningDetailRequest() {/*
		System.out.println("选择性提交预约信息:"+orderInfos);
		if(null!=orderInfos){
			OrderUtils.saveAllTraning(orderInfos);
			//for (int i = 0; i < orderInfos.size()*REQUEST_MAX||(null==carsInfos||carsInfos.size()<1); i++) {
			for (int i = 0;(null==carsInfos||carsInfos.size()<1); i++) {
				OrderInfo orderInfo = orderInfos.get(i%orderInfos.size());
				if(null!=orderInfo.getAppingPart()&&
						(orderInfo.getAppointDate().equals(traningTime)||orderInfo.getAppointDate().equals(traningOtherTime))){
					//TODO 分发线程，尝试获取每一天的详细约车信息
					if (orderInfo.getAppingPart().equals(String.valueOf(Constant.APPINGPART_MORNING))&& morning) {// 如果是预约的早上
						System.out.println("满足上午约车的条件："+orderInfo);
						getTraningDetailInfo(orderInfo);
					} else if (orderInfo.getAppingPart().equals(String.valueOf(Constant.APPINGPART_AFFTERNOON))&& afternoon) {// 如果是预约的下午
						System.out.println("满足下午约车的条件："+orderInfo);
						getTraningDetailInfo(orderInfo);
					} else if (orderInfo.getAppingPart().equals(String.valueOf(Constant.APPINGPART_EVENING))&& evening) {// 如果是预约的晚上
						System.out.println("满足晚上约车的条件："+orderInfo);
						getTraningDetailInfo(orderInfo);
					}
				}
				//System.out.println("crasInfos:"+carsInfos+"    size:"+(null==carsInfos?0:carsInfos.size()));
			}
		}

	*/}
}
