package com.raft.rpc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.raft.constants.IRaftConstants;

public class AppendEntriesRPC{
	
	private long term;
	
	private String leaderId; //machine IP

	private long prevLogIndex;
	
	private int[] entries;
	
	public long getTerm() {
		return term;
	}


	public void setTerm(long term) {
		this.term = term;
	}


	public String getLeaderId() {
		return leaderId;
	}


	public void setLeaderId(String leaderId) {
		this.leaderId = leaderId;
	}


	public int[] getEntries() {
		return entries;
	}


	public void setEntries(int[] entries) {
		this.entries = entries;
	}


	public AppendEntriesRPC(Map<String, Object> valueMap) {
		processObject(valueMap);
	}
	
	
	private void processObject(Map<String, Object> valueMap) {
		Set<String> key = valueMap.keySet();
		List<Integer> entries = new ArrayList<Integer>();
		for(String keyString: key) {
			if(keyString.equals(IRaftConstants.TERM))
				this.term = Integer.parseInt((String)valueMap.get(keyString));
			else if(keyString.equals(IRaftConstants.LEADER_ID))
				this.leaderId = (String) valueMap.get(keyString);
			else if(keyString.equals(IRaftConstants.ENTRIES)) {
				List<String> stringEntries = (List<String>)valueMap.get(keyString);
				this.entries = new int[stringEntries.size()];
				for(int index = 0 ;index<stringEntries.size(); index++) {
					this.entries[index] = Integer.parseInt(stringEntries.get(index));
				}
			}
		}
	}
	
	public void display() {
		System.out.println("AppendEntriesRPC Object");
	}

}
