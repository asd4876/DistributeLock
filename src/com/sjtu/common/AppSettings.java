package com.sjtu.common;

import java.util.Arrays;
import java.util.List;

/**
 * Common Settings.
 * 
 * Author: Devil
 * Created on: Jun 25, 2017 1:34:13 PM
 */

public class AppSettings {
	
	public static final String LEADER_IP = "127.0.0.1";
	public static final int LEADER_PORT = 1111;
	
	public static final List<String> FOLLOWER_IP_LIST = Arrays.asList("127.0.0.1", "127.0.0.1");
	public static final List<Integer> FOLLOWER_PORT_LIST = Arrays.asList(2222, 3333);
	
	// used for client and followers;
	public static final int CLIENT_PREEMPT = 1;
	public static final int CLIENT_RELEASE = 2;
	public static final int CLIENT_CHECK_OWNER = 3;
	public static final int CLIENT_CONNECT = 4;
	public static final int CLIENT_STOP = 9;
	
	// used for leader;
	public static final int LEADER_PREEMPT = 21;
	public static final int LEADER_RELEASE = 22;
	public static final int LEADER_STOP = 29;
	
	// used for feedback;
	public static final int SUCCESS = 200;
	public static final int FAIL = 404;
	
	
	
}
