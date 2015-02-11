package com.raft.main;

import java.util.ArrayList;
import java.util.List;

import com.raft.constants.MachineState;
import com.raft.start.StartRaft;
import com.raft.utils.ServerUtils;

public class RAFTMain {
	public static void main(String[] args) {		
						
		MachineState.setServerPropertiesMap(ServerUtils.serverProperties("properties/server.properties"));		
		MachineState.setServerName(args[0]);
		String serverIp = MachineState.getServerPropertiesMap().get(MachineState.getServerName());
		/*Map<Integer, ServerStateNode> portServerMap = new HashMap<Integer, ServerStateNode>();
		for(EMachineState machineState : EMachineState.values()) 
			for(EServerState serverState : EServerState.values()) 
				if(serverState != EServerState.ACCEPT) {
					Integer serverMachineState = ServerUtils.getMachineServerState(machineState, serverState);
					portServerMap.put(
							serverMachineState,
							new ServerStateNode(ServerUtils
									.createServer(serverIp,serverMachineState),
									serverMachineState.toString(), serverState));
				}		
		
		System.out.println("Starting Server ");
		StartServer.getServer().startServer(portServerMap);*/
		
		List<String> serverList = new ArrayList<String>();
		serverList.add("127.0.0.1");
		//serverList.add("localhost");
		MachineState.serverList = serverList;
		
		// Step2:
		List<String> nodeList = new ArrayList<String>();
		MachineState.initializeMachineState(nodeList);
		System.out.println("Starting Raft");
		new Thread(new StartRaft()).start();
	}
}
