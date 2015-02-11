package com.raft.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.raft.constants.EMachineState;
import com.raft.constants.EServerState;
import com.raft.constants.IRaftConstants;
import com.raft.constants.MachineState;
import com.raft.serverstate.AcceptableServerState;
import com.raft.serverstate.IServerStateContext;
import com.raft.serverstate.ReadableServerState;
import com.raft.serverstate.WritableServerState;
import com.raft.start.ServerStateNode;

public class ServerUtils {
	
	public static ServerSocketChannel createServer(String hostName, int port) {
		ServerSocketChannel server = null;
		try {
			server = ServerSocketChannel.open();
			server.configureBlocking(false);
			server.socket().bind(new InetSocketAddress(hostName, port));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Created Server listening in port: "+port);
		return server;
	}
	
	public static Integer getMachineServerState(EMachineState machineState, EServerState serverState) {
		Integer machineServerState = null;
		switch(machineState) {
			case INITIATOR:
				machineServerState = serverState==EServerState.READ?
						IRaftConstants.INITIATOR_READ_PORT:
							IRaftConstants.INITIATOR_WRITE_PORT;
				break;
			case FOLLOWER:
				machineServerState = serverState==EServerState.READ?
						IRaftConstants.FOLLOWER_READ_PORT:
							IRaftConstants.FOLLOWER_WRITE_PORT;
				break;
			case CANDIDATE:
				machineServerState = serverState==EServerState.READ?
						IRaftConstants.CANDIDATE_READ_PORT:
							IRaftConstants.CANDIDATE_WRITE_PORT;
				break;
			case LEADER:
				machineServerState = serverState==EServerState.READ?
						IRaftConstants.LEADER_READ_PORT:
							IRaftConstants.LEADER_WRITE_PORT;
				break;			
		}
		return machineServerState;
	}
	
	public static Map<String, String> serverProperties(String fileName) {
		Properties serverProperties = new Properties();
		Map<String, String> serverMap = new HashMap<String, String>();
		try {
			serverProperties.load(new FileInputStream(new File(fileName)));
			for(Object serverKeys: serverProperties.keySet())
					serverMap.put((String)serverKeys, (String)serverProperties.get(serverKeys));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return serverMap;
	}
	
	public static void getPropertiesInstance() {
		
	}
	
	public static IServerStateContext getServerContext(EServerState state) {
		
		IServerStateContext serverContext = null;
		if(state == EServerState.ACCEPT)
			serverContext = AcceptableServerState.getServerStateContext();
		else if(state == EServerState.READ)
			serverContext = ReadableServerState.getServerStateContext();
		else if(state == EServerState.WRITE)
			serverContext = WritableServerState.getServerStateContext();
		
		 return serverContext;
	}
	
	public static ServerStateNode getServerNode(EServerState state) {
		ServerStateNode server = null;
		System.out.println(MachineState.getNodeState().toString());
		if(EMachineState.INITIATOR.equals(MachineState.getNodeState()))
			server = MachineState.portServerMap.get(state==EServerState.READ?IRaftConstants.INITIATOR_READ_PORT:IRaftConstants.INITIATOR_WRITE_PORT);
		else if(EMachineState.FOLLOWER.equals(MachineState.getNodeState()))
			server = MachineState.portServerMap.get(state==EServerState.READ?IRaftConstants.FOLLOWER_READ_PORT:IRaftConstants.FOLLOWER_WRITE_PORT);
		else if(EMachineState.CANDIDATE.equals(MachineState.getNodeState()))
			server = MachineState.portServerMap.get(state==EServerState.READ?IRaftConstants.CANDIDATE_READ_PORT:IRaftConstants.CANDIDATE_WRITE_PORT);
		else if(EMachineState.LEADER.equals(MachineState.getNodeState()))
			server = MachineState.portServerMap.get(state==EServerState.READ?IRaftConstants.LEADER_READ_PORT:IRaftConstants.LEADER_WRITE_PORT);
		return server;
	}

}
