package com.yuanda.request;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.yuanda.Constant;
import com.yuanda.port.HttpClientRequest;
import com.yuanda.utils.HttpClientHelper;

public class LoginRequest extends HttpClientRequest {

	private String userName;
	private String password;
	private boolean isGetLoginCookie = false;//是否获取登陆cookie
	public LoginRequest() {
	}
	public LoginRequest(String userName,String password){
		this.userName = userName;
		this.password = password;
	}
	@Override
	public void doGet() {
		
	}

	@Override
	public void doPost() {
		HttpPost httpPost = new HttpPost(Constant.YUANDA_LOGIN_URL);
		System.out.println("httpget:"+httpPost.getURI());
		
		LinkedList<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>(); 
		params.add(new BasicNameValuePair("__VIEWSTATE", "/wEPDwUKMTg0NDI4MDE5OGRkFUZOE3C7QQL4pJ/OooIN7IVnK5Qfznhym5mN84V5JNQ=")); 
		params.add(new BasicNameValuePair("__EVENTVALIDATION", "/wEWBgKyrJC6AwKl1bKzCQK1qbSRCwLoyMm8DwLi44eGDAKAv7D9CmhsXW9MljUvafu1DHCUaQFrdAWsE0NBXcwobLtWNjsS")); 
		params.add(new BasicNameValuePair("BtnLogin", "登  录")); 
		params.add(new BasicNameValuePair("txtUserName", userName)); 
		params.add(new BasicNameValuePair("txtPassword", password)); 
		
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse response = httpClient.execute(httpPost); //执行POST方法 
			if(!"500".equals(response.getStatusLine().getStatusCode())){
				isGetLoginCookie = true;
			}
			System.out.println("resCode = " + response.getStatusLine().getStatusCode());//获取响应码 
			//System.out.println("result+" + EntityUtils.toString(response.getEntity(), "utf-8")); //获取响应内容 
		} catch (UnsupportedEncodingException e) {
			System.out.println("获取登陆cookie失败，异常");
			//e.printStackTrace();
		} catch (ClientProtocolException e) {
			System.out.println("获取登陆cookie失败，异常");
			//e.printStackTrace();
		} catch (IOException e) {
			System.out.println("获取登陆cookie失败，异常");
			//e.printStackTrace();
		}finally{
			httpPost.abort();
		}
	}
	
	@Override
	public void setRequestHead() {
	}

	@Override
	public Object getDate() {
		return isGetLoginCookie;
	}
}
