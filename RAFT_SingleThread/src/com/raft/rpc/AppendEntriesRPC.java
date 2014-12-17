package com.raft.rpc;

import java.io.Serializable;

public class AppendEntriesRPC implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long term;
	
	private String leaderId; //machine IP

	private long prevLogIndex;
	
	
	public void display() {
		System.out.println("AppendEntriesRPC Object");
	}

}
