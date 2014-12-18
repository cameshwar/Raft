package com.raft.serverstate;

import com.raft.start.ServerStateNode;


public class WritableServerState implements IServerStateContext{
	
	private static WritableServerState serverState = null;
	
	private WritableServerState() {
		// TODO Auto-generated constructor stub
	}
	
	public static IServerStateContext getServerStateContext() {
		if(WritableServerState.serverState == null)
			WritableServerState.serverState = new WritableServerState();
		return WritableServerState.serverState;
	}

	@Override
	public void changeState(ServerStateNode server) {
		// TODO Auto-generated method stub
		
	}
}
