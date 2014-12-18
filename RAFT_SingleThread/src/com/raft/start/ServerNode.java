package com.raft.start;

import java.io.IOException;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import com.raft.constants.IRaftConstants;
import com.raft.rpc.AppendEntriesRPC;
import com.raft.rpc.RequestVotesRPC;
import com.raft.rpc.XMLReaderRPC;
import com.raft.utils.XMLUtils;
import com.sun.xml.internal.ws.util.ByteArrayBuffer;

public class ServerNode implements Runnable {
	
	private ServerSocketChannel server;
	
	private Selector selector;

	private boolean isReadyToRead;
	
	private boolean isReadytoWrite;
	
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

	public boolean isReadyToRead() {
		return isReadyToRead;
	}

	public boolean isReadytoWrite() {
		return isReadytoWrite;
	}
	
	public ServerNode(ServerSocketChannel server) {
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

	@Override
	public void run() {
		
		SocketChannel client = null;
		int message = 0;
		System.out.println("Status of Server: "+Thread.currentThread().getName());
		try {
			while (true) {
				selector.select();
				this.isReadyToRead = false;
				this.isReadytoWrite = false;
				for (Iterator<SelectionKey> i = selector.selectedKeys().iterator(); i.hasNext();) { 
					
					SelectionKey key = i.next(); 
					i.remove();
										
					/*if (key.isConnectable()) {
						((SocketChannel)key.channel()).finishConnect(); 
					} 
					else*/ if (key.isAcceptable()) { 
						// accept connection
						this.isReadyToRead = false;
						this.isReadytoWrite = false;
						ServerSocketChannel server = (ServerSocketChannel)key.channel();
						client = server.accept(); 
						client.configureBlocking(false); 
						client.socket().setTcpNoDelay(true); 
						client.register(selector, SelectionKey.OP_READ);						
					} else if (key.isReadable()) {
						this.isReadyToRead = true;
						this.isReadytoWrite = false;
						client = (SocketChannel) key.channel();					
						
						
						//ReadableByteChannel channel = Channels.newChannel(client.socket().getInputStream());
						ByteBuffer buffer = ByteBuffer.allocate(1024);
						ByteArrayBuffer data = new ByteArrayBuffer();
						int read;
						XMLReaderRPC readerRPC = new XMLReaderRPC();
						while((read = client.read(buffer)) > 0) {
							buffer.flip();
							while(buffer.hasRemaining()) {
								data.write(buffer.get());
							}							
						}					
						
						if(data.size()>0) {
							readerRPC.readDocument(data.toString());
							XMLUtils.processRPCObject(readerRPC);
							data.reset();
						}
						
						data.close();
						
						/*ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
						ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
						try {
							this.appendEntriesRPC = (AppendEntriesRPC)objectInputStream.readObject();
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}*/
						
						this.isReadyToRead = false;
						this.isReadytoWrite = false;
						System.out.println("Data Read");						
						client.socket().close();
					} else if(key.isWritable()) {
						this.isReadytoWrite = true;
						this.isReadyToRead = false;
						client = (SocketChannel) key.channel();
						ByteBuffer buf = null;
						if(IRaftConstants.INITIATOR_BCAST_MSG1 == message) {
							buf = ByteBuffer.wrap(Integer.toString(IRaftConstants.INITIATOR_BCAST_MSG1).getBytes());
						} else if(IRaftConstants.INITIATOR_BCAST_MSG2 == message) {
							buf = ByteBuffer.wrap(Integer.toString(IRaftConstants.INITIATOR_BCAST_MSG2).getBytes());
						} else if(IRaftConstants.INITIATOR_BCAST_MSG3 == message) {
							buf = ByteBuffer.wrap(Integer.toString(IRaftConstants.INITIATOR_BCAST_MSG3).getBytes());
						} else {							
							buf = ByteBuffer.wrap(Integer.toString(0).getBytes());
						}
						client.write(buf);
						//System.out.println(Thread.currentThread().getName()+"Message sent from server "+new String(buf.array()));
						//key.interestOps(SelectionKey.OP_READ);
						//client.register(selector, SelectionKey.OP_READ);
						//client.write(src)
						//client.close();
					}					
				}
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClosedChannelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				selector.close();
				server.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
