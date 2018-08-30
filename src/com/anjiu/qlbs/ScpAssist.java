package com.anjiu.qlbs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import com.anjiu.qlbs.base.ScpInfo;
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
	 */
	public static Connection getConn(ScpInfo scpInfo) {
		Connection conn = new Connection(scpInfo.getIp(), scpInfo.getPort());
		try {
			// conn.connect();
			conn.connect(null, 1000, 1000);
			boolean isAuthenticated = conn.authenticateWithPassword(scpInfo.getUsername(), scpInfo.getPassword());
			if (isAuthenticated == false) {
				ScpLog.error("authentication failed, scpInfo:{}", scpInfo);
				return null;
			}
		} catch (Exception e) {
			ScpLog.error("Get connection from ssh has an exception,{}", e);
		}
		return conn;
	}

	/**
	 * 改成openxshell
	 * 
	 * @param conn
	 * @param command
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
			ScpLog.info("command:{},result:,{}", command, sess.getExitStatus());
			br.close();
			sess.close();
		} catch (

		Exception e) {
			ScpLog.error("Excetion during run command,{}", e);
			conn.close();
		}
		return sb.toString();
	}

	public static void runCommand2(final Connection conn, final String... command) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Session session;
				try {
					session = conn.openSession();
					session.requestDumbPTY(); // 建立虚拟终端
					session.startShell(); // 打开一个shell
					OutputStream fout = session.getStdin(); // 也可以用PrintWriter
															// out = new
															// PrintWriter(session.getStdin());
					// fout.write(("mkdir huzi" + "\n").getBytes());
					// fout.write(("ls" + "\n").getBytes());
					for (String cmd : command) {
						fout.write((cmd).getBytes());
						System.out.println(cmd);
						try {
							TimeUnit.SECONDS.sleep(1);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					InputStream is = new StreamGobbler(session.getStdout());
					BufferedReader br = new BufferedReader(new InputStreamReader(is));
					while (true) {
						try {
							String line = br.readLine();
							if (line == null)
								break;
							System.out.println(line);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					is.close();
					br.close();
					session.close();
					conn.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}).start();
	}

	public static void runCommand3(final Connection conn, final String... command) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Session session;
				try {
					session = conn.openSession();
					session.requestDumbPTY(); // 建立虚拟终端
					session.startShell(); // 打开一个shell
					OutputStream fout = session.getStdin(); // 也可以用PrintWriter
															// out = new
															// PrintWriter(session.getStdin());
					for (String cmd : command) {
						fout.write((cmd).getBytes());
						System.out.println(cmd);
						try {
							TimeUnit.SECONDS.sleep(1);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					InputStream is = new StreamGobbler(session.getStdout());
					BufferedReader br = new BufferedReader(new InputStreamReader(is));
					while (true) {
						try {
							String line = br.readLine();
							if (line == null)
								break;
							System.out.println(line);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					try {
						TimeUnit.SECONDS.sleep(5);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					is.close();
					br.close();
					session.close();
					conn.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}).start();
	}

	public static void putFile(final Connection conn, String source, String target) {
		try {
			SCPClient client = new SCPClient(conn);
			client.put(source, target);
			ScpLog.info("源文件source:{}, 目标文件target{}", source, target);
		} catch (IOException ex) {
			ex.printStackTrace();
			conn.close();
		}
	}

	/**
	 * 获取文件 @author Jeremy @version V1.0 @param @param conn @param @param
	 * remote @param @param localDir @throws
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

	/**
	 * 获取文件 @author Jeremy @version V1.0 @param @param conn @param @param
	 * remote @param @param localDir @throws
	 */
	public static void getFilePlus(Connection conn, String remote, String localDir) {
		try {
			SCPClient client = new SCPClient(conn);
			client.get(remote, localDir);
		} catch (IOException ex) {
			ex.printStackTrace();
			conn.close();
		}
	}
}
