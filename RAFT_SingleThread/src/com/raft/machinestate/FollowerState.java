package com.raft.machinestate;

import com.raft.constants.ENodeState;
import com.raft.constants.IRaftConstants;
import com.raft.rpc.AppendEntriesRPC;
import com.raft.start.ServerNode;
import com.raft.timer.TimerThread;

public class FollowerState implements IMachineContext{
	
	private static FollowerState followerState = null;
	
	private FollowerState() {		
	}
	
	public static IMachineContext getMachineContext() {
		if(followerState == null)
			followerState = new FollowerState();
		return followerState;
	}
	
	@Override
	public void process() {
		System.out.println("Context: Follower");
		TimerThread timer = new TimerThread(IRaftConstants.FOLLOWER_TIMEOUT);
		new Thread(timer,"Timer Follower").start();
		
		ServerNode server = MachineState.portServerMap.get(IRaftConstants.FOLLOWER_PORT);
		while(true) {
			if(server.isReadyToRead() && !server.isReadytoWrite()) {
				timer.setResetTimer(true);
				while(server.getAppendEntriesRPC()==null);
				AppendEntriesRPC appendEntriesRPC = server.getAppendEntriesRPC();
				timer.setResetTimer(false);
			} else if(timer.isTimeOut()) {
				System.out.println("Context Promoted: Candidate");
				MachineState.setState(ENodeState.CANDIDATE);
				break;
			}			 
		}
	}

}
