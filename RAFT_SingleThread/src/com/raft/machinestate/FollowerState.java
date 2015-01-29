package com.raft.machinestate;

import com.raft.constants.EMachineState;
import com.raft.constants.EServerState;
import com.raft.constants.IRaftConstants;
import com.raft.constants.MachineState;
import com.raft.rpc.AppendEntriesRPC;
import com.raft.serverstate.ReadableServerState;
import com.raft.serverstate.WritableServerState;
import com.raft.start.ServerStateNode;
import com.raft.timer.TimerThread;
import com.raft.utils.ServerUtils;

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
		
		ServerStateNode server = MachineState.portServerMap.get(IRaftConstants.FOLLOWER_PORT);
		ReadableServerState readableServer = null;
		WritableServerState writableServer = null;
		while(true) {
			if(MachineState.getServerState() == EServerState.READ)
				readableServer = (ReadableServerState) ServerUtils.getServerContext();
			else
				writableServer = (WritableServerState) ServerUtils.getServerContext();
			if(readableServer.isReading()) {								
				timer.setResetTimer(true);	
				while(readableServer.getAppendEntriesRPC()==null);
				AppendEntriesRPC appendEntriesRPC = server.getAppendEntriesRPC();
				MachineState.setServerState(EServerState.WRITE);
				timer.setResetTimer(false);
			} else if(timer.isTimeOut()) {
				System.out.println("Context Promoted: Candidate");
				MachineState.setMachineState(EMachineState.CANDIDATE);
				break;
			}			 
		}
	}

}
