package com.anjiu.qlbs.base;

import java.util.ArrayList;
import java.util.List;

public class ScpInfo {
	
	private String serverId;
	private String ip;
    private int port;
    private String username;
	private String password;
	
	//可选
	private String remoteDir;
	//游戏服路径配置
	private List<ServerInfo> serverInfos;
	
	public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
    
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getRemoteDir() {
		return remoteDir;
	}

	public void setRemoteDir(String remoteDir) {
		this.remoteDir = remoteDir;
	}
	
	public ScpInfo(){
		serverInfos = new ArrayList<>();
	}
	
	public ScpInfo(String serverId, String IP, int port, String username, String passward) {
	    this.serverId = serverId;
		this.ip = IP;
	    this.port = port;
	    this.username = username;
	    this.password = passward;
	}
    
    public ScpInfo(String IP, int port, String username, String passward) {
        this.ip = IP;
        this.port = port;
        this.username = username;
        this.password = passward;
    }
    
    public ScpInfo(String ip, int port, String username, String password, String remoteDir) {
		this.ip = ip;
		this.port = port;
		this.username = username;
		this.password = password;
		this.remoteDir = remoteDir;
	}
    
    public List<ServerInfo> getServerInfos() {
		return serverInfos;
	}

	public void setServerInfos(List<ServerInfo> serverInfos) {
		this.serverInfos = serverInfos;
	}
    
	@Override
	public String toString() {
		return "ScpInfo [ip=" + ip + ", username=" + username + ", serverInfos:"+serverInfos+"]";
	}
	
}
