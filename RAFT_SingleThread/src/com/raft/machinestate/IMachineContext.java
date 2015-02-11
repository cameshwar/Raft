package com.raft.machinestate;

import java.util.Map;

import com.raft.start.ServerStateNode;

public interface IMachineContext {
	public void process(Map<Integer, ServerStateNode> servers);
}
