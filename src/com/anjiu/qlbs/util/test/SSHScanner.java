package com.anjiu.qlbs.util.test;

import java.util.concurrent.TimeUnit;

public class SSHScanner extends Task{
	private SSHMain ssh;
	
	public SSHScanner(SSHMain ssh){
		this.ssh = ssh;
	}
	
	@Override
	public void run() {
		int count = 15;
		while (true) {
			if (isStop()) {
				System.out.println("关闭SSHScanner线程");
				break;
			}
			ssh.getQueue().add("ll\r\n");
			// 重置存活时间
			ssh.setStartTime(System.currentTimeMillis());
			try {
				TimeUnit.SECONDS.sleep(count);
				count += count;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
