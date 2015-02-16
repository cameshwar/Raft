package com.raft.machinestate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.raft.constants.EMachineState;
import com.raft.constants.EServerState;
import com.raft.constants.IRaftConstants;
import com.raft.constants.MachineState;
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
	
	private Map<Integer, ServerStateNode> servers = null;
	
	private int noOfVotes = 0;
	
	private CandidateState() {
	}
	
	public static IMachineContext getMachineContext() {
		if(candidateState == null) {
			candidateState = new CandidateState();			
		}
		readableServer = ServerUtils.getServerContext(EServerState.READ);
		writableServer = ServerUtils.getServerContext(EServerState.WRITE);
		acceptableServer = ServerUtils.getServerContext(EServerState.ACCEPT);
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
	
	private List<Thread> broadCastMsgs() {
		final ServerStateNode readServer = servers.get(ServerUtils.getMachineServerState(EMachineState.CANDIDATE, EServerState.READ));
		ServerStateNode writeServer = servers.get(ServerUtils.getMachineServerState(EMachineState.CANDIDATE, EServerState.WRITE));
		noOfVotes = 0;
		List<String> serverList = MachineState.serverList;
		final List<ClientNode> clients = new ArrayList<ClientNode>(serverList.size());
		List<Thread> threads = new ArrayList<Thread>();
		for(int index = 0 ; index<serverList.size(); index++) {
			Thread thread = new Thread()					
					{
						
						@Override
						public void interrupt() {
							// TODO Auto-generated method stub
							super.interrupt();
							System.out.println("Thread interrupted");
						}
						
						@Override
						public void run() {
							List<Integer> bCastMsgs1 = new ArrayList<Integer>();
							bCastMsgs1.add(IRaftConstants.FOLLOWER_READ_PORT);
							//bCastMsgs1.add(IRaftConstants.FOLLOWER_WRITE_PORT);
							ClientNode client = new ClientNode(bCastMsgs1, "READ");
							clients.add(client);
							new Thread(client, "Thread 1").start();
							//writableServer.changeState(writeServers.get(serverIndex));
							//System.out.println("client thread running ");
							CandidateState.readableServer.changeState(readServer);
							List<Integer> bCastMsgs2 = new ArrayList<Integer>();
							bCastMsgs2.add(IRaftConstants.CANDIDATE_READ_PORT);
							//bCastMsgs1.add(IRaftConstants.FOLLOWER_WRITE_PORT);
							new Thread(new ClientNode(bCastMsgs2, "READ"), "Thread 1").start();
							while(!CandidateState.readableServer.isReady());
							noOfVotes++;
							System.out.println("votes "+noOfVotes);
						}
					};
			threads.add(thread);
			thread.start();
		}
		return threads;
	}

	@Override
	public void process(Map<Integer, ServerStateNode> servers) {
		this.servers = servers;
		// TODO Auto-generated method stub
		//setServerStates();
		TimerThread timer = new TimerThread(IRaftConstants.CANDIDATE_TIMEOUT);
		new Thread(timer,"Timer Candidate").start();
		List<Thread> threads = null;
		while(true) {
			if(threads == null)
				threads= broadCastMsgs();
			else {
				for(Thread thread: threads)
					thread.interrupt();
				threads.clear();
			}
			threads = broadCastMsgs();
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
		MachineState.setMachineState(EMachineState.FOLLOWER);
		//resetServerStates();
	}

}
