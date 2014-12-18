package com.raft.start;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

import com.raft.rpc.AppendEntriesRPC;
import com.raft.rpc.RequestVotesRPC;

public class ServerStateNode{

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
	
	public ServerStateNode(ServerSocketChannel server) {
		try {
			this.server = server;
			this.selector = Selector.open();
			this.server.register(selector, SelectionKey.OP_ACCEPT);
		} catch (ClosedChannelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
