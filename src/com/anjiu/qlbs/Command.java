package com.anjiu.qlbs;

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
	 * 压缩指定文件作为备份 会根据当前日期备份源文件
	 * 
	 * @param command
	 * @return
	 */
	public static String zip(String fileName, String... files) {
		String filePath = "";
		for (String file : files) {
			filePath += file + " ";
		}
		return "zip -r " + fileName + ".zip " + filePath + ";";
	}

	/**
	 * 压缩指定文件作为备份 会根据当前日期备份源文件
	 * 
	 * @param command
	 * @return
	 */
	public static String zip(String fileName) {
		String newName = fileName + "_" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".zip ";
		return "zip -r " + newName + fileName + ";";
	}

	/**
	 * 强制解压到指定目录
	 * 
	 * @param command
	 * @return
	 */
	public static String unzip(String source, String target) {
		StringBuilder sb = new StringBuilder("unzip -o ");
		sb.append(source).append(" -d ").append(target).append(" ;");
		return sb.toString();
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
	 * 压缩指定文件作为备份 会根据当前日期备份源文件
	 * 
	 * @param command
	 * @return
	 */
	public static String showState() {
		return "sh serverState.sh;";
	}

}
