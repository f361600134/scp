package com.anjiu.qlbs.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

/**
 * 模拟交互式终端
 * 
 * @see http://www.programcreek.com/java-api-examples/index.php?api=ch.ethz.ssh2
 *      .SCPClient
 *
 */

public final class SSHAgent2 {

	private Connection connection;
	private Session session;
	private BufferedReader stdout;
	private PrintWriter printWriter;
	private BufferedReader stderr;
	private final ExecutorService service = Executors.newFixedThreadPool(3);
	private final Scanner scanner = new Scanner(System.in);

	public void initSession(String hostName, String userName, String passwd) throws IOException {
		connection = new Connection(hostName);
		connection.connect();
		boolean authenticateWithPassword = connection.authenticateWithPassword(userName, passwd);
		if (!authenticateWithPassword) {
			throw new RuntimeException("Authentication failed. Please check hostName, userName and passwd");
		}
		session = connection.openSession();
		session.requestDumbPTY();
		session.startShell();
		stdout = new BufferedReader(new InputStreamReader(new StreamGobbler(session.getStdout()), StandardCharsets.UTF_8));
		stderr = new BufferedReader(new InputStreamReader(new StreamGobbler(session.getStderr()), StandardCharsets.UTF_8));
		printWriter = new PrintWriter(session.getStdin());
	}

	public void execCommand() throws IOException {
		service.submit(new Runnable() {
			@Override
			public void run() {
				String line;
				try {
					while ((line = stdout.readLine()) != null) {
						System.out.println(line);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		service.submit(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						TimeUnit.SECONDS.sleep(1);
					} catch (InterruptedException e) {

						e.printStackTrace();
					}
					String nextLine = scanner.nextLine();
					printWriter.write(nextLine + "\r\n");
					printWriter.flush();
				}

			}
		});
	}

	public void close() {
		IOUtils.closeQuietly(stdout);
		IOUtils.closeQuietly(stderr);
		IOUtils.closeQuietly(printWriter);
		IOUtils.closeQuietly(scanner);
		session.close();
		connection.close();
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		SSHAgent2 sshAgent = new SSHAgent2();
		String ip = "192.168.6.138";
		// int port = 22;
		String username = "root";
		String password = "123456";
		sshAgent.initSession(ip, username, password);
		sshAgent.execCommand();
		// sshAgent.close();
	}

}
