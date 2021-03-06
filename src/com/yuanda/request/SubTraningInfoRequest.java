package com.yuanda.request;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import com.yuanda.Constant;
import com.yuanda.bean.CarsInfo;
import com.yuanda.port.HttpClientRequest;
import com.yuanda.utils.Style;

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
				"&trainType="+ (getType().equals(Style.SUBJECT_TOW)?1:6) +                    //科目二 &trainType=1      科目三    &trainType=6
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
					SubTraningInfoRequest.state = SubTraningInfoRequest.REQUEST_CONTINUE;
				}else{
					SubTraningInfoRequest.state = SubTraningInfoRequest.REQUEST_FAILE;
					//isTraningSuccess = true;//包含不正确的数据，说明此次回话无效，重新发送请求
					System.out.println("请求错误，请重试");
				}
				if (responseMessage.contains("预约4个小时")) {// 成功提示，该科目训练每天最多能预约4个小时
					SubTraningInfoRequest.state = SubTraningInfoRequest.REQUEST_SUCCESS;
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
			carsInfo = (CarsInfo) object;
		}
	}

	@Override
	public Object getDate() {
		return SubTraningInfoRequest.state;
	}
}
