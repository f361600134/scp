package com.anjiu.qlbs;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.DocumentException;
import com.anjiu.qlbs.base.ScpInfo;
import com.anjiu.qlbs.base.ServerInfo;
import com.anjiu.qlbs.util.ScpLog;

public class ScpConstant {
	
	public static class Filter implements FileFilter{
		@Override
		public boolean accept(File file) {
			return false;
		}
		
	}
	
	private static String XMLPATH = "res/auth.xml";
	private static String commondSource = "upload";
	private static String commonRemoteDir = "";
	private static List<ScpInfo> scpInfos;
	
	/**
	 * 如果是默认路径, 寻找到路径下最新的文件
	 * 如果是指定路径,直接获取到该文件上传
	 */
	public static void loadUpload(){
		File file = new File(commondSource);
		for (File f : file.listFiles()) {
			
		}
	}
	
	public static void main(String[] args) {
//		loadFile();
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
	        List<ScpInfo> tempScpInfos = new ArrayList<>();
	        
	        for (Iterator<?> i = root.elementIterator("scp"); i.hasNext();) {
	        	foo = (org.dom4j.Element) i.next(); 
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
				tempScpInfos.add(scpInfo);
			}
	        scpInfos = tempScpInfos;
	        
	        ScpLog.info("===============配置信息=================");
	        ScpLog.info("commondSource:{}",commonRemoteDir);
	        ScpLog.info("commondSource:{}", commondSource);
			ScpLog.info("scpInfos:{}", scpInfos);
			 ScpLog.info("===============配置信息=================");
		} catch (DocumentException e) {
			e.printStackTrace();
		}  
		
	}
	
}
