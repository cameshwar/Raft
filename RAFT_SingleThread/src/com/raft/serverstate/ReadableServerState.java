package com.raft.serverstate;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import com.raft.constants.EServerState;
import com.raft.constants.MachineState;
import com.raft.rpc.AppendEntriesRPC;
import com.raft.rpc.XMLReaderRPC;
import com.raft.start.ServerStateNode;
import com.sun.xml.internal.ws.util.ByteArrayBuffer;


public class ReadableServerState implements IServerStateContext{
	
	private static ReadableServerState serverState = null;
	
	private AppendEntriesRPC appendEntriesRPC;
	
	private boolean reading = false;
	
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
	public void changeState(ServerStateNode server) {
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
				ByteBuffer buffer = ByteBuffer.allocate(1024);
				ByteArrayBuffer data = new ByteArrayBuffer();
				XMLReaderRPC readerRPC = new XMLReaderRPC();
				while(client.read(buffer) > 0) {
					this.reading = true;
					buffer.flip();
					while(buffer.hasRemaining()) {
						data.write(buffer.get());
					}							
				}
				if(data.size()>0) {					
					readerRPC.readDocument(data.toString());
					readerRPC.processRPC();
					System.out.println("RPC Processed");
					this.appendEntriesRPC = new AppendEntriesRPC(readerRPC.getValueMap());
					System.out.println("RPC Obj Created");
					data.reset();					
					key.interestOps(SelectionKey.OP_WRITE);				
				
					MachineState.setServerState(EServerState.WRITE);
				
					data.close();			
				
					System.out.println("Data Read");
					this.appendEntriesRPC = null;
					this.reading = false;
				}
				if(MachineState.serverState!=EServerState.READ)
					break;
				//client.socket().close();				
			}
			if(MachineState.serverState!=EServerState.READ)
				break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	

}
