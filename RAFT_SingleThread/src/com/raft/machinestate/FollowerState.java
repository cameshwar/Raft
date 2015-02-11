package com.raft.machinestate;

import java.util.Map;

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
		}
		readableServer = ServerUtils.getServerContext(EServerState.READ);
		writableServer = ServerUtils.getServerContext(EServerState.WRITE);
		return followerState;
	}
	
	private void setServerStates() {
		this.readableServer = ServerUtils.getServerContext(EServerState.READ);
		this.writableServer = ServerUtils.getServerContext(EServerState.WRITE);
	}
	
	private void resetServerStates() {
		this.readableServer = null;
		this.writableServer = null;
		//this.acceptableServer = null;
	}
	
	
	private AppendEntriesRPC processRawData(ByteArrayBuffer rawData) {
		AppendEntriesRPC appendEntriesRPC = null;
		if(rawData.size()>0) {
			XMLReaderRPC readerRPC = new XMLReaderRPC();
			
			readerRPC.readDocument(rawData.toString());
			readerRPC.processRPC();
			appendEntriesRPC = new AppendEntriesRPC(readerRPC.getValueMap());
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
	public void process(Map<Integer, ServerStateNode> servers) {
		//setServerStates();
		TimerThread timer = new TimerThread(IRaftConstants.FOLLOWER_TIMEOUT);
		new Thread(timer,"Timer Follower").start();
		
		ServerStateNode readServer = servers.get(ServerUtils.getMachineServerState(EMachineState.FOLLOWER, EServerState.READ));
		ServerStateNode writeServer = servers.get(ServerUtils.getMachineServerState(EMachineState.FOLLOWER, EServerState.WRITE));
		
		FollowerState.readableServer.changeState(readServer);		
		while(!FollowerState.readableServer.isReady()) {			
			if(timer.isTimeOut()) {
				MachineState.setMachineState(EMachineState.CANDIDATE);
				timer.setShutTimer(true);
				return;
			}
		}
		timer.setResetTimer(true);
		ByteArrayBuffer readData = new ByteArrayBuffer();
		FollowerState.readableServer.processData(readData);
		AppendEntriesRPC appendEntriesRPC = processRawData(readData);
		FollowerState.writableServer.changeState(writeServer);
		timer.setResetTimer(false);
		
		while(!timer.isTimeOut());
		timer.setShutTimer(true);	
		MachineState.setMachineState(EMachineState.CANDIDATE);
		//resetServerStates();
	}

}
