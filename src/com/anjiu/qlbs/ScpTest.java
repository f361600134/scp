

package com.anjiu.qlbs;

import java.util.Arrays;
import java.util.List;

import com.anjiu.qlbs.base.ScpInfo;

import ch.ethz.ssh2.Connection;

/**
 * 
 * 这里每条线程做相同的事情, 对于外网来说不友好
 * 可以改成更新服务器组:
 *  服务器组, 那么登录验证就只有一条.
 * 	1.上传单文件到服务器
 *  2.停服,如果停服失败? 停服后的日志记录, 需要分析.
 *  2.覆盖文件到不同服务器组
 *  3.启动服务器,返回信息记录
 *  4.复制启动日志到指定目录,压缩,下载
 * @Description 
 * @author Jeremy
 * @date 2018年8月20日 上午12:58:57 
 * @version V1.0
 */
public class ScpTest {
	
	public static void main(String[] args) {
//		testOpen();
//		testStart1();
//		testStart2();
//		testOpen1211();
//		testOpen34810();
//		testOpen5679();
//		testOpenL();
	}
	
	public static void testOpen(){
		ScpInfo scpInfo = new ScpInfo("192.168.6.139", 20089, "root", "wzyd508");
		String commonPath = "/home/Jeremy/gameserver/newer/";
		String fileName = "morningGlory_sample.zip";
		
		List<String> serverPath = Arrays.asList("/home/Jeremy/gameserver/morningGlory_s1", 
				"/home/Jeremy/gameserver/morningGlory_s2");
		
		Connection conn = ScpAssist.getConn(scpInfo);
		ScpAssist.putFile(conn, "E:/a/"+fileName, commonPath);
		/*
		 * -.上传文件 putfile 指定目录dirpath
		 * -.移动到指定目录
		 * -.for 配置的服务器地址path数量
		 * 		2.1.备份原数据压缩成固定格式 zip -r backup.zip classes lib
		 * 		2.2.解压覆盖文件到对应地址 unzip -o source -d target
		 * 		2.3.启动服务器 sh runner.sh
		 * -.下载error.log日志 start日志, 检索异常
		 */
		for (String path : serverPath) {
			//解压
			String command = Command.unzip(commonPath+fileName, path);
			ScpAssist.runCommand(conn, command);
			System.out.println("解压指定目录文件 cp.command:"+command);
		}
		//获取日志文件
	}
	
	public static void testOpen1211(){
		ScpInfo scpInfo = new ScpInfo("106.14.114.232", 22, "root", "@Wzcs!#$*");
		String commonPath = "/data/webapps/ddlhyy_android/newer/";
		String fileName = "server.zip";
		
		List<String> serverPath = Arrays.asList("/data/webapps/ddlhyy_android/morningGlory_s1/",
				"/data/webapps/ddlhyy_android/morningGlory_s2/",
				"/data/webapps/ddlhyy_android/morningGlory_s11/");
		
		Connection conn = ScpAssist.getConn(scpInfo);
//		ScpAssist.putFile(conn, "E:/a/"+fileName, commonPath);
		for (String path : serverPath) {
//			//关服
			String command = Command.stop(path);
//			ScpAssist.runCommand(conn, command);
//			System.out.println("关服command:"+command);
			
			//解压
			command = Command.unzip(commonPath+fileName, path);
//			ScpAssist.runCommand(conn, command);
//			System.out.println("解压指定目录文件 cp.command:"+command);
//			
//			//启动游戏
			command = Command.start(path);
			ScpAssist.runCommand(conn, command);
			System.out.println("解压指定目录文件 cp.command:"+command);
		}
	}
	
	public static void testOpen34810(){
		ScpInfo scpInfo = new ScpInfo("47.100.21.19", 22, "root", "@Wzcs!#$*");
		String commonPath = "/data/webapps/ddlhyy_android/newer/";
		String fileName = "server.zip";
		
		List<String> serverPath = Arrays.asList(
				"/data/webapps/ddlhyy_android/morningGlory_s3/",
				/*"/data/webapps/ddlhyy_android/morningGlory_s4/",*/
				"/data/webapps/ddlhyy_android/morningGlory_s8/",
				"/data/webapps/ddlhyy_android/morningGlory_s10/");
		
		Connection conn = ScpAssist.getConn(scpInfo);
//		ScpAssist.putFile(conn, "E:/a/"+fileName, commonPath);
		for (String path : serverPath) {
			//关服
			String command = Command.stop(path);
//			ScpAssist.runCommand(conn, command);
//			System.out.println("关服command:"+command);
			
			//解压
			command = Command.unzip(commonPath+fileName, path);
//			ScpAssist.runCommand(conn, command);
//			System.out.println("解压指定目录文件 command:"+command);
			
			//启动游戏
			command = Command.start(path);
			ScpAssist.runCommand(conn, command);
			System.out.println("启动 command:"+command);
		}
	}
	
	public static void testOpen5679(){
		ScpInfo scpInfo = new ScpInfo("101.132.47.203", 22, "root", "@Wzcs!#$*");
		String commonPath = "/data/webapps/ddlhyy_android/newer/";
		String fileName = "server.zip";
		
		List<String> serverPath = Arrays.asList(
				"/data/webapps/ddlhyy_android/morningGlory_s5/",
				/*"/data/webapps/ddlhyy_android/morningGlory_s6/",*/
				"/data/webapps/ddlhyy_android/morningGlory_s7/", 
				"/data/webapps/ddlhyy_android/morningGlory_s9/");
		
		Connection conn = ScpAssist.getConn(scpInfo);
//		ScpAssist.putFile(conn, "E:/a/"+fileName, commonPath);
		for (String path : serverPath) {
			//关服
			String command = Command.stop(path);
//			ScpAssist.runCommand(conn, command);
//			System.out.println("关服command:"+command);
			
			//解压
//			command = Command.unzip(commonPath+fileName, path);
//			ScpAssist.runCommand(conn, command);
//			System.out.println("解压指定目录文件 command:"+command);
//			
//			//启动游戏
			command = Command.start(path);
			ScpAssist.runCommand(conn, command);
			System.out.println("启动 command:"+command);
		}
	}
	
	public static void testOpenL(){
		ScpInfo scpInfo = new ScpInfo("47.100.58.171", 22, "root", "@Wzcs!#$*");
		String commonPath = "/data/webapps/ddlhyy_android/newer/";
		String fileName = "server.zip";
		
		List<String> serverPath = Arrays.asList("/data/webapps/ddlhyy_android/morningGlory_s1/",
				"/data/webapps/ddlhyy_android/morningGlory_s2/");
		
		
		Connection conn = ScpAssist.getConn(scpInfo);
		ScpAssist.putFile(conn, "E:/a/"+fileName, commonPath);
		for (String path : serverPath) {
			//关服
			String command = Command.stop(path);
//			ScpAssist.runCommand(conn, command);
//			System.out.println("关服command:"+command);
			
			//解压
//			command = Command.unzip(commonPath+fileName, path);
//			ScpAssist.runCommand(conn, command);
//			System.out.println("解压指定目录文件 command:"+command);
//			
//			//启动游戏
			command = Command.start(path);
			ScpAssist.runCommand(conn, command);
			System.out.println("启动 command:"+command);
		}
	}

	/**
	 * 有问题!!! What the fuck!!
	 */
	public static void testStart(){
		/*
		 * -.上传文件 putfile 指定目录dirpath
		 * -.移动到指定目录
		 * -.for 配置的服务器地址path数量
		 * 		2.1.备份原数据压缩成固定格式 zip -r backup.zip classes lib
		 * 		2.2.解压覆盖文件到对应地址 unzip -o source -d target
		 * 		2.3.启动服务器 sh runner.sh
		 * -.下载error.log日志 start日志, 检索异常
		 */
		ScpInfo scpInfo = new ScpInfo("192.168.6.139", 20089, "root", "wzyd508");
		String commonPath = "/home/Jeremy/gameserver/newer/";
		String fileName = "morningGlory_sample.zip";
		
		List<String> serverPath = Arrays.asList("/home/Jeremy/gameserver/morningGlory_s1", 
				"/home/Jeremy/gameserver/morningGlory_s2");
		
		Connection conn = ScpAssist.getConn(scpInfo);
		ScpAssist.putFile(conn, "E:/a/"+fileName, commonPath);
		for (String path : serverPath) {
			ScpAssist.runCommand2(conn, Command.cd(path),  
					Command.zip(), Command.unzip(commonPath+fileName, path));
//			ScpAssist.getFile(conn, path+"/start.log", "E:/a/log");
		}
	}
	
	public static void testStart1(){
		/*
		 * -.上传文件 putfile 指定目录dirpath
		 * -.移动到指定目录
		 * -.for 配置的服务器地址path数量
		 * 		2.1.备份原数据压缩成固定格式 zip -r backup.zip classes lib
		 * 		2.2.解压覆盖文件到对应地址 unzip -o source -d target
		 * 		2.3.启动服务器 sh runner.sh
		 * -.下载error.log日志 start日志, 检索异常
		 */
		ScpInfo scpInfo = new ScpInfo("106.14.114.232", 22, "root", "@Wzcs!#$*");
		String commonPath = "/data/webapps/ddlhyy_android/newer/";
		String fileName = "server.zip";
		
		//服务器组
		String serverCommonPath = "/data/webapps/ddlhyy_android/";
		List<String> serverNames = Arrays.asList("morningGlory_s1");
		
		Connection conn = ScpAssist.getConn(scpInfo);
		ScpAssist.putFile(conn, "E:/a/"+fileName, commonPath);
		for (String name : serverNames) {
			String path = serverCommonPath + name;
			ScpAssist.runCommand2(conn, Command.cd(path),  
					Command.zipp(name), Command.unzip(commonPath+fileName, path));
//			ScpAssist.getFile(conn, path+"/start.log", "E:/a/log");
		}
	}

	public static void testStart2(){
		/*
		 * -.上传文件 putfile 指定目录dirpath
		 * -.移动到指定目录
		 * -.for 配置的服务器地址path数量
		 * 		2.1.备份原数据压缩成固定格式 zip -r backup.zip classes lib
		 * 		2.2.解压覆盖文件到对应地址 unzip -o source -d target
		 * 		2.3.启动服务器 sh runner.sh
		 * -.下载error.log日志 start日志, 检索异常
		 */
		ScpInfo scpInfo = new ScpInfo("106.14.114.232", 22, "root", "@Wzcs!#$*");
		String commonPath = "/data/webapps/ddlhyy_android/newer/";
		String fileName = "server.zip";
		
		//服务器组
		String serverCommonPath = "/data/webapps/ddlhyy_android/";
		List<String> serverNames = Arrays.asList("morningGlory_s1/");
		
		Connection conn = ScpAssist.getConn(scpInfo);
//		ScpAssist.putFile(conn, "E:/a/"+fileName, commonPath);
		for (String name : serverNames) {
			String path = serverCommonPath + name;
			ScpAssist.runCommand2(conn, Command.cd(path),  
					Command.zipp(name), Command.unzip(commonPath+fileName, path));
//			ScpAssist.getFile(conn, path+"/start.log", "E:/a/log");
		}
	}

}
