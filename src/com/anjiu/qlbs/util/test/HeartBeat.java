package com.anjiu.qlbs.util.test;

import java.util.concurrent.TimeUnit;

public class HeartBeat extends Task implements Runnable{
	
	private SSHMain ssh;
	public HeartBeat(SSHMain ssh){
		this.ssh = ssh;
	}

	@Override
	public void run() {
		while (true) {
//			if (isStop()) {
//				System.out.println("关闭HeartBeat线程");
//				break;
//			}
			ssh.getQueue().add("\r\n");
			try {
				TimeUnit.SECONDS.sleep(15);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		
	}
	
	

}
