package com.raft.utils;

import com.raft.constants.ENodeState;
import com.raft.machinestate.CandidateState;
import com.raft.machinestate.FollowerState;
import com.raft.machinestate.IMachineContext;
import com.raft.machinestate.InitiatorState;
import com.raft.machinestate.LeaderState;
import com.raft.machinestate.MachineState;

public class MachineContextUtils {
	
	public static IMachineContext getMachineContext() {
		
		IMachineContext machineContext = null;
		if(MachineState.nodeState == ENodeState.INITIATOR)
			machineContext = InitiatorState.getMachineContext();
		else if(MachineState.nodeState == ENodeState.FOLLOWER)
			machineContext = FollowerState.getMachineContext();
		else if(MachineState.nodeState == ENodeState.CANDIDATE)
			machineContext = CandidateState.getMachineContext();
		else if(MachineState.nodeState == ENodeState.LEADER)
			machineContext = LeaderState.getMachineContext();
		
		 return machineContext;
	}

}
