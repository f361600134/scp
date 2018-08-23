package com.anjiu.qlbs.base;

public class ServerInfo {
	//服务器名
	private String serverName;
	//服务器路径
	private String serverPath;
	//服务器所在目录
	private String serverDir;
	private boolean doBackUp;
	private boolean doShutdown;
	private boolean doStartUp;
	
	public String getServerPath() {
		return serverPath;
	}
	public void setServerPath(String serverPath) {
		this.serverPath = serverPath;
	}
	public boolean isDoBackUp() {
		return doBackUp;
	}
	public void setDoBackUp(boolean doBackUp) {
		this.doBackUp = doBackUp;
	}
	public boolean isDoShutdown() {
		return doShutdown;
	}
	public void setDoShutdown(boolean doShutdown) {
		this.doShutdown = doShutdown;
	}
	public boolean isDoStartUp() {
		return doStartUp;
	}
	public void setDoStartUp(boolean doStartUp) {
		this.doStartUp = doStartUp;
	}
	
	public String getServerName(){
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	
	public ServerInfo(String serverPath) {
		this.serverPath = serverPath;
		this.doBackUp = true;
		this.doShutdown = true;
		this.doStartUp = true;
		init(serverPath);
	}
	
	public void init(String serverPath){
		if (serverPath == null || serverPath.isEmpty()) {
			return;
		}
		int endIndex = serverPath.lastIndexOf("/");
		int beginIndex = serverPath.lastIndexOf("/", endIndex-1);
		//解析地址服务器所在路径,和名字
		setServerDir(serverPath.substring(0, beginIndex));
		//截取的时候不要/,从/后开始截取
		setServerName(serverPath.substring(beginIndex+1, endIndex));
	}
	
	
	public String getServerDir() {
		return serverDir;
	}
	public void setServerDir(String serverDir) {
		this.serverDir = serverDir;
	}
	@Override
	public String toString() {
		return "ServerInfo [serverPath=" + serverPath + "]";
	}
	
}
