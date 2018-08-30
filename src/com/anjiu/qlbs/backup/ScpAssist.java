package com.anjiu.qlbs.backup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.anjiu.qlbs.util.ScpLog;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class ScpAssist {

	/**
	 * 获取连接
	 * 
	 * @param scpInfo
	 * @return
	 * @throws IOException
	 */
	public static Connection getConn(ScpInfo scpInfo) throws IOException {
		Connection conn = new Connection(scpInfo.getIp(), scpInfo.getPort());
		// try {
		// conn.connect();
		conn.connect(null, 1000, 1000);
		boolean isAuthenticated = conn.authenticateWithPassword(scpInfo.getUsername(), scpInfo.getPassword());
		if (isAuthenticated == false) {
			ScpLog.error("authentication failed, scpInfo {}" + scpInfo);
			return null;
		}
		// } catch (Exception e) {
		// logger.error("There was a problem while connecting to" +
		// scpInfo.getIp() + ":" + scpInfo.getPort());
		// return null;
		// }
		return conn;
	}

	/**
	 * 可以改成并发请求, 发出命令后, 返回给前端, 等待处理结果, 最后使用Ajax把每个结果返回给前端.
	 * 
	 * @param conn
	 * @param command
	 * 
	 * @return
	 */
	public static String runCommand(Connection conn, String command) {
		StringBuilder sb = new StringBuilder();
		try {
			Session sess = conn.openSession();
			sess.execCommand(command);
			InputStream stdout = new StreamGobbler(sess.getStdout());
			BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
			while (true) {
				String line = br.readLine();
				if (line == null)
					break;
				sb.append(line).append("\n");
			}
			ScpLog.error("command:{}, result:{}, output:{}", command, sess.getExitStatus(), sb.toString());
			br.close();
			sess.close();
		} catch (Exception e) {
			ScpLog.error("Excetion during run command,{}", e);
			conn.close();
			return sb.toString();
		}
		return sb.toString();
	}

	/**
	 * 可以改成并发请求, 发出命令后, 返回给前端, 等待处理结果, 最后使用Ajax把每个结果返回给前端.
	 * 
	 * @param conn
	 * @param command
	 * 
	 * @return
	 */
	public static ScpResult runCmd(Connection conn, String command) {
		ScpResult result = ScpResult.create();
		try {
			StringBuilder sb = new StringBuilder();
			Session sess = conn.openSession();
			sess.execCommand(command);
			InputStream stdout = new StreamGobbler(sess.getStdout());
			BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
			while (true) {
				String line = br.readLine();
				if (line == null)
					break;
				sb.append(line);
			}
			result.setResultCode(sess.getExitStatus());
			result.setResultInfo(sb.toString());

			ScpLog.info("command:" + command + ", errorCode:" + sess.getExitStatus() + ", output:" + sb.toString());
			br.close();
			sess.close();
		} catch (Exception e) {
			ScpLog.error("Excetion during run command,{}", e);
			conn.close();
			return result;
		}
		return result;
	}

	public static void main(String[] args) {
		String str1 = "aaa\n".trim();
		String str2 = "aaa".trim();
		System.out.println(str1);
		System.out.println(str2);
		System.out.println(str2.equals(str1));
	}

	/**
	 * 上传文件到指定目录
	 * 
	 * @param conn
	 * @param source
	 * @param target
	 * @return void
	 * @date 2018年8月24日下午4:43:19
	 */
	public static void putFile(final Connection conn, String source, String target) {
		try {
			SCPClient client = new SCPClient(conn);
			client.put(source, target);
			ScpLog.info("源文件source:{}, 目标文件夹target:{}", source, target);
		} catch (IOException ex) {
			ex.printStackTrace();
			conn.close();
		}
	}

	/**
	 * 上传文件到指定目录
	 * 
	 * @param conn
	 * @param source
	 * @param target
	 * @return void
	 * @date 2018年8月24日下午4:43:19
	 */
	public static void putFile(final Connection conn, String source, String target, String remoteFileName) {
		try {
			SCPClient client = new SCPClient(conn);
			client.put(source, remoteFileName, target, "0644");
			ScpLog.info("源文件source: {}, 目标文件夹target:{}", source, target);
		} catch (IOException ex) {
			ex.printStackTrace();
			conn.close();
		}
	}

	/**
	 * 获取文件
	 * 
	 * @param conn
	 * @param remote
	 * @param localDir
	 * @return void
	 * @date 2018年8月24日下午4:43:38
	 */
	public static void getFile(Connection conn, String remote, String localDir) {
		try {
			SCPClient client = new SCPClient(conn);
			client.get(remote, localDir);
			ScpLog.info("源文件 remote:{}, 目标文件 localDir:{}", remote, localDir);
		} catch (IOException ex) {
			ex.printStackTrace();
			conn.close();
		}
	}

}
