package com.anjiu.qlbs;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Command {
	
	/**
	 * 进入路径
	 * @param path
	 * @return
	 */
	public static String cd(String path){
		return "cd " + path + "\n";
	}
	
	/**
	 * 拷贝文件
	 * @param path
	 * @return
	 */
	public static String cp(String path){
		return "cp " + path + "\n";
	}
	
	/**
	 * 压缩指定文件作为备份
	 * @param command
	 * @return
	 */
	public static String zip(){
		return "zip -r backup.zip classes lib \n";
	}
	
	/**
	 * 压缩指定文件作为备份
	 * @param command
	 * @return
	 */
	public static String zipp(String fileName){
		String newName = fileName +"_"+ new SimpleDateFormat("yyyyMMdd").format(new Date());
		return "zip -r ../"+newName+".zip ../"+fileName;
	}
	
	/**
	 * 压缩指定文件作为备份
	 * @param command
	 * @return
	 */
	public static String zip(String path){
		return "zip -r "+path+"/backup.zip "+path+"/classes "+path+"/lib \n";
	}
	
	/**
	 * 强制解压到指定目录
	 * @param command
	 * @return
	 */
	public static String unzip(String source, String target){
		StringBuilder sb = new StringBuilder("unzip -o ");
		sb.append(source).append(" -d ").append(target).append(" \n");
		return sb.toString();
	}
	
	/**
	 * 启动游戏服务器
	 * @param command
	 * @return
	 */
	public static String start(){
		return "sh runner.sh \n";
	}
	
	/**
	 * 启动游戏服务器
	 * @param command
	 * @return
	 */
	public static String start(String path){
		return "sh "+path+"runner.sh \n";
	}
	
	/**
	 * 关闭游戏服务器
	 * @param command
	 * @return
	 */
	public static String stop(String path){
		return "sh "+path+"stop.sh y\n";
	}
	
	

}
