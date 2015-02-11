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
	
	private static Map<String, String> serverPropertiesMap;	
	
	private static String serverName;
	
	
	public static String getServerName() {
		return serverName;
	}

	public static void setServerName(String serverName) {
		MachineState.serverName = serverName;
	}

	public static Map<String, String> getServerPropertiesMap() {
		return serverPropertiesMap;
	}

	public static void setServerPropertiesMap(
			Map<String, String> serverPropertiesMap) {
		MachineState.serverPropertiesMap = serverPropertiesMap;
	}

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
