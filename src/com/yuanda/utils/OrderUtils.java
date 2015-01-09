package com.yuanda.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yuanda.Constant;
import com.yuanda.bean.CarsInfo;
import com.yuanda.bean.OrderInfo;

public class OrderUtils {

	/**
	 * 自动分析所有约车数据
	 * @param string
	 * @return
	 */
	public static List<OrderInfo> analysisForString(String string){
		List<OrderInfo> orderInfos = new ArrayList<OrderInfo>();
		string = analysisTable(string);
		String[] yyrp = string.split("yyrq='");
		for (String orders : yyrp) {
			OrderInfo orderInfo = new OrderInfo();
			if(orders.contains("有")){
				//匹配日期
				Pattern  datePattern = Pattern.compile("2015(\\d*)");
				Matcher matcher = datePattern.matcher(orders);
				if(matcher.find()){
					orderInfo.setAppointDate(matcher.group());
					//System.out.print("选择日期:"+matcher.group());
				}
				//匹配预约时段
				datePattern = Pattern.compile("yysd='(\\d+)'");
				matcher = datePattern.matcher(orders);
				if(matcher.find()){
					//匹配预约时段数字
					datePattern = Pattern.compile("(\\d+)");
					matcher = datePattern.matcher(matcher.group());
					if(matcher.find()){
						orderInfo.setAppingPart(matcher.group());
						//System.out.print("    可预约时段:"+matcher.group());
					}
				}
				//匹配id
				datePattern = Pattern.compile("id='(\\w+\\d+)'");
				matcher = datePattern.matcher(orders);
				if(matcher.find()){
					//匹配id
					datePattern = Pattern.compile("(\\w+\\d+)");
					matcher = datePattern.matcher(matcher.group());
					if(matcher.find()){
						orderInfo.setId(matcher.group());
						//System.out.println("    对应id:"+matcher.group());
					}
				}
				orderInfos.add(orderInfo);
			}
		}
		return orderInfos;
	}
	
	/**
	 * 分析出table
	 * @param string
	 * @return
	 */
	public static String analysisTable(String string){

		String[] split = string.split("\\n");

		for (String string2 : split) {
			if(string2.contains("预约日期")){
				System.out.println("table:"+string2);//TODO  打印table
				return string2;
			}
		}
		return "";
	}
	
	
	/**
	 * 解析json获取约车的详细信息
	 * @param jsonString
	 * @return
	 */
	public static List<CarsInfo> analysisCarsInfo(String jsonString){
		Gson gson = new Gson();
		List<CarsInfo> options = gson.fromJson(jsonString,new TypeToken<List<CarsInfo>>(){}.getType());
		return options;
	}

	/**
	 * 保存所有的约车信息到本地(json的形式)
	 * @param orderInfos
	 */
	public static void saveAllTraning(List<OrderInfo> orderInfos) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 保存某一天的详细约车信息<br/>
	 * <p2>这样的数据可以在当天内使用，都不用再次重新获取信息了</p2>
	 * @param traningInfo   当前可约车信息(json格式)
	 * @param fileName      保存文件的名字
	 */
	public static void saveSingledayTraningDetailInfo(String traningInfo,String fileName) {
		boolean writeState = FileToolkit.string2File(traningInfo, Constant.fileSavePath+File.separator+fileName);
		System.out.println("文件保存writeSatate:"+writeState);
	} 

	/**
	 * 从本地读取保存某一天的约车详细信息
	 * @param fileName
	 * @param timer 约车时间段
	 * @return
	 */
	public static List<CarsInfo> getCarsInfoFromLocal(String fileName,int timer) {
		List<CarsInfo> beforeCarsInfo,afterCarsInfo;
		String readFileString = FileToolkit.readFile(Constant.fileSavePath+File.separator+fileName);
		System.out.println("读取到的json:"+readFileString);
		beforeCarsInfo = OrderUtils.analysisCarsInfo(readFileString);
		
		afterCarsInfo = SimulationUtils.getCarsInfo(beforeCarsInfo,fileName,timer);

		return afterCarsInfo;
	}
	
	/**
	 * 判断指定文件是否存在
	 * @param fileName
	 * @return
	 */
	public static boolean traningFileIsExists(String fileName){
		return new File(Constant.fileSavePath+File.separator+fileName).exists();
	}
}
