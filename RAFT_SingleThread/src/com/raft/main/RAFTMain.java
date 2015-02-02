package com.raft.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.raft.constants.EServerState;
import com.raft.constants.IRaftConstants;
import com.raft.constants.MachineState;
import com.raft.start.ClientNode;
import com.raft.start.ServerStateNode;
import com.raft.start.StartRaft;
import com.raft.start.StartServer;
import com.raft.utils.ServerUtils;

public class RAFTMain {
	public static void main(String[] args) {

		/**
		 * Algorithm:
		 * 
		 * Step1: Start the nodes listening in the INITIATOR, CANDIDATE,
		 * FOLLOWER and LEADER
		 * 
		 * Step2: Algorithm logic. Here, we have to make a switch over to the
		 * port based on the logic.
		 * 
		 * 
		 */
		// Step1:
		/*
		 * Map<Integer, ServerNode> portServerMap = new HashMap<Integer,
		 * ServerNode>(); portServerMap.put(IRaftConstants.INITIATOR_PORT, new
		 * ServerNode(ServerUtils.creteServer(IRaftConstants.INITIATOR_PORT)));
		 * portServerMap.put(IRaftConstants.FOLLOWER_PORT, new
		 * ServerNode(ServerUtils.creteServer(IRaftConstants.FOLLOWER_PORT)));
		 * portServerMap.put(IRaftConstants.CANDIDATE_PORT, new
		 * ServerNode(ServerUtils.creteServer(IRaftConstants.CANDIDATE_PORT)));
		 * portServerMap.put(IRaftConstants.LEADER_PORT, new
		 * ServerNode(ServerUtils.creteServer(IRaftConstants.LEADER_PORT)));
		 * System.out.println("Starting Server ");
		 * StartServer.getServer().startServer(portServerMap);
		 */
		Map<Integer, ServerStateNode> portServerMap = new HashMap<Integer, ServerStateNode>();
		portServerMap.put(
				IRaftConstants.INITIATOR_READ_PORT,
				new ServerStateNode(ServerUtils
						.creteServer("127.0.0.1",IRaftConstants.INITIATOR_READ_PORT),
						"Init Read", EServerState.READ));
		portServerMap.put(
				IRaftConstants.INITIATOR_WRITE_PORT,
				new ServerStateNode(ServerUtils
						.creteServer("127.0.0.1",IRaftConstants.INITIATOR_WRITE_PORT),
						"Init write", EServerState.WRITE));
		portServerMap.put(
				IRaftConstants.FOLLOWER_READ_PORT,
				new ServerStateNode(ServerUtils
						.creteServer("127.0.0.1",IRaftConstants.FOLLOWER_READ_PORT),
						"Foll Read", EServerState.READ));
		portServerMap.put(
				IRaftConstants.FOLLOWER_WRITE_PORT,
				new ServerStateNode(ServerUtils
						.creteServer("127.0.0.1",IRaftConstants.FOLLOWER_WRITE_PORT),
						"Foll write", EServerState.WRITE));
		portServerMap.put(
				IRaftConstants.CANDIDATE_READ_PORT,
				new ServerStateNode(ServerUtils
						.creteServer("127.0.0.1",IRaftConstants.CANDIDATE_READ_PORT),
						"Cand Read", EServerState.READ));
		portServerMap.put(
				IRaftConstants.CANDIDATE_WRITE_PORT,
				new ServerStateNode(ServerUtils
						.creteServer("127.0.0.1",IRaftConstants.CANDIDATE_WRITE_PORT),
						"Cand write", EServerState.WRITE));
		portServerMap.put(
				IRaftConstants.LEADER_READ_PORT, 
				new ServerStateNode(ServerUtils.
						creteServer("127.0.0.1",IRaftConstants.LEADER_READ_PORT),
				"Lead Read", EServerState.READ));
		portServerMap.put(
				IRaftConstants.LEADER_WRITE_PORT,
				new ServerStateNode(ServerUtils
						.creteServer("127.0.0.1",IRaftConstants.LEADER_WRITE_PORT),
						"Lead write", EServerState.WRITE));
		System.out.println("Starting Server ");
		StartServer.getServer().startServer(portServerMap);
		
		List<String> serverList = new ArrayList<String>();
		serverList.add("127.0.0.1");
		//serverList.add("localhost");
		MachineState.serverList = serverList;
		
		// Step2:
		List<String> nodeList = new ArrayList<String>();
		MachineState.initializeMachineState(nodeList, portServerMap);
		System.out.println("Starting Raft");
		new Thread(new StartRaft()).start();

		/*
		 * System.out.println("Starting server Thread "+IRaftConstants.
		 * INITIATOR_PORT1);
		 */
		/*List<Integer> bCastMsgs1 = new ArrayList<Integer>();
		bCastMsgs1.add(IRaftConstants.FOLLOWER_READ_PORT);
		//bCastMsgs1.add(IRaftConstants.FOLLOWER_WRITE_PORT);
		new Thread(new ClientNode(bCastMsgs1, "READ"), "Thread 1").start();
		
		List<Integer> bCastMsgs2 = new ArrayList<Integer>();
		bCastMsgs2.add(IRaftConstants.CANDIDATE_WRITE_PORT);
		new Thread(new ClientNode(bCastMsgs2, "WRITE"), "Thread 2").start();*/
		/*
		 * System.out.println("Starting server Thread "+IRaftConstants.
		 * INITIATOR_PORT2); List<Integer> bCastMsgs2 = new
		 * ArrayList<Integer>();
		 * bCastMsgs2.add(IRaftConstants.INITIATOR_BCAST_MSG1);
		 * bCastMsgs2.add(IRaftConstants.INITIATOR_BCAST_MSG3); new Thread(new
		 * ClientNode(IRaftConstants.INITIATOR_PORT2,
		 * bCastMsgs2),"Thread 2").start();
		 * 
		 * System.out.println("Starting server Thread "+IRaftConstants.
		 * INITIATOR_PORT3); List<Integer> bCastMsgs3 = new
		 * ArrayList<Integer>();
		 * bCastMsgs3.add(IRaftConstants.INITIATOR_BCAST_MSG1);
		 * bCastMsgs3.add(IRaftConstants.INITIATOR_BCAST_MSG2); new Thread(new
		 * ClientNode(IRaftConstants.INITIATOR_PORT3,
		 * bCastMsgs3),"Thread 3").start();
		 */
	}
}
