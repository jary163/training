package com.yuanda;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.cookie.Cookie;
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
import com.yuanda.utils.Style;

public class Training extends BaseRunnable{

	private  List<OrderInfo> orderInfos;
	private  List<CarsInfo> carsInfos;
	private boolean isGetCarsInfo = false;//是否获取到了当天约车详细信息
	private  String userName;
	private String password;
	private  boolean morning = true;
	private  boolean afternoon = true;
	private  boolean evening = true;
	//private  boolean isTraningSuccess = false; //约车是否成功，是否需要等待一段时间再约车
	private int state;//预约状态
	private static final long WAITTING_TIME = 3000;//约车失败，10s之后再请求一次
	private static final int REQUEST_MAX = 5;//每个请求最大的请求次数
	private  String traningTime="20141213";//可以约车的时间段一
	private  String traningOtherTime="20141214";//可以约车的时间段二
	private  HttpClient httpClient;
	private Style trainingStyle;//科目类型
	private int timer;//约车时间段
	
	public Training(){};
	public Training(String userName,String password){
		this.userName = userName;
		this.password = password;
	}

	public Training(String userName, String password, Style subjectTow,String traningTime,int timer) {
		this.userName = userName;
		this.password = password;
		this.trainingStyle = subjectTow;
		this.traningTime = traningTime;
		this.timer = timer;
	}
	@Override
	public void run() {
		while(SubTraningInfoRequest.REQUEST_SUCCESS!=state){
			state = SubTraningInfoRequest.REQUEST_CONTINUE;
			httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000); 
			
			sendLoginRequest(userName, password);
			
			if(!OrderUtils.traningFileIsExists(traningTime)){
				System.out.println("文件不存在，获取一次所有约车信息："+Constant.fileSavePath+File.separator+traningTime);
				sendAllTraningRequest();
				
				sendTraningDetailRequest();
			}else{
				System.out.println("文件存在，直接进行约车"+Constant.fileSavePath+File.separator+traningTime);
			}
			
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
		while(!isGetAllTraningInfo&&count<REQUEST_MAX){
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
		if(null!=orderInfos&&orderInfos.size()>1){
			OrderUtils.saveAllTraning(orderInfos);
			//for (int i = 0; i < orderInfos.size()*REQUEST_MAX||(null==carsInfos||carsInfos.size()<1); i++) {
			for (int i = 0;!isGetCarsInfo&&i<REQUEST_MAX; i++) {
				//System.out.println("size:"+orderInfos.size()+"   i:"+i);
				OrderInfo orderInfo = orderInfos.get(i%orderInfos.size());
				if(null!=orderInfo.getAppingPart()&&
						(orderInfo.getAppointDate().equals(traningTime))){//TODO 目前只能处理获取一天的详细约车信息
						System.out.println("满足日期约车的条件："+orderInfo);
						getTraningDetailInfo(orderInfo);
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
		isGetCarsInfo = (Boolean) traningInfo.getDate();
	} 
	
	
	/**
	 * 对信息进行提交
	 */
	private  void submitTraningInfo() {
		if(null==carsInfos&&OrderUtils.traningFileIsExists(traningTime)){//TODO 涉及到多线程，可以设置成一个公共变量进行读取
			carsInfos = OrderUtils.getCarsInfoFromLocal(traningTime,timer);
		}
		if(null!=carsInfos&&carsInfos.size()>=1){//读取完了之后有可能还为空
			HttpClientRequest subTraningInfo = new SubTraningInfoRequest();
			List<Cookie> cookies = ((DefaultHttpClient)httpClient).getCookieStore().getCookies();
			for (int i = 0; i < cookies.size(); i++) {
			System.out.println("cookiename=="+cookies.get(i).getName());
			System.out.println("cookieValue=="+cookies.get(i).getValue());
			System.out.println("Domain=="+cookies.get(i).getDomain());
			System.out.println("Path=="+cookies.get(i).getPath());
			System.out.println("Version=="+cookies.get(i).getVersion());
			}
			for (int i = 0; i <carsInfos.size()&&SubTraningInfoRequest.REQUEST_CONTINUE==state; i++) {//对已经获取到的约车信息进行遍历，看看能不能约上
				System.out.println("提交约车信息:"+carsInfos.get(i));
				subTraningInfo.setHttpClient(httpClient);
				subTraningInfo.setDate(carsInfos.get(i%carsInfos.size()));
				subTraningInfo.doGet();
				state = (Integer) subTraningInfo.getDate();
				//isTraningSuccess = (boolean) subTraningInfo.getDate();
			}
		}
		System.out.println("submitCarsInfo:"+carsInfos);
		if(SubTraningInfoRequest.REQUEST_SUCCESS==state){
			System.out.println("预约成功");
		}else{
			System.out.println("预约失败");
		}
		
	}

}
