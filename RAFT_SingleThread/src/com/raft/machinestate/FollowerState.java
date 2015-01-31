package com.raft.machinestate;

import com.raft.constants.EMachineState;
import com.raft.constants.EServerState;
import com.raft.constants.IRaftConstants;
import com.raft.constants.MachineState;
import com.raft.rpc.AppendEntriesRPC;
import com.raft.rpc.XMLReaderRPC;
import com.raft.serverstate.IServerStateContext;
import com.raft.start.ServerStateNode;
import com.raft.timer.TimerThread;
import com.raft.utils.ServerUtils;
import com.sun.xml.internal.ws.util.ByteArrayBuffer;

public class FollowerState implements IMachineContext{
	
	private static FollowerState followerState = null;
	
	private static IServerStateContext readableServer = null;
	
	private static IServerStateContext writableServer = null;
	
	private FollowerState() {
	}
	
	public static IMachineContext getMachineContext() {
		if(followerState == null) {
			followerState = new FollowerState();
			setServerStates();
		}
		return followerState;
	}
	
	private static void setServerStates() {
		readableServer = ServerUtils.getServerContext(EServerState.READ);
		writableServer = ServerUtils.getServerContext(EServerState.WRITE);
	}
	
	private AppendEntriesRPC processRawData(ByteArrayBuffer rawData) {
		AppendEntriesRPC appendEntriesRPC = null;
		if(rawData.size()>0) {
			XMLReaderRPC readerRPC = new XMLReaderRPC();
			
			readerRPC.readDocument(rawData.toString());
			readerRPC.processRPC();
			System.out.println("RPC Processed");
			appendEntriesRPC = new AppendEntriesRPC(readerRPC.getValueMap());
			System.out.println("RPC Obj Created");
			rawData.reset();
			/*try {
				rawData.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
		return appendEntriesRPC;
	}
	
	
	@Override
	public void process() {
		
		System.out.println("Context: Follower");
		TimerThread timer = new TimerThread(IRaftConstants.FOLLOWER_TIMEOUT);
		new Thread(timer,"Timer Follower").start();
		
		ServerStateNode server = ServerUtils.getServerNode(EServerState.READ);
		
		readableServer.changeState(server);		
		while(!readableServer.isReady()) {			
			if(timer.isTimeOut()) {
				MachineState.setMachineState(EMachineState.CANDIDATE);
				return;
			}
		}
		timer.setResetTimer(true);
		ByteArrayBuffer readData = new ByteArrayBuffer();
		readableServer.processData(readData);
		AppendEntriesRPC appendEntriesRPC = processRawData(readData);
		writableServer.changeState(ServerUtils.getServerNode(EServerState.WRITE));
		timer.setResetTimer(false);
		
		while(!timer.isTimeOut());
		timer.setShutTimer(true);	
		MachineState.setMachineState(EMachineState.CANDIDATE);
	}

}
