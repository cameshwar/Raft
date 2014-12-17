package com.raft.state;

import com.raft.constants.ENodeState;



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
	public void process() {
		System.out.println("Context: Initiator");
		MachineState.setState(ENodeState.FOLLOWER);		
	}
	
}
