package com.raft.machinestate;

import java.util.Map;

import com.raft.constants.EMachineState;
import com.raft.constants.MachineState;
import com.raft.start.ServerStateNode;



public class InitiatorState implements IMachineContext{
	
	private static InitiatorState initiatorState = null;
	
	private InitiatorState() {		
	}
	
	public static IMachineContext getMachineContext() {
		if(initiatorState == null)
			initiatorState = new InitiatorState();
		return initiatorState;
	}
	
	@Override
	public void process(Map<Integer, ServerStateNode> servers) {
		MachineState.setMachineState(EMachineState.FOLLOWER);		
	}
	
}
