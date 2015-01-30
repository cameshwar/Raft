package com.raft.constants;

public interface IRaftConstants {
	
	public static final int INITIATOR_PORT1 = 6060;
	
	public static final int INITIATOR_PORT2 = 6061;
	
	public static final int INITIATOR_PORT3 = 6062;
	
	public static final int INITIATOR_READ_PORT = 6060;
	
	public static final int INITIATOR_WRITE_PORT = 6061;
	
	public static final int FOLLOWER_READ_PORT = 6070;
	
	public static final int FOLLOWER_WRITE_PORT = 6071;
	
	public static final int CANDIDATE_READ_PORT = 6080;
	
	public static final int CANDIDATE_WRITE_PORT = 6081;
	
	public static final int LEADER_READ_PORT = 6090;
	
	public static final int LEADER_WRITE_PORT = 6091;
	
	public static final int INITIATOR_BCAST_MSG1 = 6060;
	
	public static final int INITIATOR_BCAST_MSG2 = 6061;
	
	public static final int INITIATOR_BCAST_MSG3 = 6062;
	
	public static final long FOLLOWER_TIMEOUT = 5000L;
	
	public static final long CANDIDATE_TIMEOUT = 8000L;
	
	public static final long LEADER_TIMEOUT = 10000L;
	
	public static final long GRACE_TIMEOUT = 2000L;
	
	public static final String APPEND_ENTRIES_RPC = "AppendEntriesRPC_Req";
	
	public static final String TERM = "term";
	
	public static final String LEADER_ID = "leader_id";
	
	public static final String ENTRIES = "entries";
	
	public static final String ENTRY = "entry";
	
	public static final String REQUEST_VOTES_RPC = "RequestVotesRPC_Req";

}
