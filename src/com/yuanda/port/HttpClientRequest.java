package com.yuanda.port;

import org.apache.http.client.HttpClient;

import com.yuanda.utils.Style;

public abstract class HttpClientRequest {
	protected HttpClient httpClient;
	private Style type;//约车类型

	/**
	 * 发送get请求
	 */
	public abstract void doGet();
	
	/**
	 * 发送post请求
	 */
	public abstract void doPost();
	
	/**
	 * 设置请求头
	 */
	public abstract void setRequestHead();
	
	/**
	 * 一个回话使用一个httpClient
	 * @param httpClient
	 */
	public void setHttpClient(HttpClient httpClient){
		this.httpClient = httpClient;
	}
	
	/**
	 * 获取单个请求中需要的数据
	 * @return
	 */
	public Object getDate(){
		return null;
	}
	
	/**
	 * 设置orderInfo信息
	 * @param object
	 */
	public void setDate(Object object){
		
	}

    public Style getType() {
        return type;
    }

    public void setType(Style type) {
        this.type = type;
    }
}
