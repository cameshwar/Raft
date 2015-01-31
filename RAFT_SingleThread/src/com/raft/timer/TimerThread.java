package com.raft.timer;

public class TimerThread implements Runnable{

	private long timer;
	
	private boolean timeOut = false; 
	
	private boolean resetTimer = false;
	
	private boolean shutTimer = false;
	
	public void setResetTimer(boolean resetTimer) {
		this.resetTimer = resetTimer;
	}
	public boolean isTimeOut() {
		return timeOut;
	}
	public TimerThread(long timer) {
		this.timer = timer;
	}
	
	public void setShutTimer(boolean shutTimer) {
		this.shutTimer = shutTimer;
	}
	
	@Override
	public void run() {
		long now = System.currentTimeMillis();
		while(true) {
			if(shutTimer) {
				break;
			} else if((System.currentTimeMillis()-now) > timer) {
				//System.out.println("Signalled as timeout");
				timeOut = true;
				//break;			
			} else if(resetTimer) {
				now = System.currentTimeMillis();
				//resetTimer = false;
				//System.out.println("Timer Resetted");
			}
		}
		System.out.println("Timer thread - Shutdown");
	}
}
