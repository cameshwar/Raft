package com.raft.machinestate;

import java.util.Map;

import com.raft.start.ServerStateNode;

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
	public void process(Map<Integer, ServerStateNode> servers) {
		while(true);
		
	}

}
