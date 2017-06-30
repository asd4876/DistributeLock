package com.sjtu.test;

import com.sjtu.server.Server;

/**
 * Follower server test.
 * 
 * Author: Devil
 * Created on: Jun 25, 2017 4:47:02 PM
 */

public class FollowerServerTest1 {
	
	public static void main(String[] args) {
		Server followerServer = new Server(2222, false);
		followerServer.init();
	}
	
}
