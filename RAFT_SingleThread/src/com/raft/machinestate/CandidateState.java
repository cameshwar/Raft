package com.raft.machinestate;

import java.util.ArrayList;
import java.util.List;

import com.raft.constants.EMachineState;
import com.raft.constants.EServerState;
import com.raft.constants.IRaftConstants;
import com.raft.constants.MachineState;
import com.raft.serverstate.AcceptableServerState;
import com.raft.serverstate.IServerStateContext;
import com.raft.start.ClientNode;
import com.raft.start.ServerStateNode;
import com.raft.timer.TimerThread;
import com.raft.utils.ServerUtils;

public class CandidateState implements IMachineContext{
	
	private static CandidateState candidateState = null;
	
	private static IServerStateContext readableServer = null;
	
	private static IServerStateContext writableServer = null;
	
	private static IServerStateContext acceptableServer = null;
	
	private int noOfVotes = 0;
	
	private CandidateState() {
	}
	
	public static IMachineContext getMachineContext() {
		if(candidateState == null) {
			candidateState = new CandidateState();
			setServerStates();
		}
		return candidateState;
	}
	
	private static void setServerStates() {
		readableServer = ServerUtils.getServerContext(EServerState.READ);
		writableServer = ServerUtils.getServerContext(EServerState.WRITE);
		acceptableServer = ServerUtils.getServerContext(EServerState.ACCEPT);
	}
	
	private void broadCastMsgs() {
		List<String> serverList = MachineState.serverList;
		for(int index = 0 ; index<serverList.size(); index++) {
			final int serverIndex = index;
			new Thread(
					new Runnable() {
						
						@Override
						public void run() {
							List<Integer> bCastMsgs1 = new ArrayList<Integer>();
							bCastMsgs1.add(IRaftConstants.FOLLOWER_READ_PORT);
							//bCastMsgs1.add(IRaftConstants.FOLLOWER_WRITE_PORT);
							ClientNode client = new ClientNode(bCastMsgs1, "READ");
							new Thread(client, "Thread 1").start();
							//writableServer.changeState(writeServers.get(serverIndex));							
						}
					}
					).start();
			System.out.println("client thread running ");
			readableServer.changeState(ServerUtils.getServerNode(EServerState.READ));
			while(!readableServer.isReady());
			noOfVotes++;
			System.out.println("votes "+noOfVotes);
		}		
	}

	@Override
	public void process() {
		// TODO Auto-generated method stub
		System.out.println("candidate");

		TimerThread timer = new TimerThread(IRaftConstants.CANDIDATE_TIMEOUT);
		new Thread(timer,"Timer Candidate").start();
		
		while(true) {
			broadCastMsgs();
			if(noOfVotes>=1)
				break;
			if(timer.isTimeOut())
				continue;
		}
		
		MachineState.setMachineState(EMachineState.LEADER);
		
	}

}
