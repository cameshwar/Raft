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
	
	public static IServerStateContext getServerContext() {
		
		IServerStateContext serverContext = null;
		if(MachineState.getServerState() == EServerState.ACCEPT)
			serverContext = AcceptableServerState.getServerStateContext();
		else if(MachineState.getServerState() == EServerState.READ)
			serverContext = ReadableServerState.getServerStateContext();
		else if(MachineState.getServerState() == EServerState.WRITE)
			serverContext = WritableServerState.getServerStateContext();
		
		 return serverContext;
	}
	
	public static ServerStateNode getServerNode() {
		ServerStateNode server = null;
		if(MachineState.nodeState == EMachineState.INITIATOR)
			server = MachineState.portServerMap.get(IRaftConstants.INITIATOR_PORT);
		else if(MachineState.nodeState == EMachineState.FOLLOWER)
			server = MachineState.portServerMap.get(IRaftConstants.FOLLOWER_PORT);
		else if(MachineState.nodeState == EMachineState.CANDIDATE)
			server = MachineState.portServerMap.get(IRaftConstants.CANDIDATE_PORT);
		else if(MachineState.nodeState == EMachineState.LEADER)
			server = MachineState.portServerMap.get(IRaftConstants.LEADER_PORT);
		return server;
	}

}
