package com.raft.constants;

import com.raft.machinestate.IMachineContext;

public enum EMachineState {
	INITIATOR {
		@Override
		public boolean process(IMachineContext machineContext) {
			machineContext.process();
			System.out.println("Machine State: Initiator");
			//machineContext.setState(FOLLOWER);
			return true;
		}
	}, 
	FOLLOWER {
		@Override
		public boolean process(IMachineContext machineContext) {
			machineContext.process();
			System.out.println("Machine State: Follower");
			//MachineState.setState(CANDIDATE);
			return true;
		}
	}, 
	CANDIDATE {
		@Override
		public boolean process(IMachineContext machineContext) {
			machineContext.process();
			//MachineState.setState(LEADER);
			//MachineState.setState(FOLLOWER);
			return true;
		}
	}, 
	LEADER {
		@Override
		public boolean process(IMachineContext machineContext) {
			machineContext.process();			
			//MachineState.setState(FOLLOWER);
			return true;
		}
	};
	
	public abstract boolean process(IMachineContext machineContext);
}
