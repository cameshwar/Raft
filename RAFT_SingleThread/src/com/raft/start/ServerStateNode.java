package com.raft.start;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

import com.raft.constants.EServerState;
import com.raft.rpc.AppendEntriesRPC;
import com.raft.rpc.RequestVotesRPC;

public class ServerStateNode{
	
	private EServerState serverState;
	
	public EServerState getServerState() {
		return serverState;
	}

	private String name = null;

	private ServerSocketChannel server;
	
	private Selector selector;

	public Selector getSelector() {
		return selector;
	}

	public void setSelector(Selector selector) {
		this.selector = selector;
	}
	
	private AppendEntriesRPC appendEntriesRPC;
	
	private RequestVotesRPC requestVotesRPC;
	
	public AppendEntriesRPC getAppendEntriesRPC() {
		return appendEntriesRPC;
	}

	public void setAppendEntriesRPC(AppendEntriesRPC appendEntriesRPC) {
		this.appendEntriesRPC = appendEntriesRPC;
	}

	public RequestVotesRPC getRequestVotesRPC() {
		return requestVotesRPC;
	}

	public void setRequestVotesRPC(RequestVotesRPC requestVotesRPC) {
		this.requestVotesRPC = requestVotesRPC;
	}	
	
	public ServerStateNode(ServerSocketChannel server, String name, EServerState serverState) {
		try {
			this.server = server;
			this.selector = Selector.open();
			this.server.register(selector, SelectionKey.OP_ACCEPT);
			this.name = name;
			this.serverState = serverState;
		} catch (ClosedChannelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {		
		return name;
	}
	
	public void destroy() {
		try {
			this.server.close();
			this.selector.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
