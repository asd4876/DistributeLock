package com.sjtu.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;

import com.sjtu.common.AppSettings;
import com.sjtu.common.NetworkService;

/**
 * Server.java
 * 
 * Author: Devil
 * Created on: Jun 25, 2017 3:21:43 PM
 */

public class Server {

	boolean isLeader = false;
	int port;
	boolean isRunning = false;
	private ConcurrentHashMap<String, String> lockMap = new ConcurrentHashMap<String, String>();
	
	
	public Server(int port, boolean isLeader){
		this.port = port;
		this.isLeader = isLeader;
		this.isRunning = true;
		System.out.println("Server ["+port+"] started!");
	}
	
	public void init() {     
		try{
			ServerSocket serverSocket = new ServerSocket(port);
			while (isRunning) {
				Socket client = serverSocket.accept();
				System.out.println("A new socket is connected!");
				new HandlerThread(client);
			}
			serverSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }    
	
	private String messageProcess(String message){
		JSONObject object = new JSONObject(message);
		int type = object.getInt("type");
		String key = object.getString("key");
		String client = object.getString("client");
		switch(type){
		case AppSettings.CLIENT_CHECK_OWNER:
			if(checkOwner(key, client)){
				object.put("type", AppSettings.SUCCESS);
			}
			else{
				object.put("type", AppSettings.FAIL);
			}
			break;
		case AppSettings.CLIENT_PREEMPT:
			if(isLeader){
				if(checkOwner(key, null)){
					lockMap.put(key, client);
					object.put("type", AppSettings.LEADER_PREEMPT);
					NetworkService.broadcast(object.toString());
					object.put("type", AppSettings.SUCCESS);
				}
				else{
					object.put("type", AppSettings.FAIL);
				}
			}
			else{
				return NetworkService.sendMessage(AppSettings.LEADER_IP, AppSettings.LEADER_PORT, message);
			}
			break;
		case AppSettings.CLIENT_RELEASE:
			if(isLeader){
				if(checkOwner(key, client)){
					lockMap.remove(key);
					object.put("type", AppSettings.LEADER_RELEASE);
					NetworkService.broadcast(object.toString());
					object.put("type", AppSettings.SUCCESS);
				}
				else{
					object.put("type", AppSettings.FAIL);
				}
			}
			else{
				return NetworkService.sendMessage(AppSettings.LEADER_IP, AppSettings.LEADER_PORT, message);
			}
			break;
		case AppSettings.CLIENT_STOP:
			if(isLeader){
				isRunning = false;
				object.put("type", AppSettings.LEADER_STOP);
				NetworkService.broadcast(object.toString());
				object.put("type", AppSettings.SUCCESS);
			}
			else{
				return NetworkService.sendMessage(AppSettings.LEADER_IP, AppSettings.LEADER_PORT, message);
			}
			break;
		case AppSettings.CLIENT_CONNECT:
			object.put("type", AppSettings.SUCCESS);
			break;
		case AppSettings.LEADER_PREEMPT:
			lockMap.put(key, client);
			object.put("type", AppSettings.SUCCESS);
			break;
		case AppSettings.LEADER_RELEASE:
			lockMap.remove(key);
			object.put("type", AppSettings.SUCCESS);
			break;
		case AppSettings.LEADER_STOP:
			isRunning = false;
			object.put("type", AppSettings.SUCCESS);
			break;
		default:
			System.out.println("Error: unknown message type");
			break;
		}
		return object.toString();
	}
	
	public boolean checkOwner(String key, String client){
		String value = lockMap.get(key);
		// preempt
		if(client == null && value == null){
			return true;
		}
		// check owner
		if(client != null && value != null && client.equals(value)){
			return true;
		}
		return false;
	}

	

    private class HandlerThread implements Runnable {    
        private Socket socket;    
        public HandlerThread(Socket client) {    
            socket = client;    
            new Thread(this).start();    
        }    

        public void run() {    
            try {   
            	BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            	String message = in.readLine();
            	
            	System.out.println("get: "+ message);
            	String feedback = messageProcess(message);
            	System.out.println("send: "+ feedback);
            	System.out.println("keys: "+ lockMap);
            	
            	PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            	out.println(feedback);
            	out.close(); 
            	in.close();
          
                socket.close();
            } catch (Exception e) {    
                e.printStackTrace();  
            } 
        }    
    }    

}
