package com.raft.constants;

import com.raft.serverstate.IServerStateContext;
import com.raft.start.ServerStateNode;

public enum EServerState {
	
	ACCEPT{
		@Override
		public boolean changeServerState(IServerStateContext serverStateContext, ServerStateNode server) {
			serverStateContext.changeState(server);
			System.out.println("Server State: Accept");
			return true;
		}
	},
	READ{
		@Override
		public boolean changeServerState(IServerStateContext serverStateContext, ServerStateNode server) {
			serverStateContext.changeState(server);
			System.out.println("Server State: Reading");
			return true;
		}
	},
	WRITE{
		@Override
		public boolean changeServerState(IServerStateContext serverStateContext, ServerStateNode server) {
			serverStateContext.changeState(server);
			System.out.println("Server State: Writing");
			return true;
		}
	};
	
	public abstract boolean changeServerState(IServerStateContext serverStateContext, ServerStateNode server);

}
