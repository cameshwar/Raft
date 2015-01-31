package com.raft.machinestate;

public class LeaderState implements IMachineContext{
	
private static LeaderState leaderState = null;
	
	private LeaderState() {
	}
	
	public static IMachineContext getMachineContext() {
		if(leaderState == null)
			leaderState = new LeaderState();
		return leaderState;
	}

	@Override
	public void process() {
		System.out.println("Leader");
		while(true);
		
	}

}
