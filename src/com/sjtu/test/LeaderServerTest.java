package com.sjtu.test;

import com.sjtu.server.Server;

/**
 * Leader server test.
 * 
 * Author: Devil
 * Created on: Jun 25, 2017 4:46:13 PM
 */

public class LeaderServerTest {

	public static void main(String[] args) {
		Server leaderServer = new Server(1111,true);
		leaderServer.init();
	}

}
