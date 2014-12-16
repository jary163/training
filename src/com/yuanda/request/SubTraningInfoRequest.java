package com.yuanda.request;

import java.io.IOException;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import com.yuanda.Constant;
import com.yuanda.bean.CarsInfo;
import com.yuanda.bean.OrderInfo;
import com.yuanda.port.HttpClientRequest;
import com.yuanda.utils.OrderUtils;

public class SubTraningInfoRequest extends HttpClientRequest{

	private CarsInfo carsInfo;
	//private boolean isTraningSuccess;//是否预约成功
	public static int REQUEST_FAILE = 1;
	public static int REQUEST_SUCCESS = 2;
	public static int REQUEST_CONTINUE = 3;
	public static int state = 0;
	@Override
	public void doGet() {
		if(null==carsInfo){
			throw new RuntimeException("give me orderInfo,orderInfo is Null");
		}
		//GET http://yuanda.bjxueche.net/Tools/km2.aspx?jlcbh=612519&yyrqbegin=20141128&xnsd=58&trainType=1&type=km2Car2&_=1416531632103 HTTP/1.1
		HttpGet httpGet = new HttpGet(Constant.YUANDA_ENTER_TRANNING_REQUEST_URL+
				"?jlcbh="+carsInfo.getJLCBH()+
				"&yyrqbegin="+carsInfo.getAppointDate()+
				//"&yyrqbegin=20141126"+
				"&xnsd="+carsInfo.getXNSD()+
				//"&xnsd=15"+
				"&trainType=1"+
				"&type=km2Car2");
		System.out.println("提交约车请求--->httpget:"+httpGet.getURI());
		try
		{		
			
			// 客户端执行get请求 返回响应实体
			HttpResponse response = httpClient.execute(httpGet);
			
			// 服务器响应状态行
			System.out.println(response.getStatusLine());
			
			Header[] heads = response.getAllHeaders();
			// 打印所有响应头
			for(Header h:heads){
				//System.out.println(h.getName()+":"+h.getValue());
			}
			
			// 获取响应消息实体
			HttpEntity entity = response.getEntity();
			
			//System.out.println("------------------------------------");
			
			
			if(entity != null){
								
				//响应内容
				String responseMessage = EntityUtils.toString(entity);
				if(!responseMessage.contains("NullReferenceException")&&!responseMessage.contains("不正确的数据")&&!responseMessage.contains("JsonReaderException")){//TODO  没有处理（科目二训练次数已够或未约晚段）
					System.out.println("提交确认信息响应内容:"+responseMessage);
					state = REQUEST_CONTINUE;
				}else{
					state = REQUEST_FAILE;
					//isTraningSuccess = true;//包含不正确的数据，说明此次回话无效，重新发送请求
					System.out.println("请求错误，请重试");
				}
				if(responseMessage.contains("成功")){
					state = REQUEST_SUCCESS;
				}
				//System.out.println("----------------------------------------");
				// 响应内容长度
				//System.out.println("响应内容长度："+entity.getContentLength());
			}
		} catch (ClientProtocolException e){
			System.out.println("提交预约信息失败，异常");
			//e.printStackTrace();
		} catch (IOException e){
			System.out.println("提交预约信息失败，异常");
			//e.printStackTrace();
		}finally{
			httpGet.abort();
		}
		
	}

	@Override
	public void doPost() {
		
	}

	@Override
	public void setRequestHead() {
		
	}

	@Override
	public void setDate(Object object) {
		if(object instanceof CarsInfo){
			this.carsInfo = (CarsInfo) object;
		}
	}
	
	@Override
	public Object getDate() {
		return state;
	}
}
