package com.raft.utils;

import com.raft.constants.ENodeState;
import com.raft.state.CandidateState;
import com.raft.state.FollowerState;
import com.raft.state.IMachineContext;
import com.raft.state.InitiatorState;
import com.raft.state.LeaderState;
import com.raft.state.MachineState;

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
