package com.anjiu.qlbs;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;

import com.anjiu.qlbs.base.ScpInfo;
import com.anjiu.qlbs.base.ServerInfo;
import com.anjiu.qlbs.util.ScpLog;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

public class ScpConstant {
	
	private static String XMLPATH = "res/auth.xml";
	public static String commondSource = "upload";
	public static String commonRemoteDir = "";
	public static File uploadFile;
//	public static List<ScpInfo> scpInfos;
	/**
	 * key: ip
	 * value: ScpInfo
	 */
	public static Multimap<String, ScpInfo> scpInfoMap;
	
	/**
	 * 如果是默认路径, 寻找到路径下最新的文件
	 * 如果是指定路径,直接获取到该文件上传
	 */
	public static void loadUpload(){
		File file = new File(commondSource);
		if(file.isFile()) {
			uploadFile = file;
			return;
		};
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				ScpLog.info("f.name:{}", f.getName());
				//System.out.println("筛选出指定文件");
				//uploadFile=xx;
			}
		}
		if(uploadFile==null){
			ScpLog.error("uploadFile is null, exit the system.");
			System.exit(0);
		}
	}
	
	public static void main(String[] args) {
		loadFile();
		loadUpload();
	}
	
	public static void loadFile(){
		File f = new File(XMLPATH);  
        org.dom4j.io.SAXReader reader = new org.dom4j.io.SAXReader();  
        org.dom4j.Document doc;
		try {
			doc = reader.read(f);
			org.dom4j.Element root = doc.getRootElement();  
	        org.dom4j.Element foo;
	        //解析common
	        for (Iterator<?> i = root.elementIterator("common"); i.hasNext();) {
	             foo = (org.dom4j.Element) i.next();
	             String source = foo.elementText("source");
	             if (source != null && !source.isEmpty())
	            	 commondSource = source;
	             
	             //校验目标上产路径
	             commonRemoteDir = foo.elementText("commonRemoteDir");
	             if (commonRemoteDir == null || commonRemoteDir.isEmpty())
	             {
	            	 ScpLog.error("加载配置文件出错, targetBasePath:{}", commonRemoteDir);
	            	 System.exit(0);
	             }
			}
	        
	        //解析server
	        ScpInfo scpInfo = null;
	        ServerInfo serverInfo = null;
	        org.dom4j.Element ele = null;
	        List<ServerInfo> serverInfos = null;
	        //List<ScpInfo> tempScpInfos = new ArrayList<>();
	        Multimap<String, ScpInfo> tempmultimap = ArrayListMultimap.create();
	        
	        for (Iterator<?> i = root.elementIterator("scp"); i.hasNext();) {
	        	foo = (org.dom4j.Element) i.next(); 
	        	String serverName = foo.elementText("serverName");
	        	String ip = foo.elementText("ip");
				int port = Integer.parseInt(foo.elementText("port"));
				String username = foo.elementText("username");
				String password = foo.elementText("password");
				String remoteDir = foo.elementText("remoteDir");
				scpInfo = new ScpInfo(ip, port, username, password, remoteDir);
				//解析服务器组
				List<?> list = foo.element("servers").elements();
				serverInfos = new ArrayList<>();
				for (Object obj : list) {
					ele = (org.dom4j.Element) obj;
					String server = ele.getStringValue();
					String doShutdown = ele.attributeValue("doShutdown");
					String doBackup = ele.attributeValue("doBackup");
					String doStart = ele.attributeValue("doStart");
					serverInfo = new ServerInfo(server);
					if (doShutdown != null && doShutdown.equals("false"))
						serverInfo.setDoShutdown(false);
					if (doShutdown != null && doBackup.equals("false"))
						serverInfo.setDoBackUp(false);
					if (doShutdown != null && doStart.equals("false"))
						serverInfo.setDoStartUp(false);
					serverInfos.add(serverInfo);
				}
				scpInfo.setServerInfos(serverInfos);
				tempmultimap.put(serverName,scpInfo);
			}
	        scpInfoMap = tempmultimap;
	        
	        ScpLog.info("===============配置信息=================");
	        ScpLog.info("commondSource:{}",commonRemoteDir);
	        ScpLog.info("commondSource:{}", commondSource);
			ScpLog.info("scpInfoMap:{}", scpInfoMap);
			 ScpLog.info("===============配置信息=================");
		} catch (DocumentException e) {
			e.printStackTrace();
		}  
		
	}
	
}
