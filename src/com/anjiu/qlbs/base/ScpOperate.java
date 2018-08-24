package com.anjiu.qlbs.base;

import com.anjiu.qlbs.Command;
import com.anjiu.qlbs.ScpAssist;
import com.anjiu.qlbs.ScpConstant;
import com.anjiu.qlbs.util.ScpLog;

import ch.ethz.ssh2.Connection;

public enum ScpOperate {

	/** 关服 */
	SHUTDOWN(1, "关服") {
		@Override
		public void exeCommand(ScpInfo scpInfo) {
			Connection conn = ScpAssist.getConn(scpInfo);
			for (ServerInfo serverInfo : scpInfo.getServerInfos()) {
				String result = ScpAssist.runCommand(conn, Command.shutdown(serverInfo.getServerPath()));
				ScpLog.info("关服结果, result:{}", serverInfo, result);
			}
			conn.close();
			super.print();
		}
	},

	/** 上传文件 */
	UPLOAD(2, "上传文件") {
		@Override
		public void exeCommand(ScpInfo scpInfo) {
			Connection conn = ScpAssist.getConn(scpInfo);
			String target = scpInfo.getRemoteDir();
			if (target == null || target.isEmpty()) {
				target = ScpConstant.commonRemoteDir;
			}
			String source = ScpConstant.uploadFile.getAbsolutePath();
			ScpAssist.putFile(conn, source, target);
			conn.close();
			super.print();
		}
	},

	/** 更新 */
	UPDATE(3, "更新覆盖源文件") {
		@Override
		public void exeCommand(ScpInfo scpInfo) {
			Connection conn = ScpAssist.getConn(scpInfo);
			String path = ScpConstant.commonRemoteDir;
			if (scpInfo.getRemoteDir() != null && !scpInfo.getRemoteDir().isEmpty()) {
				path = scpInfo.getRemoteDir();
			}
			String fileName = ScpConstant.uploadFile.getName();
			for (ServerInfo serverInfo : scpInfo.getServerInfos()) {
				ScpAssist.runCommand(conn, Command.unzip(path + fileName, serverInfo.getServerPath()));
			}
			conn.close();
			super.print();
		}
	},

	/** 启动 */
	START(4, "启动") {
		@Override
		public void exeCommand(ScpInfo scpInfo) {
			long start = System.currentTimeMillis();
			Connection conn = ScpAssist.getConn(scpInfo);
			for (ServerInfo serverInfo : scpInfo.getServerInfos()) {
				start = System.currentTimeMillis();
				String command = Command.startup(serverInfo.getServerPath());
				String result = ScpAssist.runCommand(conn, command);
				if ((System.currentTimeMillis() - start) > 30 * 1000) {
					ScpLog.error("执行超时, 服务器启动有误, 请进入相应的服务器查看, result:" + result);
				} else {
					ScpLog.info("启动成功, server:{}, result:{}", serverInfo, result);
				}
			}
			conn.close();
		}
	},

	/** 上传更新重启 */
	NORMAL(5, "上传更新重启") {
		@Override
		public void exeCommand(ScpInfo scpInfo) {
			ScpOperate.UPLOAD.exeCommand(scpInfo);
			ScpOperate.UPDATE.exeCommand(scpInfo);
			ScpOperate.START.exeCommand(scpInfo);
			super.print();
		}
	},

	/** 下载日志文件 */
	DOWNLOAD(6, "下载日志文件") {
		@Override
		public void exeCommand(ScpInfo scpInfo) {
			Connection conn = ScpAssist.getConn(scpInfo);
			// 组装命令cd
			String downloadPath = scpInfo.getRemoteDir();
			if (downloadPath == null || downloadPath.isEmpty()) {
				downloadPath = ScpConstant.commonRemoteDir;
			}
			String command = Command.cd(downloadPath);
			// 组装命令zip
			String source = "";
			for (ServerInfo serverInfo : scpInfo.getServerInfos()) {
				// 组装下载log日志
				source += "../" + serverInfo.getServerName() + "/start.log ";
			}
			command += Command.zip("log", source);
			// 运行压缩日志文件
			ScpAssist.runCommand(conn, command);
			// 运行下载日志
			ScpAssist.getFile(conn, downloadPath + "/log.zip", ScpConstant.downloadDir);
		}
	},
	/** 备份原服务器 */
	BACKUP(7, "备份原服务器") {
		@Override
		public void exeCommand(ScpInfo scpInfo) {
			Connection conn = ScpAssist.getConn(scpInfo);
			String command = null;
			for (ServerInfo serverInfo : scpInfo.getServerInfos()) {
				// 组装命令
				command = Command.cd(serverInfo.getServerDir());
				command += Command.zip(serverInfo.getServerName());
				// 运行
				ScpAssist.runCommand(conn, command);
			}
		}
	},
	SHOWLOG(8, "查看异常信息") {
		@Override
		public void exeCommand(ScpInfo scpInfo) {
			Connection conn = ScpAssist.getConn(scpInfo);
			String path = null, command = null, newPath = null, fileName = null;

			for (ServerInfo serverInfo : scpInfo.getServerInfos()) {
				// 解析名字
				path = serverInfo.getServerPath();
				int endIndex = path.lastIndexOf("/");
				int beginIndex = path.lastIndexOf("/", endIndex - 1);

				// 解析地址服务器所在路径,和名字
				newPath = path.substring(0, beginIndex);
				// 截取的时候不要/,从/后开始截取
				fileName = path.substring(beginIndex + 1, endIndex);
				// 组装命令
				command = Command.cd(newPath);
				command += Command.zip(fileName);
				// 运行
				ScpAssist.runCommand(conn, command);
			}
		}
	},
	SHOWSTATE(9, "查看服务器状态") {
		@Override
		public void exeCommand(ScpInfo scpInfo) {
			Connection conn = ScpAssist.getConn(scpInfo);
			String command = null;
			for (ServerInfo serverInfo : scpInfo.getServerInfos()) {
				// 组装命令
				command = Command.cd(serverInfo.getServerPath());
				command += Command.showState();
				// 运行
				String result = ScpAssist.runCommand(conn, command);
				System.out.println(serverInfo + " 状态:" + result);
			}
		}
	};

	private int type;
	private String name;

	ScpOperate(int type, String name) {
		this.type = type;
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public static ScpOperate getScpType(int type) {
		for (ScpOperate scpType : values()) {
			if (scpType.type == type) {
				return scpType;
			}
		}
		return null;
	}

	public void print() {
		System.out.println(getName() + "成功");
	}

	public void exeCommand(ScpInfo scpInfo) {
	}

}
