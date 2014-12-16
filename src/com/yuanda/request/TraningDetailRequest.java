package com.yuanda.request;

import java.io.IOException;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import com.yuanda.Constant;
import com.yuanda.bean.CarsInfo;
import com.yuanda.bean.OrderInfo;
import com.yuanda.port.HttpClientRequest;
import com.yuanda.utils.OrderUtils;
import com.yuanda.utils.Style;

public class TraningDetailRequest extends HttpClientRequest {

	private OrderInfo orderInfo;
	private List<CarsInfo> analysisCarsInfo;
	private boolean isGetCarsInfo = false;
	@Override
	public void doGet() {
		isGetCarsInfo = false;
		if(null==orderInfo){
			throw new RuntimeException("give me orderInfo,orderInfo is Null");
		}
		
		HttpGet httpGet = new HttpGet(Constant.YUANDA_ASSIGN_PERSON_DETAIL+
				"?filters%5Byyrq%5D="+orderInfo.getAppointDate()+
				"&filters%5Bxnsd%5D="+orderInfo.getAppingPart()+
				"&filters%5Bxllxid%5D="+ (getType().equals(Style.SUBJECT_TOW)?1:6) +   //科目二   &filters%5Bxllxid%5D=1 科目三   &filters%5Bxllxid%5D=6
				"&filters%5Btype%5D=km2Car"+
				"&pageno=1"+"&pagesize=10");
		System.out.println("获取"+orderInfo.getAppointDate()+"信息--->httpget:"+httpGet.getURI());
		try
		{		
			
			// 客户端执行get请求 返回响应实体
			HttpResponse response = httpClient.execute(httpGet);
			
			// 服务器响应状态行
			StatusLine statusLine = response.getStatusLine();
			System.out.println(statusLine);
			if(statusLine.toString().contains("500")){//访问失败，直接返回
				System.out.println("获取约车详情失败，正在请求重试");
				return;
			}
			
			Header[] heads = response.getAllHeaders();
			// 打印所有响应头
			for(Header h:heads){
				//System.out.println(h.getName()+":"+h.getValue());
			}
			
			// 获取响应消息实体
			HttpEntity entity = response.getEntity();
			
			//System.out.println("------------------------------------");
			
			
			
			if(entity != null){
				isGetCarsInfo = true;		
				//响应内容
				//System.out.println(EntityUtils.toString(entity));
				String string = new String(EntityUtils.toString(entity));
				string = string.replaceAll("]([\\s\\S]*)", "]");
				//System.out.println(string);
				//保存某一天的详细约车信息
				OrderUtils.saveSingledayTraningDetailInfo(string,orderInfo.getAppointDate());
/*				analysisCarsInfo = OrderUtils.analysisCarsInfo(string);
				for (CarsInfo carsInfo : analysisCarsInfo) {
					carsInfo.setAppointDate(orderInfo.getAppointDate());
				}*/
				
				
				System.out.println("----------------------------------------");
				// 响应内容长度
				System.out.println("1响应内容长度："+entity.getContentLength());
			}
		} catch (ClientProtocolException e){
			System.out.println("获取预约详细信息失败，异常");
			//e.printStackTrace();
		} catch (IOException e){
			System.out.println("获取预约详细信息失败，异常");
			//e.printStackTrace();
		}finally{
			httpGet.abort();
		}
	}

	@Override
	public void doPost() {

	}

	@Override
	public void setDate(Object object) {
		if(object instanceof OrderInfo){
			this.orderInfo = (OrderInfo) object;
		}
	}
	
	@Override
	public Object getDate() {
		return isGetCarsInfo;
	}
	
	@Override
	public void setRequestHead() {

	}

}
