package com.raft.serverstate;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import com.raft.rpc.AppendEntriesRPC;
import com.raft.start.ServerStateNode;
import com.sun.xml.internal.ws.util.ByteArrayBuffer;


public class ReadableServerState implements IServerStateContext{
	
	private static ReadableServerState serverState = null;
	
	private AppendEntriesRPC appendEntriesRPC;
	
	private boolean ready = false;
	
	private boolean reading = false;
	
	private ByteArrayBuffer readData = new ByteArrayBuffer();
	
	public boolean isReading() {
		return reading;
	}

	public AppendEntriesRPC getAppendEntriesRPC() {
		return appendEntriesRPC;
	}

	private ReadableServerState() {
		// TODO Auto-generated constructor stub
	}
	
	public static IServerStateContext getServerStateContext() {
		if(ReadableServerState.serverState == null)
			ReadableServerState.serverState = new ReadableServerState();		
		return ReadableServerState.serverState;
	}

	@Override
	public void changeState(final ServerStateNode server) {
		this.ready = false;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				SocketChannel client = null;
				Selector selector = server.getSelector();
				
				try {
					while(true) {
					selector.select();
					for (Iterator<SelectionKey> i = selector.selectedKeys().iterator(); i.hasNext();) {
						SelectionKey key = i.next(); 
						i.remove();
						
						client = (SocketChannel) key.channel();					
						
						ByteBuffer buffer = ByteBuffer.allocate(1024);
						
						while(client.read(buffer) > 0) {					
							ready = true;
							buffer.flip();
							while(buffer.hasRemaining()) {
								readData.write(buffer.get());
							}
							buffer.clear();
						}
						
					}					
					if(ready) 
						break;
					}
				} catch (IOException e) {					
					e.printStackTrace();
				}				
			}
			
		}).start();
		
	}

	@Override
	public boolean isReady() {
		return this.ready;
	}

	@Override
	public void processData(ByteArrayBuffer buf) {
		buf = this.readData;
		this.ready = false;
	}

	

}
