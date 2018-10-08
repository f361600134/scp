package com.anjiu.qlbs.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

/**
 * 模拟交互式终端
 * 
 * 可以考虑以后取代项目中的scp
 * 
 * 线程池无法关闭, 线程池关闭后, 进程无法关闭
 * 
 * @see http://www.programcreek.com/java-api-examples/index.php?api=ch.ethz.ssh2.SCPClient
 *
 */

public final class SSHAgent3 {

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
	
	//线程关闭标志位
	private volatile boolean stop = false;

	public Queue<String> getQueue() {
		return queue;
	}

	public void addQueue(String command) {
		queue.add(command);
	}

	public void setQueue(Queue<String> queue) {
		this.queue = queue;
	}

	public void initSession(String hostName, String userName, String passwd, float survivalSec) throws IOException {
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

	public void initSession(String hostName, String userName, String passwd) throws IOException {
		initSession(hostName, userName, passwd, DEFAULT_SURVIVAL);
	}
	
	
	public void stop(){
		stop = true;
	}

	public void execCommand() throws IOException {
		//这里stdout线程阻塞, 所以不能关闭
		service.submit(new Runnable() {
			@Override
			public void run() {
				String line;
				try {
					while (true) {
						if((line = stdout.readLine()) != null){
							System.out.println(line);
						}
						if (stop) {
							System.out.println("关闭响应线程");
							break;
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		// 从监控队列中获取到命令去执行
		service.submit(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if (stop) {
						System.out.println("关闭执行线程");
						break;
					}
					if (!queue.isEmpty()) {
						String cmd = queue.poll();
						printWriter.write(cmd);
						printWriter.flush();
						// 重置存活时间
						startTime = System.currentTimeMillis();
					}
					try {
						TimeUnit.MICROSECONDS.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if ((startTime + survivalTime) < System.currentTimeMillis()) {
						// 关闭线程池
						System.out.println("connection timed out, close the connection");
						stop();
						print();
						//close();
					}
				}
			}
		});
		// 接受命令增加到队列, 这里线程阻塞, 所以不能关闭
		service.submit(new Runnable() {
			@Override
			public void run() {
				while (true) {
					String nextLine = scanner.nextLine();
					queue.add(nextLine + "\r\n");
					if (stop) {
						System.out.println("关闭命令线程");
						break;
					}
					try {
						TimeUnit.SECONDS.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	/**
	 * 关闭
	 * 
	 * @return void
	 * @date 2018年9月30日上午11:45:25
	 */
	public void close() {
		// 关闭IO
//		IOUtils.closeQuietly(stdout);
//		IOUtils.closeQuietly(stderr);
//		IOUtils.closeQuietly(printWriter);
//		IOUtils.closeQuietly(scanner);
		// 关闭ssh连接
		session.close();
		connection.close();
		try {
			stdout.close();
			stderr.close();
			printWriter.close();
			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 关闭线程池
		service.shutdownNow();
	}

	public void print() {
		System.out.println("startTime:" + startTime + ", 存活时间:" + survivalTime);
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		SSHAgent3 sshAgent = new SSHAgent3();
		String ip = "192.168.1.106";
		// int port = 22;
		String username = "root";
		String password = "Jeremy2oe5";
		sshAgent.initSession(ip, username, password, 0.5f);
		sshAgent.execCommand();
		// sshAgent.close();
	}

}
