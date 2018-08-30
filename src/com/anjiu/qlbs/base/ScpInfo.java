package com.anjiu.qlbs.base;

import java.util.ArrayList;
import java.util.List;

import com.anjiu.qlbs.Command;

public class ScpInfo {

	private String serverName;
	private String serverId;
	private String ip;
	private int port;
	private String username;
	private String password;

	// 远程更新的目录
	private String remoteDir;
	// 游戏服路径配置
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

	public ScpInfo() {
		serverInfos = new ArrayList<>();
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public ScpInfo(String serverId, String serverName, String IP, int port, String username, String passward) {
		this.serverId = serverId;
		this.serverName = serverName;
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

	public ScpInfo(String serverId, String serverName, String IP, int port, String username, String passward, String remoteDir) {
		this.serverId = serverId;
		this.serverName = serverName;
		this.ip = IP;
		this.port = port;
		this.username = username;
		this.password = passward;
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
		return "ScpInfo [ip=" + ip + ", port:" + port + ", username=" + username + ", serverInfos:" + serverInfos + "]";
	}

	public static void main(String[] args) {
		String path = "/home/Jeremy/gameserver/morningGlory_s2/";
		// 解析名字
		int endIndex = path.lastIndexOf("/");
		int beginIndex = path.lastIndexOf("/", endIndex - 1);

		System.out.println("beginIndex" + beginIndex + ", endIndex" + endIndex);
		// 解析地址服务器所在路径,和名字
		String newPath = path.substring(0, beginIndex);
		String fileName = path.substring(beginIndex + 1, endIndex);
		System.out.println("newPath:" + newPath + ", fileName:" + fileName);
		// 组装命令
		String command = Command.cd(newPath);
		command += Command.zip(fileName);
		// 运行
		System.out.println(command);
	}

}
