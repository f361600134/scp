package com.anjiu.qlbs.backup;

import java.io.File;

import com.anjiu.qlbs.util.ScpLog;

public class ScpInfo {

	// 登录信息
	private String ip;
	private int port = 22;
	private String username;
	private String password;

	// 游戏服务器信息
	private String gameServerPath;// 游戏服路径配置
	// 根据gameServerPath解析
	private String gameServerDir; // 游戏服所在目录
	private String gameServerName;// 游戏服名称

	// 更新文件信息
	private File file;

	// 热更端口
	private int hotfixPort;

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getGameServerPath() {
		return gameServerPath;
	}

	public void setGameServerPath(String gameServerPath) {
		this.gameServerPath = gameServerPath;
	}

	public String getUsername() {
		return username;
	}

	public String getGameServerDir() {
		return gameServerDir;
	}

	public void setGameServerDir(String gameServerDir) {
		this.gameServerDir = gameServerDir;
	}

	public String getGameServerName() {
		return gameServerName;
	}

	public void setGameServerName(String gameServerName) {
		this.gameServerName = gameServerName;
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

	public int getHotfixPort() {
		return hotfixPort;
	}

	public void setHotfixPort(int hotfixPort) {
		this.hotfixPort = hotfixPort;
	}

	public ScpInfo() {
	}

	// public static ScpInfo create(Servers server, File file) {
	// ScpInfo scpInfo = new ScpInfo();
	// scpInfo.setIp(server.getAddress());
	// String port =
	// GmCommonFactory.getCommonValue(GardeniaConstant.GAME_SERVER_LOGIN.PORT);
	// if (!StringUtils.isBlank(port)) {
	// scpInfo.setPort(Integer.valueOf(port));
	// }
	// scpInfo.setUsername(GmCommonFactory.getCommonValue(GardeniaConstant.GAME_SERVER_LOGIN.KEY));
	// scpInfo.setPassword(GmCommonFactory.getCommonValue(GardeniaConstant.GAME_SERVER_LOGIN.PWD));
	// scpInfo.setGameServerPath(StringUtil.checkPath(server.getGameServerPath()));
	// scpInfo.setFile(file);
	// scpInfo.setHotfixPort(server.getHotfixPort());
	// scpInfo.init(scpInfo.getGameServerPath());
	// return scpInfo;
	// }

	// public static ScpInfo create(Servers server) {
	// return create(server, null);
	// }

	public void init(String gameServerPath) {
		if (gameServerPath == null || gameServerPath.isEmpty()) {
			return;
		}
		try {
			int endIndex = gameServerPath.lastIndexOf("/");
			int beginIndex = gameServerPath.lastIndexOf("/", endIndex - 1);
			// 解析地址服务器所在路径,和名字
			setGameServerDir(gameServerPath.substring(0, beginIndex));
			// 截取的时候不要/,从/后开始截取
			setGameServerName(gameServerPath.substring(beginIndex + 1, endIndex));
		} catch (Exception e) {
			ScpLog.error("不合法的路径, gameServerPath:{}", gameServerPath);
		}

	}

	@Override
	public String toString() {
		return "ScpInfo [ip=" + ip + ", port:" + port + ", username=" + username + ", password:" + password + ", gamseServerPath:" + gameServerPath + "]";
	}

	// public static void main(String[] args) {
	// String path = "/home/Jeremy/gameserver/morningGlory_s2/";
	// // 解析名字
	// int endIndex = path.lastIndexOf("/");
	// int beginIndex = path.lastIndexOf("/", endIndex - 1);
	//
	// System.out.println("beginIndex" + beginIndex + ", endIndex" + endIndex);
	// // 解析地址服务器所在路径,和名字
	// String newPath = path.substring(0, beginIndex);
	// String fileName = path.substring(beginIndex + 1, endIndex);
	// System.out.println("newPath:" + newPath + ", fileName:" + fileName);
	// // 组装命令
	// String command = Command.cd(newPath);
	// command += Command.zip(fileName);
	// // 运行
	// System.out.println(command);
	// }

}
