package com.anjiu.qlbs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class ScpClient implements Runnable{

	public volatile boolean state;
	
	private Connection conn;
	private String[] commands;
	
	public ScpClient(Connection conn){
		this.conn = conn;
	}
	
	public ScpClient(Connection conn, String[] commands){
		this.conn = conn;
		this.commands = commands;
	}
	
	@Override
	public void run() {
		Session session;
		try {
			session = conn.openSession();
			session.requestDumbPTY();   //建立虚拟终端
		session.startShell();           //打开一个shell
		OutputStream fout = session.getStdin();  //也可以用PrintWriter out = new PrintWriter(session.getStdin());
		for (String cmd : commands) {
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
		        if (line == null) break;
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

}
