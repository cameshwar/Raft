package com.raft.constants;

import java.util.Map;

import com.raft.machinestate.IMachineContext;
import com.raft.start.ServerStateNode;
import com.raft.start.StartServer;
import com.raft.utils.ServerUtils;

public enum EMachineState {
	INITIATOR {
		@Override
		public boolean process(IMachineContext machineContext) {
			System.out.println("Machine State: Initiator");
			Map<Integer, ServerStateNode> servers = ServerUtils.createReadWriteServers(INITIATOR);
			StartServer.getServer().startServer(servers);
			machineContext.process(servers);
			ServerUtils.destroyReadWriteServers(INITIATOR, servers);			
			//machineContext.setState(FOLLOWER);
			return true;
		}
	}, 
	FOLLOWER {
		@Override
		public boolean process(IMachineContext machineContext) {
			System.out.println("Machine State: Follower");
			Map<Integer, ServerStateNode> servers = ServerUtils.createReadWriteServers(FOLLOWER);
			StartServer.getServer().startServer(servers);
			machineContext.process(servers);			
			ServerUtils.destroyReadWriteServers(FOLLOWER, servers);
			//MachineState.setState(CANDIDATE);
			return true;
		}
	}, 
	CANDIDATE {
		@Override
		public boolean process(IMachineContext machineContext) {
			System.out.println("Machine State: Candidate");
			Map<Integer, ServerStateNode> servers = ServerUtils.createReadWriteServers(CANDIDATE);
			StartServer.getServer().startServer(servers);
			machineContext.process(servers);
			ServerUtils.destroyReadWriteServers(CANDIDATE, servers);
			//MachineState.setState(LEADER);
			//MachineState.setState(FOLLOWER);
			return true;
		}
	}, 
	LEADER {
		@Override
		public boolean process(IMachineContext machineContext) {
			System.out.println("Machine State: Leader");
			Map<Integer, ServerStateNode> servers = ServerUtils.createReadWriteServers(LEADER);
			StartServer.getServer().startServer(servers);
			machineContext.process(servers);
			ServerUtils.destroyReadWriteServers(LEADER, servers);
			//MachineState.setState(FOLLOWER);
			return true;
		}
	};
	
	public abstract boolean process(IMachineContext machineContext);
}
