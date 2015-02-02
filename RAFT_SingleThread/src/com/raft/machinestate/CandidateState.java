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
	
	private IServerStateContext readableServer = null;
	
	private IServerStateContext writableServer = null;
	
	private IServerStateContext acceptableServer = null;
	
	private int noOfVotes = 0;
	
	private CandidateState() {
	}
	
	public static IMachineContext getMachineContext() {
		if(candidateState == null) {
			candidateState = new CandidateState();			
		}
		return candidateState;
	}
	
	private void setServerStates() {
		this.readableServer = ServerUtils.getServerContext(EServerState.READ);
		this.writableServer = ServerUtils.getServerContext(EServerState.WRITE);
		this.acceptableServer = ServerUtils.getServerContext(EServerState.ACCEPT);
	}
	
	private void resetServerStates() {
		this.readableServer = null;
		this.writableServer = null;
		this.acceptableServer = null;
	}
	
	private void broadCastMsgs() {
		noOfVotes = 0;
		List<String> serverList = MachineState.serverList;
		final List<ClientNode> clients = new ArrayList<ClientNode>(serverList.size());
		for(int index = 0 ; index<serverList.size(); index++) {
			final int serverIndex = index;
			new Thread(
					new Runnable() {
						
						@Override
						public void run() {
							List<Integer> bCastMsgs1 = new ArrayList<Integer>();
							bCastMsgs1.add(IRaftConstants.FOLLOWER_READ_PORT);
							//bCastMsgs1.add(IRaftConstants.FOLLOWER_WRITE_PORT);
							/*ClientNode client = new ClientNode(bCastMsgs1, "READ");
							clients.add(client);
							new Thread(client, "Thread 1").start();*/
							//writableServer.changeState(writeServers.get(serverIndex));
							//System.out.println("client thread running ");
							readableServer.changeState(ServerUtils.getServerNode(EServerState.READ));
							List<Integer> bCastMsgs2 = new ArrayList<Integer>();
							bCastMsgs2.add(IRaftConstants.CANDIDATE_READ_PORT);
							//bCastMsgs1.add(IRaftConstants.FOLLOWER_WRITE_PORT);
							new Thread(new ClientNode(bCastMsgs2, "READ"), "Thread 1").start();
							while(!readableServer.isReady());
							noOfVotes++;
							System.out.println("votes "+noOfVotes);
						}
					}
					).start();			
		}		
	}

	@Override
	public void process() {
		// TODO Auto-generated method stub
		System.out.println("candidate");
		setServerStates();
		TimerThread timer = new TimerThread(IRaftConstants.CANDIDATE_TIMEOUT);
		new Thread(timer,"Timer Candidate").start();
		
		while(true) {
			broadCastMsgs();
			while(!timer.isTimeOut());
			if(noOfVotes>=1)
				break;
			else {
				timer.setResetTimer(true);
				timer.setResetTimer(false);
				continue;
			}
		}
		timer.setShutTimer(true);
		System.out.println("Promoting");
		MachineState.setMachineState(EMachineState.LEADER);
		resetServerStates();
	}

}
