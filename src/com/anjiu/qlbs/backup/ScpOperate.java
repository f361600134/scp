package com.anjiu.qlbs.backup;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import com.anjiu.qlbs.util.ScpLog;

import ch.ethz.ssh2.Connection;

public enum ScpOperate {

	/** 关服 */
	SHUTDOWN(1, "关服") {
		@Override
		public String exeCommand(ScpInfo scpInfo) {
			Connection conn = null;
			String result = "";
			try {
				conn = ScpAssist.getConn(scpInfo);
				if (conn == null) {
					return failed();
				}
				result = ScpAssist.runCommand(conn, Command.shutdown(scpInfo.getGameServerPath()));
				ScpLog.info("关服结果, serverInfo: {}, result:{}", scpInfo.getGameServerPath(), result);
			} catch (IOException e) {
				ScpLog.error("There was a problem while connecting to:" + scpInfo.getIp() + ":" + scpInfo.getPort());
				return failed();
			} finally {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			}
			return result;
		}

	},

	/** 上传文件 */
	SYNFILE(2, "同步更新文件") {
		@Override
		public String exeCommand(ScpInfo scpInfo) {
			Connection conn = null;
			String result = "";
			String remoteFileName = "server.zip";
			try {
				conn = ScpAssist.getConn(scpInfo);
				if (conn == null) {
					return failed();
				}
				// 源文件路径
				String source = scpInfo.getFile().getAbsolutePath();
				String target = scpInfo.getGameServerPath();
				// 获取上传文件源, 改名
				ScpAssist.putFile(conn, source, target, remoteFileName);
				// 上传完成后, 检索一下有没有此文件
				String command = Command.cd(scpInfo.getGameServerPath());
				command += Command.find(remoteFileName);
				result = ScpAssist.runCommand(conn, command).trim();
				ScpLog.info(getName() + " ScpInfo:" + scpInfo.getGameServerPath() + ", result:" + result);
			} catch (IOException e) {
				ScpLog.error("There was a problem while connecting to:" + scpInfo.getIp() + ":" + scpInfo.getPort());
				return failed();
			} finally {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			}
			return StringUtils.equals(remoteFileName, result) ? success() : failed();
		}

	},

	/** 更新 */
	UPDATE(3, "更新覆盖源文件") {

		@Override
		public String exeCommand(ScpInfo scpInfo) {
			String result = "";
			File file = scpInfo.getFile();
			if (file == null) {
				ScpLog.error("更新覆盖文件失败, 文件信息为空");
				return failed();
			}
			String fileName = file.getName();
			Connection conn = null;
			try {
				conn = ScpAssist.getConn(scpInfo);
				if (conn == null) {
					return failed();
				}
				String command = Command.cd(scpInfo.getGameServerPath());
				command += Command.unzip(fileName);
				result = ScpAssist.runCommand(conn, command).trim();
				ScpLog.info(getName() + " ScpInfo:" + scpInfo.getGameServerPath() + ", result:" + result);
			} catch (IOException e) {
				ScpLog.error("There was a problem while connecting to:" + scpInfo.getIp() + ":" + scpInfo.getPort());
				return failed();
			} finally {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			}
			return StringUtils.isBlank(result) ? success() : failed();
		}

	},

	/**
	 * 启动
	 * 
	 * @warning 启动每次需要等待完成, 比较耗时. 可以改成异步操作.
	 */
	START(4, "启动") {
		@Override
		public String exeCommand(ScpInfo scpInfo) {
			String result = "";
			Connection conn = null;
			try {
				conn = ScpAssist.getConn(scpInfo);
				if (conn == null) {
					return failed();
				}
				long start = System.currentTimeMillis();
				String command = Command.startup(scpInfo.getGameServerPath());
				result = ScpAssist.runCommand(conn, command);
				ScpLog.info(getName() + " ScpInfo:" + scpInfo.getGameServerPath() + ", result:" + result);
				if ((System.currentTimeMillis() - start) > 30 * 1000) {
					ScpLog.error("执行超时, 请进入相应的服务器查看, result:" + result);
				} else {
					ScpLog.info("启动成功, server:" + scpInfo.getGameServerPath() + ", result:" + result);
				}
			} catch (IOException e) {
				ScpLog.error("There was a problem while connecting to:" + scpInfo.getIp() + ":" + scpInfo.getPort());
				return failed();
			} finally {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			}
			return result;
		}
	},

	/** 上传更新重启 */
	NORMAL(5, "上传更新重启") {

		@Override
		public String exeCommand(ScpInfo scpInfo) {
			ScpOperate.SYNFILE.exeCommand(scpInfo);
			ScpOperate.UPDATE.exeCommand(scpInfo);
			ScpOperate.START.exeCommand(scpInfo);
			return "";
		}

	},

	/** 备份原服务器 */
	BACKUP(6, "备份原服务器") {

		@Override
		public String exeCommand(ScpInfo scpInfo) {
			String result = "";
			Connection conn = null;
			try {
				conn = ScpAssist.getConn(scpInfo);
				if (conn == null) {
					return failed();
				}
				// 组装命令
				String command = Command.cd(scpInfo.getGameServerDir());
				command += Command.zip(scpInfo.getGameServerName());
				// 运行
				result = ScpAssist.runCommand(conn, command).trim();
				ScpLog.info(getName() + " ScpInfo:" + scpInfo.getGameServerPath() + ", result:" + result);
			} catch (IOException e) {
				ScpLog.error("There was a problem while connecting to:" + scpInfo.getIp() + ":" + scpInfo.getPort());
				return result;
			} finally {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			}
			// 使用zip -q -r 不显示任何信息, 所以当result为空的时候, 表示成功
			return StringUtils.isBlank(result) ? success() : failed();
		}

	},
	SHOWSTATE(7, "查看服务器状态") {
		@Override
		public String exeCommand(ScpInfo scpInfo) {
			String result = "-1";
			Connection conn = null;
			try {
				String gameServerpath = scpInfo.getGameServerPath();
				if (StringUtils.isBlank(gameServerpath)) {
					return result;
				}
				conn = ScpAssist.getConn(scpInfo);
				if (conn == null) {
					return result;
				}
				// 组装命令
				String command = Command.cd(scpInfo.getGameServerPath());
				command += Command.showState();
				// 运行
				result = ScpAssist.runCommand(conn, command);
				conn.close();
				ScpLog.info(getName() + " ScpInfo:" + scpInfo.getGameServerPath() + ", result:" + result);
			} catch (IOException e) {
				ScpLog.error("There was a problem while connecting to:" + scpInfo.getIp() + ":" + scpInfo.getPort());
				return failed();
			} finally {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			}
			// -99 为关闭, 其他为开启
			return result.trim();
		}
	},
	SHUTDOWN_Q(8, "强制关服") {
		@Override
		public String exeCommand(ScpInfo scpInfo) {
			String result = "";
			Connection conn = null;
			try {
				conn = ScpAssist.getConn(scpInfo);
				if (conn == null) {
					return "";
				}
				result = ScpAssist.runCommand(conn, Command.shutdownQ(scpInfo.getGameServerPath()));
				ScpLog.info("关服结果, serverInfo:" + scpInfo.getGameServerPath() + ", result:" + result);
				conn.close();
			} catch (IOException e) {
				ScpLog.error("There was a problem while connecting to:" + scpInfo.getIp() + ":" + scpInfo.getPort());
				return failed();
			} finally {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			}
			return result;
		}

	},
	SHUTDOWN_K(9, "通知进程关服") {
		@Override
		public String exeCommand(ScpInfo scpInfo) {
			Connection conn = null;
			String result = "";
			try {
				conn = ScpAssist.getConn(scpInfo);
				if (conn == null) {
					return failed();
				}
				result = ScpAssist.runCommand(conn, Command.shutdownK(scpInfo.getGameServerPath()));
				ScpLog.info("关服结果, serverInfo:" + scpInfo.getGameServerPath() + ", result:" + result);
			} catch (IOException e) {
				ScpLog.error("There was a problem while connecting to:" + scpInfo.getIp() + ":" + scpInfo.getPort());
				return failed();
			} finally {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			}
			return result;
		}

	},
	SHOWSTATEPLUS(10, "查看服务器状态(10)") {
		@Override
		public ScpResult runCommand(ScpInfo scpInfo) {
			ScpResult result = null;
			Connection conn = null;
			try {
				String gameServerpath = scpInfo.getGameServerPath();
				if (StringUtils.isBlank(gameServerpath)) {
					return result;
				}
				conn = ScpAssist.getConn(scpInfo);
				if (conn == null) {
					return result;
				}
				// 组装命令
				String command = Command.cd(scpInfo.getGameServerPath());
				command += Command.showState();
				// 运行
				result = ScpAssist.runCmd(conn, command);
				conn.close();
				ScpLog.info(getName() + " ScpInfo:" + scpInfo.getGameServerPath() + ", result:" + result);
			} catch (IOException e) {
				ScpLog.error("There was a problem while connecting to:" + scpInfo.getIp() + ":" + scpInfo.getPort());
			} finally {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			}
			// -99 为关闭, 其他为开启
			return result;
		}
	},
	SYNHOTFIX(11, "同步解压热更文件") {
		@Override
		public String exeCommand(ScpInfo scpInfo) {
			ScpResult result = runCommand(scpInfo);
			return result.getResultInfo();
		}

		@Override
		public ScpResult runCommand(ScpInfo scpInfo) {
			ScpResult result = null;
			Connection conn = null;
			String hotfixName = "hotfix.zip";
			try {
				String gameServerpath = scpInfo.getGameServerPath();
				if (StringUtils.isBlank(gameServerpath)) {
					return result;
				}
				conn = ScpAssist.getConn(scpInfo);
				if (conn == null) {
					return result;
				}
				// 上传热更文件
				String source = scpInfo.getFile().getAbsolutePath();
				String remoteTarget = gameServerpath + "/hotfix/";
				ScpAssist.putFile(conn, source, remoteTarget, hotfixName);
				// 解压压缩文件
				String command = Command.cd(remoteTarget);
				command += Command.unzip(hotfixName);

				// 组装hotfixurl请求,注意这里要转译,否则linux系统无法识别
				String hotfixurl = "/game/services?action=hotupdate\\&fix=1";
				hotfixurl = "http://" + scpInfo.getIp() + ":" + scpInfo.getHotfixPort() + hotfixurl;
				command += Command.curl(hotfixurl);

				result = ScpAssist.runCmd(conn, command);
				ScpLog.info(getName() + " ScpInfo:" + scpInfo.getGameServerPath() + ", result:" + result);
			} catch (IOException e) {
				ScpLog.error("There was a problem while connecting to:" + scpInfo.getIp() + ":" + scpInfo.getPort());
			} finally {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			}
			return result;
		}
	},

	;

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

	public String success() {
		return getName() + "成功";
	}

	public String failed() {
		return getName() + "失败";
	}

	public String exeCommand(ScpInfo scpInfo) {
		return "None";
	}

	public ScpResult runCommand(ScpInfo scpInfo) {
		return null;
	}

}
