package com.raft.start;

import com.raft.constants.MachineState;
import com.raft.utils.MachineContextUtils;


public class StartRaft implements Runnable{
	
	@Override
	public void run() {		
		initializeRaft();
	}
	
	private void initializeRaft() {
		while(MachineState.nodeState.process(MachineContextUtils.getMachineContext()));
	}
}
