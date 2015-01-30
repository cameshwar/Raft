package com.raft.utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

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
	
	public static ServerSocketChannel creteServer(int port) {
		ServerSocketChannel server = null;
		try {
			server = ServerSocketChannel.open();
			server.configureBlocking(false);
			server.socket().bind(new InetSocketAddress(port));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Created Server listening in port: "+port);
		return server;
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
		if(MachineState.nodeState == EMachineState.INITIATOR)
			server = MachineState.portServerMap.get(state==EServerState.READ?IRaftConstants.INITIATOR_READ_PORT:IRaftConstants.INITIATOR_WRITE_PORT);
		else if(MachineState.nodeState == EMachineState.FOLLOWER)
			server = MachineState.portServerMap.get(state==EServerState.READ?IRaftConstants.FOLLOWER_READ_PORT:IRaftConstants.FOLLOWER_WRITE_PORT);
		else if(MachineState.nodeState == EMachineState.CANDIDATE)
			server = MachineState.portServerMap.get(state==EServerState.READ?IRaftConstants.CANDIDATE_READ_PORT:IRaftConstants.CANDIDATE_WRITE_PORT);
		else if(MachineState.nodeState == EMachineState.LEADER)
			server = MachineState.portServerMap.get(state==EServerState.READ?IRaftConstants.LEADER_READ_PORT:IRaftConstants.LEADER_WRITE_PORT);
		return server;
	}

}
