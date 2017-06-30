package com.sjtu.client;

import java.util.UUID;

import org.json.JSONObject;

import com.sjtu.common.AppSettings;
import com.sjtu.common.NetworkService;

/**
 * Client.java
 * 
 * Author: Devil
 * Created on: Jun 25, 2017 7:12:11 PM
 */

public class Client {
	
	private String id;
	private String ip;
	private int port;
	private JSONObject obj;
	
	public Client(String ip, int port){
		this.id = UUID.randomUUID().toString();
		this.ip = ip;
		this.port = port;
		this.obj = new JSONObject();
		obj.put("client", this.id);
		obj.put("key", "");
		obj.put("type", AppSettings.CLIENT_CONNECT);
		System.out.println("Client startup, connecting to server ["+ip+":"+port+"] ...");
		String feedback = NetworkService.sendMessage(ip, port, obj.toString());
		int code = new JSONObject(feedback).getInt("type");
		if(code==AppSettings.SUCCESS){
			System.out.println("Connected Successfully!");
		}
		else{
			System.out.println("Connected Failed!");
		}
	}
	
	public boolean TryLock(String key){
		obj.put("type", AppSettings.CLIENT_PREEMPT);
		obj.put("key", key);
		String feedback = NetworkService.sendMessage(ip, port, obj.toString());
		int code = new JSONObject(feedback).getInt("type");
		if(code==AppSettings.SUCCESS){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean TryUnlock(String key){
		obj.put("type", AppSettings.CLIENT_RELEASE);
		obj.put("key", key);
		String feedback = NetworkService.sendMessage(ip, port, obj.toString());
		int code = new JSONObject(feedback).getInt("type");
		if(code==AppSettings.SUCCESS){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean OwnTheLock(String key) {
		obj.put("type", AppSettings.CLIENT_CHECK_OWNER);
		obj.put("key", key);
		String feedback = NetworkService.sendMessage(ip, port, obj.toString());
		int code = new JSONObject(feedback).getInt("type");
		if (code == AppSettings.SUCCESS) {
			return true;
		} else {
			return false;
		}
	}

}
