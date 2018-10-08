package com.anjiu.qlbs.util.test;

import java.util.concurrent.TimeUnit;

public class Pusher extends Task implements Runnable{
	
	private SSHMain ssh;
	public Pusher(SSHMain ssh){
		this.ssh = ssh;
	}
	
	@Override
	public void run() {
		while (true) {
			if (!ssh.getQueue().isEmpty()) {
				String cmd = ssh.getQueue().poll();
				ssh.getPrintWriter().write(cmd);
				ssh.getPrintWriter().flush();
				System.out.println("=========Pusher 1===========");
			}
//			if (isStop()) {
//				System.out.println("关闭Pusher线程");
//				break;
//			}
			try {
				TimeUnit.MICROSECONDS.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
