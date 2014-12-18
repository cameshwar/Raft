package com.raft.machinestate;

import com.raft.constants.EMachineState;
import com.raft.constants.MachineState;



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
		MachineState.setMachineState(EMachineState.FOLLOWER);		
	}
	
}
