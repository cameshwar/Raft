package com.raft.serverstate;

import com.raft.start.ServerStateNode;


public interface IServerStateContext {
	
	public void changeState(ServerStateNode server);

}
