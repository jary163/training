package com.yuanda;

import java.util.Date;
import java.util.List;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import com.yuanda.bean.CarsInfo;
import com.yuanda.bean.OrderInfo;
import com.yuanda.port.BaseRunnable;
import com.yuanda.port.HttpClientRequest;
import com.yuanda.request.GetTraningInfoRequest;
import com.yuanda.request.LoginRequest;
import com.yuanda.request.SubTraningInfoRequest;
import com.yuanda.request.TraningDetailRequest;
import com.yuanda.utils.OrderUtils;
import com.yuanda.utils.SimulationUtils;

public class DirectTraining extends BaseRunnable{

	private  List<OrderInfo> orderInfos;
	private  List<CarsInfo> carsInfos;
	private  String userName;
	private String password;
	private  boolean morning = true;
	private  boolean afternoon = true;
	private  boolean evening = true;
	private  boolean isTraningSuccess = false; //约车是否成功，是否需要等待一段时间再约车
	private static final long WAITTING_TIME = 5000;//约车失败，10s之后再请求一次
	private static final int REQUEST_MAX = 50;//每个请求最大的请求次数
	private  String traningTime="20141127";//可以约车的时间段一
	private  String traningOtherTime="20141128";//可以约车的时间段二
	private  HttpClient httpClient;
	
	public DirectTraining(){};
	public DirectTraining(String userName,String password){
		this.userName = userName;
		this.password = password;
	}

	@Override
	public void run() {
		while(!isTraningSuccess){
			httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000); 
			
			sendLoginRequest(userName, password);
			
			//sendAllTraningRequest();
			
			//sendTraningDetailRequest();
			
			submitTraningInfo();

			try {
				Thread.sleep(WAITTING_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		
	}

	
	/**
	 * 登录获取cookie信息
	 */
	private  void sendLoginRequest(String userName, String password) {
		boolean isgetCookie = false;
		int count = 0;
		while(!isgetCookie&&count<REQUEST_MAX){
			System.out.println("获取登陆cookie:"+new Date(System.currentTimeMillis()).toLocaleString());
			HttpClientRequest loginRequest = new LoginRequest(userName, password);
			loginRequest.setHttpClient(httpClient);
			loginRequest.doPost();
			isgetCookie = (Boolean) loginRequest.getDate();
			count++;
		}
	}

	/**
	 * 通过cookie信息获取所有能约车信息
	 */
	private  void sendAllTraningRequest() {
		boolean isGetAllTraningInfo = false;
		int count = 0 ;
		//while(!isGetAllTraningInfo||count<REQUEST_MAX){
		while(!isGetAllTraningInfo){
			System.out.println("获取所有能约车信息:"+count);
			HttpClientRequest traningInfo = new GetTraningInfoRequest();
			traningInfo.setHttpClient(httpClient);
			traningInfo.doGet();
			orderInfos = (List<OrderInfo>) traningInfo.getDate();
			isGetAllTraningInfo = (null!=orderInfos&&orderInfos.size()>=1?true:false);
			count++;
		}
	}



	/**
	 * 选择性提交预约信息
	 */
	private  void sendTraningDetailRequest() {
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

	}

	/**
	 * 通过cookie信息获取将要预约当天的详细信息
	 */
	private  void getTraningDetailInfo(OrderInfo orderInfo) {

		HttpClientRequest traningInfo = new TraningDetailRequest();
		traningInfo.setHttpClient(httpClient);
		traningInfo.setDate(orderInfo);
		traningInfo.doGet(); 
		carsInfos = (List<CarsInfo>) traningInfo.getDate();
	} 
	
	
	/**
	 * 对信息进行提交
	 */
	private  void submitTraningInfo() {/*
		carsInfos = SimulationUtils.getCarsInfoSimu(System.currentTimeMillis());//TODO  获取手动填写的carsInfo信息,暂时废弃掉
		HttpClientRequest subTraningInfo = new SubTraningInfoRequest();
			for (int i = 0; i < carsInfos.size()*REQUEST_MAX&&!isTraningSuccess; i++) {//对已经获取到的约车信息进行遍历，看看能不能约上
				System.out.println("提交约车信息:"+carsInfos.get(i%carsInfos.size()));
				subTraningInfo.setHttpClient(httpClient);
				subTraningInfo.setDate(carsInfos.get(i%carsInfos.size()));
				subTraningInfo.doGet();
				isTraningSuccess = (boolean) subTraningInfo.getDate();
			}
		
		if(isTraningSuccess){
			System.out.println("预约成功");
		}else{
			System.out.println("预约失败");
		}
		
	*/}

}
