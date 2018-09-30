package com.anjiu.qlbs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import com.anjiu.qlbs.backup.ScpResult;
import com.anjiu.qlbs.base.ScpInfo;
import com.anjiu.qlbs.util.ScpLog;

import ch.ethz.ssh2.ChannelCondition;
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
			String line = null;
			while (true) {
				line = br.readLine();
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

	public static void main(String args[]) {
		test();
	}

	// 用于测试使用execCommand是否不携带环境
	public static void test() {
		// 组装连接部分
		String ip = "192.168.6.138";
		int port = 22;
		String username = "root";
		String password = "123456";
		ScpInfo scpInfo = new ScpInfo(ip, port, username, password);
		Connection conn = getConn(scpInfo);
		// 组装命令部分
		String command = "sh /data/webapps/ddlhyy_android/update/build.sh > fail.log";
		ScpResult result = runCmd(conn, command);
		System.out.println(result);
	}

	public static void test1() {
		try {
			/* Create a connection instance */
			Connection conn = new Connection("127.0.0.1");
			/* Now connect */
			conn.connect();
			/* Authenticate */
			boolean isAuthenticated = conn.authenticateWithPassword("username", "password");
			if (isAuthenticated == false)
				throw new IOException("Authentication failed. Please check hostname, username and password.");
			/* Create a session */
			Session sess = conn.openSession();
			// sess.execCommand("uname -a && date && uptime && who");   
			System.out.println("start exec command.......");
			// sess.execCommand("echo /"Text on STDOUT/"; echo /"Text on
			// STDERR/" >&2");
			// sess.execCommand("env");           
			sess.requestPTY("bash");
			sess.startShell();
			InputStream stdout = new StreamGobbler(sess.getStdout());
			InputStream stderr = new StreamGobbler(sess.getStderr());
			BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(stdout));
			BufferedReader stderrReader = new BufferedReader(new InputStreamReader(stderr));
			// if you want to use sess.getStdin().write(), here is a sample
			// byte b[]={'e','n','v','/n'};
			// byte b[]={'e','x','i','t','/n'};
			// sess.getStdin().write(b)
			/*
			 *     String str="env";    String str1="exit";   
			 * System.out.println(str+str1);    out.write(str.getBytes());   
			 * out.write('/n');    out.write(str1.getBytes());   
			 * out.write('/n');
			 */
			// we used PrintWriter, it makes things simple   PrintWriter out
			// =new PrintWriter(sess.getStdin());
			// out.println("env");
			// out.println("exit");
			// out.close();
			sess.waitForCondition(ChannelCondition.CLOSED | ChannelCondition.EOF | ChannelCondition.EXIT_STATUS, 30000);
			System.out.println("Here is the output from stdout:");
			while (true) {
				String line = stdoutReader.readLine();
				if (line == null)
					break;
				System.out.println(line);
			}
			System.out.println("Here is the output from stderr:");
			while (true) {
				String line = stderrReader.readLine();
				if (line == null)
					break;
				System.out.println(line);
			}

			/* Show exit status, if available (otherwise "null") */
			System.out.println("ExitCode: " + sess.getExitStatus());
			sess.close();/* Close this session */
			conn.close();/* Close the connection */
		} catch (Exception e) {
			e.printStackTrace(System.err);
			System.exit(2);
		}

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
