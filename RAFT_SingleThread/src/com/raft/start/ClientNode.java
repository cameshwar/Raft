package com.raft.start;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.raft.constants.EMachineState;
import com.raft.rpc.XMLGenerationRPC;

public class ClientNode implements Runnable {

	
	private EMachineState status;

	List<Integer> bCastMsgs = new ArrayList<Integer>();

	public ClientNode(List<Integer> bCastMsgs) {
		this.bCastMsgs = bCastMsgs;
	}

	@Override
	public void run() {

		// Step 1: Start the node in INITIATOR mode. And listen the port
		//startServer();

		// Step 2: Broadcast msgs to other Initiators after timeout. If it
		// receives adequate responses proceed to step 3
		int msgcount = 0;
		while (true) {
			for (Integer bcastMsg : this.bCastMsgs) {
				/*System.out.println(Thread.currentThread().getName()
						+ " Broadcasting Msgs " + bcastMsg);*/
				if (broadCastMessage("127.0.0.1", bcastMsg, bcastMsg) == 1)
					msgcount++;
				System.out.println("Count "+msgcount);
			}
			if (msgcount > 0) {				
				break;				
			}
			else
				continue;
		}
		
		if(this.status == EMachineState.INITIATOR) {
			this.status = EMachineState.FOLLOWER;
		}

		// Step 3: Start the node in FOLLOWER mode.

		// Step 4: Broadcast msgs to other followers after timeout. If it
		// receives adequate responses proceed to Step 5

		// Step 5: Start the node in LEADER mode.

	}

/*	private boolean startServer() {
		boolean success = false;		
		new Thread(new ServerNode(ServerUtils.creteServer(port)),"Server Thread with port "+port).start();
		success = true;
		this.status = ENodeState.INITIATOR;
		System.out.println(Thread.currentThread().getName()
				+ " Status of Server in port " + port + ": " + success);
		return success;
	}*/

	private int broadCastMessage(String machine, int port, int broadCastMsg) {

		int message = 0;
		SocketChannel client = null;
		Selector selector = null;
		try {
			 selector = Selector.open();
			 client = SocketChannel.open();
			 client.configureBlocking(false);
			 client.register(selector, SelectionKey.OP_CONNECT);
			 client.connect(new InetSocketAddress(machine, port));
									

			while(true) {
				
				selector.select();
				
				for (Iterator<SelectionKey> i = selector.selectedKeys().iterator(); i.hasNext();) {
					SelectionKey key = i.next();
					i.remove();
					
					if (key.isConnectable()){
						client = (SocketChannel) key.channel();
			            if (client.isConnectionPending()){
			                client.finishConnect();
			            }
			            client.configureBlocking(false);
			            client.register(selector, SelectionKey.OP_WRITE);
                    }else if (key.isWritable()){
                    	client = (SocketChannel) key.channel();
                    	/**
                    	 * <?xml version="1.0" ?><AppendEntriesRPC_Req>
                    	 * <term>termText</term><leader_id/>
                    	 * <entries><entry>123</entry><entry>456</entry></entries>
                    	 * </AppendEntriesRPC_Req>
                    	 */
                    	XMLGenerationRPC rpc = new XMLGenerationRPC();
                    	rpc.processRPC();
                    		
                    	
                    	/*ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
                    	ObjectOutputStream objStream = new ObjectOutputStream(byteArrayStream);
                    	AppendEntriesRPC appendRPC = new AppendEntriesRPC();
                    	objStream.writeObject(appendRPC);

                    	client.write(ByteBuffer.wrap(byteArrayStream.toByteArray()));*/
                    	System.out.println("client: "+new String(rpc.getXMLStringArray()));
                    	client.write(ByteBuffer.wrap(rpc.getXMLStringArray()));
                        //message =1;
                        //System.out.println(Thread.currentThread().getName()+"Message written from client "+Integer.toString(broadCastMsg));
                        // lets get ready to read.
                        key.interestOps(SelectionKey.OP_READ);
                    } else if (key.isReadable()){
                    	System.out.println("Client is reading");
                    	client = (SocketChannel) key.channel();
						ByteBuffer buf = ByteBuffer.allocate(4);
						buf.clear();
						int read = client.read(buf);
						if(read == -1) System.out.println("No data");
						buf.flip();
						byte[] data = new byte[4]; 
						buf.get(data, 0, read);
						message = Integer.parseInt(new String(data).trim());
						System.out.println("Message from Server "+message);
						//System.out.println(Thread.currentThread().getName()+"Message received from server "+message);
//						/key.interestOps(SelectionKey.OP_WRITE);
                    }
					
				}
				if(message>0)
					break;
				//client.socket().close();
			}
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 1;

	}

}
