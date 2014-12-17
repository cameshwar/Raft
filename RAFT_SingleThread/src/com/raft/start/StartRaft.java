package com.raft.start;

import java.util.Map;

import com.raft.constants.ENodeState;
import com.raft.state.IMachineContext;
import com.raft.state.MachineContextImpl;


public class StartRaft implements Runnable{
	
	private IMachineContext machineContext;
	
	public StartRaft(Map<Integer, ServerNode> portServerMap) {
		this.machineContext = new MachineContextImpl();
		this.machineContext.setState(ENodeState.INITIATOR);
		this.machineContext.portServerMap(portServerMap);
	}
	
	@Override
	public void run() {		
		initializeRaft();
	}
	
	private void initializeRaft() {
		while(machineContext.getState().process(machineContext));
	}
}
