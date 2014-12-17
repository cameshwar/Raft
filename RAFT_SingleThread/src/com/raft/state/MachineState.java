package com.raft.state;

import java.util.List;

import com.raft.constants.ENodeState;

public class MachineState {
	public static ENodeState nodeState = ENodeState.INITIATOR;
	
	public static List<String> nodeList;
	
	public synchronized static void setState(ENodeState state) {
		MachineState.nodeState = state;		
	}
	
	public static void setNodeList(List<String> nodeList) {
		MachineState.nodeList = nodeList;
	}
	
	public static List<String> getNodeList() {
		return MachineState.nodeList;
	}
}
