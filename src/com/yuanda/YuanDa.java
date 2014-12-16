package com.yuanda;

import java.io.File;
import java.io.IOException;

import com.yuanda.utils.Style;

public class YuanDa {
	public YuanDa(){};
	public static void main(String[] args) {
		YuanDa yuanda = new YuanDa();
		yuanda.initDate();
		//yuanda.startTraning("430703199310182750","931018");
		yuanda.startTraning("152527198207050012","820705",Style.SUBJECT_TOW,"20141213",0x11);
		//yuanda.startTraning("430821198605220025","860522");
		//yuanda.startTraning("220724198802224230","880222");
	}

	/**
	 * 初始化必须数据
	 */
	private void initDate() {
		try {
			Constant.fileSavePath = new File("").getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
			Constant.fileSavePath = "C:\\yuanda";
		}
	}
	/**
	 * 开始约车
	 */
	private void startTraning(String username,String password,Style subjectTow,String traningTime,int timer) {
		new Thread(new Training(username,password,subjectTow,traningTime,timer)).start();
		//new Thread(new DirectTraining(username,password)).start();
	}
}
