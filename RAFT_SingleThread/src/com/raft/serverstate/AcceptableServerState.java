package com.raft.serverstate;

import java.io.IOException;
import java.net.SocketException;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import com.raft.constants.EServerState;
import com.raft.start.ServerStateNode;
import com.sun.xml.internal.ws.util.ByteArrayBuffer;

public class AcceptableServerState implements IServerStateContext {

	private static AcceptableServerState serverState = null;

	private boolean ready = false;
	
	private boolean close = false;

	private AcceptableServerState() {
		// TODO Auto-generated constructor stub
	}

	public static IServerStateContext getServerStateContext() {
		if (AcceptableServerState.serverState == null)
			AcceptableServerState.serverState = new AcceptableServerState();
		return AcceptableServerState.serverState;
	}

	@Override
	public void changeState(final ServerStateNode server) {		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Selector selector = server.getSelector();
					SocketChannel client = null;
					selector.select();
					for (Iterator<SelectionKey> i = selector.selectedKeys().iterator(); i
							.hasNext();) {				
						SelectionKey key = i.next();
						i.remove();				
						if (key.isValid() && key.isAcceptable()) {
							// accept connection
							ServerSocketChannel acceptServer = (ServerSocketChannel) key
									.channel();	
							if(close) {
								close = false;
								if(client!=null) client.close();
								break;
							}							
							client = acceptServer.accept();
							client.configureBlocking(false);
							client.socket().setTcpNoDelay(true);
							client.register(
									selector,
									server.getServerState() == EServerState.READ ? SelectionKey.OP_READ
											: SelectionKey.OP_WRITE);							
						}
					}
				} /*catch(CancelledKeyException e){
					
				}*/ catch (SocketException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				} catch (ClosedChannelException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
				
			}
		}).start();
				
		
	}

	@Override
	public boolean isReady() {
		return ready;
	}

	@Override
	public void processData(ByteArrayBuffer buf) {
		// TODO Auto-generated method stub

	}

	@Override
	public void closeConnection(ServerStateNode server) {
		this.close = true;
		while(!close);
		server.destroy();	
		
	}

}
