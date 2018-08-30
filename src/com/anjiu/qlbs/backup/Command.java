package com.anjiu.qlbs.backup;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Command {

	/**
	 * 进入路径
	 * 
	 * @param path
	 * @return
	 */
	public static String cd(String path) {
		return "cd " + path + ";";
	}

	/**
	 * 拷贝文件
	 * 
	 * @param path
	 * @return
	 */
	public static String cp(String path) {
		return "cp " + path + ";";
	}

	/**
	 * 
	 * @param command
	 * @return
	 */
	public static String zip(String fileName, String... files) {
		String filePath = "";
		for (String file : files) {
			filePath += file + " ";
		}
		return "zip -q -r " + fileName + ".zip " + filePath + ";";
	}

	/**
	 * 压缩指定文件作为备份 会根据当前日期备份源文件
	 * 
	 * @param command
	 * @return
	 */
	public static String zip(String fileName) {
		String newName = fileName + "_" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".zip ";
		return "zip -q -r " + newName + fileName + ";";
	}

	/**
	 * 强制解压到指定目录
	 * 
	 * @param command
	 * @return
	 */
	public static String unzip(String source, String target) {
		StringBuilder sb = new StringBuilder("unzip -q -o ");
		sb.append(source).append(" -d ").append(target).append(" ;");
		return sb.toString();
	}

	/**
	 * 强制解压到指定目录, 不显示过程
	 * 
	 * @param command
	 * @return
	 */
	public static String unzip(String source) {
		return "unzip -q -o " + source + ";";
	}

	public static void main(String[] args) {
		// String path = "/home/Jeremy/gameserver/morningGlory_s1/";
		// String npath = path.replaceAll("\\/", "");
		// System.out.println(path.length()-npath.length());
		System.out.println(zip("a", "b", "c"));
	}

	/**
	 * 启动游戏服务器
	 * 
	 * @param command
	 * @return
	 */
	public static String start() {
		return "sh runner.sh ;";
	}

	/**
	 * 启动游戏服务器
	 * 
	 * @param command
	 * @return
	 */
	public static String startup(String path) {
		return "sh " + path + "runner.sh ;";
	}

	/**
	 * 关闭游戏服务器
	 * 
	 * @param command
	 * @return
	 */
	public static String shutdown(String path) {
		return "sh " + path + "stop.sh y ;";
	}

	/**
	 * 关闭游戏服务器
	 * 
	 * @param command
	 * @return
	 */
	public static String shutdownQ(String path) {
		return "sh " + path + "stop.sh q ;";
	}

	/**
	 * 关闭游戏服务器
	 * 
	 * @param command
	 * @return
	 */
	public static String shutdownK(String path) {
		return "sh " + path + "stop.sh k ;";
	}

	/**
	 * 查看进程状态
	 * 
	 * @param command
	 * @return
	 */
	public static String showState() {
		return "sh serverState.sh;";
	}

	/**
	 * 查找文件是否存在
	 * 
	 * @param command
	 * @return
	 */
	public static String find(String name) {
		return "find " + name + ";";
	}

	/**
	 * 执行url
	 * 
	 * @param command
	 * @return
	 */
	public static String curl(String url) {
		return "curl " + url + ";";
	}

}
