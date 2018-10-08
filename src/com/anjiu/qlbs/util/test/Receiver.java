package com.anjiu.qlbs.util.test;

import java.io.BufferedReader;
import java.io.IOException;

public class Receiver extends Task implements Runnable{
	
	private BufferedReader stdout;
	
	public Receiver(BufferedReader stdout){
		this.stdout = stdout;
	}
	
	public void stop(){
		super.stop();
		try {
			stdout.close();
			System.out.println("关闭Receiver线程");
		} catch (IOException e) {
			e.printStackTrace();
		}
	} 

	@Override
	public void run() {
		String line;
		try {
			while (true) {
				if((line = stdout.readLine()) != null){
					System.out.println(line);
				}
				if (isStop()) {
					System.out.println("关闭Receiver线程");
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
