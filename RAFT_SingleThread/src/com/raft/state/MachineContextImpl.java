package com.raft.state;

import java.util.Map;

import com.raft.constants.ENodeState;
import com.raft.constants.IRaftConstants;
import com.raft.rpc.AppendEntriesRPC;
import com.raft.start.ServerNode;
import com.raft.timer.TimerThread;

public class MachineContextImpl implements IMachineContext{
	
	private ENodeState nodeState;
	
	private Map<Integer, ServerNode> portServerMap;
	
	private boolean signalled = false;
	
	public void setSignalled(boolean signalled) {
		this.signalled = signalled;
	}

	@Override
	public void portServerMap(Map<Integer, ServerNode> portServerMap) {
		this.portServerMap = portServerMap;
	}
	
	@Override
	public void process() {
		
		switch(nodeState) {
		case INITIATOR:
			processInitiator();
			break;
			
		case FOLLOWER:
			processFollower();
			break;
			
		case CANDIDATE:
			processCandidate();
			break;
			
		case LEADER:
			processLeader();
			break;			
		}
		
	}
	
	private void processInitiator() {
		System.out.println("Context: Initiator");
		nodeState = ENodeState.FOLLOWER;		
	}
	
	private void processFollower() {
		System.out.println("Context: Follower");
		TimerThread timer = new TimerThread(IRaftConstants.FOLLOWER_TIMEOUT);
		new Thread(timer,"Timer Follower").start();
		
		ServerNode server = portServerMap.get(IRaftConstants.FOLLOWER_PORT);
		while(true) {
			if(server.isReadyToRead() && !server.isReadytoWrite()) {
				timer.setResetTimer(true);
				while(server.getAppendEntriesRPC()==null);
				AppendEntriesRPC appendEntriesRPC = server.getAppendEntriesRPC();
				
			} else if(timer.isTimeOut()) {
				System.out.println("Context Promoted: Candidate");
				nodeState = ENodeState.CANDIDATE;
				break;
			}			 
		}
		
	}
	
	private void processCandidate() {
		
	}
	
	private void processLeader() {
		
	}
	
	@Override
	public void setState(ENodeState nodeState) {
		this.nodeState = nodeState;
	}
	
	@Override
	public ENodeState getState() {
		return nodeState;
	}
}
