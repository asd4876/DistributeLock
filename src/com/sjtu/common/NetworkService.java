package com.sjtu.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONObject;

/**
 * NetworkService.java
 * 
 * Author: Devil
 * Created on: Jun 25, 2017 2:46:16 PM
 */

public class NetworkService {
	public static void broadcast(String message){
		int num = AppSettings.FOLLOWER_IP_LIST.size();
		for(int i=0;i<num;i++){
			String ip = AppSettings.FOLLOWER_IP_LIST.get(i);
			int port = AppSettings.FOLLOWER_PORT_LIST.get(i);
			
			String feedback = sendMessage(ip, port, message);
			int code = new JSONObject(feedback).getInt("type");
			
			if(code != AppSettings.SUCCESS){
				System.out.println("Error: Broadcast to Server ["+ip+":"+port+"] failed!");
			}
		}
	}
	
	public static String sendMessage(String ip, int port, String message){
		
		String feedback = null;
		
		try {
			
			Socket socket = new Socket(ip, port);
			
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			out.println(message);
			
			
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			feedback = in.readLine();
			
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			System.out.println("Error: Send Message to Server ["+ip+":"+port+"] failed!");
			e.printStackTrace();
		} 
		return feedback;
	}

}
