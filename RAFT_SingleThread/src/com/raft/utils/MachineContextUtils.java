package com.raft.utils;

import com.raft.constants.EMachineState;
import com.raft.constants.MachineState;
import com.raft.machinestate.CandidateState;
import com.raft.machinestate.FollowerState;
import com.raft.machinestate.IMachineContext;
import com.raft.machinestate.InitiatorState;
import com.raft.machinestate.LeaderState;

public class MachineContextUtils {
	
	public static IMachineContext getMachineContext() {
		
		IMachineContext machineContext = null;
		if(MachineState.nodeState == EMachineState.INITIATOR)
			machineContext = InitiatorState.getMachineContext();
		else if(MachineState.nodeState == EMachineState.FOLLOWER)
			machineContext = FollowerState.getMachineContext();
		else if(MachineState.nodeState == EMachineState.CANDIDATE)
			machineContext = CandidateState.getMachineContext();
		else if(MachineState.nodeState == EMachineState.LEADER)
			machineContext = LeaderState.getMachineContext();
		
		 return machineContext;
	}

}
