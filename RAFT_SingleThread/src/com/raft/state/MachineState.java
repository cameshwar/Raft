package com.raft.state;

import java.util.List;
import java.util.Map;

import com.raft.constants.ENodeState;
import com.raft.start.ServerNode;

public class MachineState {
	public static ENodeState nodeState = ENodeState.INITIATOR;
	
	public static List<String> nodeList;
	
	public static Map<Integer, ServerNode> portServerMap;
	
	public static void initializeMachineState(List<String> nodeList, Map<Integer, ServerNode> portServerMap) {
		MachineState.nodeList = nodeList;
		MachineState.portServerMap = portServerMap;
	}
	
	public synchronized static void setState(ENodeState state) {
		MachineState.nodeState = state;		
	}
	
	public static List<String> getNodeList() {
		return MachineState.nodeList;
	}
}
