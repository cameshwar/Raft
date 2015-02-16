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
	
	private boolean close = false;
	
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
				System.out.println("in read");
				SocketChannel client = null;
				Selector selector = server.getSelector();
				
				try {
					while(true) {
					if(close) {
						close = false;
						if(client!=null) client.close();
						break;
					}
					selector.select();
					for (Iterator<SelectionKey> i = selector.selectedKeys().iterator(); i.hasNext();) {
						SelectionKey key = i.next(); 
						i.remove();
						if(key.isReadable())
							client = (SocketChannel) key.channel();
						else
							continue;;
						
						ByteBuffer buffer = ByteBuffer.allocate(1024);
						
						while(client.read(buffer) > 0) {
							System.out.println("Data Received");
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
		//this.ready = false;
	}

	@Override
	public void closeConnection(ServerStateNode server) {
		this.close = true;
		while(!close);
		server.destroy();
	}

	

}
