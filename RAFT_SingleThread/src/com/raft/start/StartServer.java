package com.raft.start;

import java.util.Map;

import com.raft.constants.MachineState;
import com.raft.utils.ServerUtils;

public class StartServer {
	
	private static StartServer server;
	
	private StartServer() {
		
	}
	
	public static StartServer getServer() {
		if(server == null) { 
			server = new StartServer();
		}
		return server;			
	}
	
	public void startServer(final Map<Integer, ServerStateNode> portServerMap) {
		for(final Integer port: portServerMap.keySet()) {
			//new Thread(portServerMap.get(port), "Server:"+port).start();
			new Thread() {
				@Override
				public void run() {					
					startServerState();
				}
				
				private void startServerState() {
					while(MachineState.serverState.changeServerState(ServerUtils.getServerContext(), portServerMap.get(port)));
				}
			}.start();
		}
			
	}

}
