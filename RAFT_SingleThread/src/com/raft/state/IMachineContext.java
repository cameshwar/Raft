package com.raft.state;

import java.util.Map;

import com.raft.constants.ENodeState;
import com.raft.start.ServerNode;


public interface IMachineContext {
	public void setState(ENodeState nodeState);
	public ENodeState getState();
	public void process();
	public void portServerMap(Map<Integer, ServerNode> portServerMap);
}
