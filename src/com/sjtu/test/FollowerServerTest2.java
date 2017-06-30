package com.sjtu.test;

import com.sjtu.server.Server;

/**
 * Follower server test.
 * 
 * Author: Devil
 * Created on: Jun 25, 2017 4:49:52 PM
 */

public class FollowerServerTest2 {

	public static void main(String[] args) {
		Server followerServer = new Server(3333, false);
		followerServer.init();
	}

}
