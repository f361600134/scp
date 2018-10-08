package com.anjiu.qlbs.util.test;

public abstract class Task implements Runnable{
	
	private volatile boolean stop = false;
	
	public void stop(){
		System.out.println(getName()+"设置标志位为true");
		stop = true;
	}
	
	public boolean isStop(){
		return stop;
	}

	public String getName(){
		return this.getClass().getName();
	}
}
