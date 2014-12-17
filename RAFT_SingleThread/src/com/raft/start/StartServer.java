package com.raft.start;

import java.util.Map;

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
	
	public void startServer(Map<Integer, ServerNode> portServerMap) {
		for(Integer port: portServerMap.keySet()) {
			new Thread(portServerMap.get(port), "Server:"+port).start();
		}
			
	}

}
