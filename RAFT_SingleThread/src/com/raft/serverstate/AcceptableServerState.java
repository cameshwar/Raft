package com.raft.serverstate;

import java.io.IOException;
import java.net.SocketException;
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

	private AcceptableServerState() {
		// TODO Auto-generated constructor stub
	}

	public static IServerStateContext getServerStateContext() {
		if (AcceptableServerState.serverState == null)
			AcceptableServerState.serverState = new AcceptableServerState();
		return AcceptableServerState.serverState;
	}

	@Override
	public void changeState(ServerStateNode server) {

		Selector selector = server.getSelector();
		SocketChannel client = null;

		try {
			selector.select();
			for (Iterator<SelectionKey> i = selector.selectedKeys().iterator(); i
					.hasNext();) {
				SelectionKey key = i.next();
				i.remove();
				if (key.isAcceptable()) {
					// accept connection
					ServerSocketChannel acceptServer = (ServerSocketChannel) key
							.channel();
					client = acceptServer.accept();
					client.configureBlocking(false);
					client.socket().setTcpNoDelay(true);
					client.register(
							selector,
							server.getServerState() == EServerState.READ ? SelectionKey.OP_READ
									: SelectionKey.OP_WRITE);
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
