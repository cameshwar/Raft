package com.raft.constants;

public interface IRaftConstants {
	
	public static final int INITIATOR_PORT1 = 6060;
	
	public static final int INITIATOR_PORT2 = 6061;
	
	public static final int INITIATOR_PORT3 = 6062;
	
	public static final int INITIATOR_PORT = 6061;
	
	public static final int FOLLOWER_PORT = 6070;
	
	public static final int CANDIDATE_PORT = 6080;
	
	public static final int LEADER_PORT = 6090;
	
	public static final int INITIATOR_BCAST_MSG1 = 6060;
	
	public static final int INITIATOR_BCAST_MSG2 = 6061;
	
	public static final int INITIATOR_BCAST_MSG3 = 6062;
	
	public static final long FOLLOWER_TIMEOUT = 5000L;
	
	public static final long CANDIDATE_TIMEOUT = 8000L;
	
	public static final long LEADER_TIMEOUT = 10000L;
	
	public static final long GRACE_TIMEOUT = 2000L;

}
