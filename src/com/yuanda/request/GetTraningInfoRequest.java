package com.yuanda.request;

import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import com.yuanda.Constant;
import com.yuanda.bean.OrderInfo;
import com.yuanda.port.HttpClientRequest;
import com.yuanda.utils.OrderUtils;

public class GetTraningInfoRequest extends HttpClientRequest {

	/**
	 * 约车信息
	 */
	private List<OrderInfo> orderInfos;

	@Override
	public void doGet() {
		HttpGet httpGet = new HttpGet(Constant.YUANDA_ALLTRANING_INFO_URL);
		System.out.println("获取所有的能约的车信息:GetTraningInfoRequest:"+httpGet.getURI());

		try
		{
			// 客户端执行get请求 返回响应实体
			HttpResponse response = httpClient.execute(httpGet);

			// 服务器响应状态行
			StatusLine statusLine = response.getStatusLine();
			/*if(statusLine.toString().contains("500")){
				System.out.println("获取能约车信息时失败,请重试:responseCode:"+statusLine);
				return ;
			}*/
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
				//System.out.println(EntityUtils.toString(entity));
				String string = EntityUtils.toString(entity);
				//System.out.println(string);
				orderInfos = OrderUtils.analysisForString(string);

				//System.out.println("----------------------------------------");
				// 响应内容长度
				//System.out.println("响应内容长度："+entity.getContentLength());
			}
		} catch (ClientProtocolException e){
			System.out.println("获取所有约车请求，异常");
			//e.printStackTrace();
		} catch (Exception e){
			System.out.println("获取所有约车请求，异常");
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
	public Object getDate() {
		return orderInfos;
	}
}
