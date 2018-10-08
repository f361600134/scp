package com.anjiu.qlbs.util.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.Lists;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class SSHMain {
	private Connection connection;
	private Session session;
	private BufferedReader stdout;
	private PrintWriter printWriter;
	private BufferedReader stderr;

	private final ExecutorService service = Executors.newFixedThreadPool(3);

	// 用于输入数据
	private final Scanner scanner = new Scanner(System.in);
	private Queue<String> queue = new ConcurrentLinkedQueue<String>();
	
	private static final int DEFAULT_SURVIVAL = 5; // minutes
	private long startTime; // ms
	private int survivalTime;// ms
	
	public SSHMain(String hostName, String userName, String passwd, float survivalSec) throws IOException {
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
		// 打印流
		printWriter = new PrintWriter(session.getStdin());
		// 设置存活时间
		survivalTime = Float.valueOf(survivalSec * 60 * 1000).intValue();
		startTime = System.currentTimeMillis();
	}
	public BufferedReader getStdout() {
		return stdout;
	}
	public void setStdout(BufferedReader stdout) {
		this.stdout = stdout;
	}
	public PrintWriter getPrintWriter() {
		return printWriter;
	}
	public void setPrintWriter(PrintWriter printWriter) {
		this.printWriter = printWriter;
	}
	public Queue<String> getQueue() {
		return queue;
	}
	public void setQueue(Queue<String> queue) {
		this.queue = queue;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public int getSurvivalTime() {
		return survivalTime;
	}
	public void setSurvivalTime(int survivalTime) {
		this.survivalTime = survivalTime;
	}
	public Scanner getScanner() {
		return scanner;
	}
	public Connection getConnection() {
		return connection;
	}
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	public Session getSession() {
		return session;
	}
	public void setSession(Session session) {
		this.session = session;
	}
	public BufferedReader getStderr() {
		return stderr;
	}
	public void setStderr(BufferedReader stderr) {
		this.stderr = stderr;
	}
	public ExecutorService getService() {
		return service;
	}
	
	public void close() {
		IOUtils.closeQuietly(stdout);
		IOUtils.closeQuietly(stderr);
		IOUtils.closeQuietly(printWriter);
		IOUtils.closeQuietly(scanner);
		session.close();
		connection.close();
	}
	
	
	public static void main(String[] args) {
		try {
			String ip = "192.168.1.100";
			// int port = 22;
			String username = "root";
			String password = "Jeremy2oe5";
			SSHMain ssh = new SSHMain(ip, username, password, 0.5f);
			
			//扫描器
			Task scaner = new SSHScanner(ssh);
			//任务推送
			Task pusher = new Pusher(ssh);
			//接收返回数据
			Task receiver = new Receiver(ssh.getStdout());
			Thread receiverThread = new Thread(receiver, receiver.getName());
			receiverThread.setDaemon(true);
			receiverThread.start();
			
			//心跳
			Task heartBeat = new HeartBeat(ssh);
			Thread heartBeatThread = new Thread(heartBeat, heartBeat.getName());
			//heartBeatThread.setDaemon(true);
			heartBeatThread.start();
			
			List<Thread> threads = Lists.newArrayList();
			List<Task> tasks = Lists.newArrayList(scaner, pusher);
			for (Task task : tasks) {
				Thread thread = new Thread(task, task.getName());
				thread.start();
				threads.add(thread);
			}
			
			tasks.add(heartBeat);
			threads.add(heartBeatThread);
			
			System.out.println("session 1是否开启:"+ssh.getConnection().isAuthenticationComplete());
			System.out.println("session 1是否开启"+ssh.getSession());
			while (true) {
				if ((ssh.getStartTime() + ssh.getSurvivalTime()) < System.currentTimeMillis()) {
					// 关闭线程池
					System.out.println("connection timed out, close the connection");
					for (Task task : tasks) {
						task.stop();
						System.out.println("1 task:"+task.getName()+" is Closed");
					}
//					for (Thread task : threads) {
//						task.stop();
//						System.out.println("2 task:"+task.getName()+" is Closed");
//					}
					break;
				}
				try {
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try {
				TimeUnit.SECONDS.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
//			ssh.close();
//			System.out.println("session 2是否开启:"+ssh.getConnection().isAuthenticationComplete());
//			System.out.println("session 2是否开启"+ssh.getSession());
//			System.out.println("====主线程执行完毕==="+ssh.getConnection().isAuthenticationPartialSuccess());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
