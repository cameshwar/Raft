package com.raft.machinestate;

public class CandidateState implements IMachineContext{
	
	private static CandidateState candidateState = null;
	
	private CandidateState() {
	}
	
	public static IMachineContext getMachineContext() {
		if(candidateState == null)
			candidateState = new CandidateState();
		return candidateState;
	}

	@Override
	public void process() {
		// TODO Auto-generated method stub
		
	}

}
