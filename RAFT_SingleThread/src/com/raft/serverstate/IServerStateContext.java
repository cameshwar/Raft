package com.raft.serverstate;

import com.raft.start.ServerStateNode;
import com.sun.xml.internal.ws.util.ByteArrayBuffer;


public interface IServerStateContext {
	
	public void changeState(ServerStateNode server);
	
	public boolean isReady();
	
	public void processData(ByteArrayBuffer buf);
	
}
