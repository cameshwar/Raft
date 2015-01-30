package com.raft.serverstate;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import com.raft.constants.EServerState;
import com.raft.constants.MachineState;
import com.raft.start.ServerStateNode;
import com.sun.xml.internal.ws.util.ByteArrayBuffer;


public class WritableServerState implements IServerStateContext{
	
	private static WritableServerState serverState = null;
	
	private boolean ready = false;
	
	//private AppendEntriesRPC appendEntriesRPC;
	
/*	public AppendEntriesRPC getAppendEntriesRPC() {
		return appendEntriesRPC;
	}*/	
	
	private WritableServerState() {
		// TODO Auto-generated constructor stub
	}
	
	public static IServerStateContext getServerStateContext() {
		if(WritableServerState.serverState == null)
			WritableServerState.serverState = new WritableServerState();
		return WritableServerState.serverState;
	}

	@Override
	public void changeState(ServerStateNode server) {
		System.out.println("Writing");
		SocketChannel client = null;
		Selector selector = server.getSelector();
		
		try {
			while(true) {
			selector.select();
			for (Iterator<SelectionKey> i = selector.selectedKeys().iterator(); i.hasNext();) {
				SelectionKey key = i.next(); 
				i.remove();
				
				client = (SocketChannel) key.channel();					
				
				
				//ReadableByteChannel channel = Channels.newChannel(client.socket().getInputStream());
				client = (SocketChannel) key.channel();
				ByteBuffer buf = null;										
				buf = ByteBuffer.wrap(Integer.toString(5).getBytes());
				client.write(buf);
				//key.interestOps(SelectionKey.OP_READ);
				//MachineState.setServerState(EServerState.READ);
				if(MachineState.serverState!=EServerState.WRITE) {
					System.out.println("key set");
					key.interestOps(SelectionKey.OP_READ);
					break;
				}
			}
			if(MachineState.serverState!=EServerState.WRITE)
				break;
			
		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean isReady() {
		return ready;
	}

	@Override
	public void processData(ByteArrayBuffer buf) {
		// TODO Auto-generated method stub
		
	}
}
