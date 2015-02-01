package com.raft.constants;

import java.util.List;
import java.util.Map;

import com.raft.start.ServerStateNode;

public class MachineState {
	public static EMachineState nodeState = EMachineState.INITIATOR;
	
	private static List<String> nodeList;
	
	public static Map<Integer, ServerStateNode> portServerMap;
	
	public static List<String> serverList;
	
	public static EServerState serverState = EServerState.ACCEPT;
	
	
	public synchronized static EServerState getServerState() {
		return serverState;
	}

	public static void initializeMachineState(List<String> nodeList, Map<Integer, ServerStateNode> portServerMap) {
		MachineState.nodeList = nodeList;
		MachineState.portServerMap = portServerMap;
	}
	
	public synchronized static void setMachineState(EMachineState state) {
		MachineState.nodeState = state;		
	}
	
	public synchronized static EMachineState getNodeState() {
		return MachineState.nodeState;
	}
	
	public synchronized static void setServerState(EServerState state) {
		MachineState.serverState = state;		
	}
	
	public static List<String> getNodeList() {
		return MachineState.nodeList;
	}
}
