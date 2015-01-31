package com.raft.main;

import java.util.ArrayList;
import java.util.List;

import com.raft.constants.IRaftConstants;
import com.raft.start.ClientNode;

public class ClientWriteMain {

	public static void main(String[] args) {
		List<Integer> bCastMsgs1 = new ArrayList<Integer>();
		bCastMsgs1.add(IRaftConstants.FOLLOWER_READ_PORT);
		//bCastMsgs1.add(IRaftConstants.FOLLOWER_WRITE_PORT);
		new Thread(new ClientNode(bCastMsgs1, "READ"), "Thread 1").start();
		
		/*List<Integer> bCastMsgs2 = new ArrayList<Integer>();
		bCastMsgs2.add(IRaftConstants.CANDIDATE_WRITE_PORT);
		new Thread(new ClientNode(bCastMsgs2, "WRITE"), "Thread 2").start();*/
	}
	
}
