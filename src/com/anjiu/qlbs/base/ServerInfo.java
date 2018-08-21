package com.anjiu.qlbs.base;

public class ServerInfo {
	
	private String serverPath;
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
	
	public ServerInfo(String serverPath) {
		this.serverPath = serverPath;
		this.doBackUp = true;
		this.doShutdown = true;
		this.doStartUp = true;
	}
	
	public ServerInfo(String serverPath, boolean doBackUp, boolean doShutdown, boolean doStartUp) {
		super();
		this.serverPath = serverPath;
		this.doBackUp = doBackUp;
		this.doShutdown = doShutdown;
		this.doStartUp = doStartUp;
	}
	
	@Override
	public String toString() {
		return "ServerInfo [serverPath=" + serverPath + ", doBackUp=" + doBackUp + ", doShutdown=" + doShutdown
				+ ", doStartUp=" + doStartUp + "]";
	}

}
