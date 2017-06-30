package com.sjtu.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import com.sjtu.client.Client;

/**
 * Test client with user input.
 * 
 * Author: Devil
 * Created on: Jun 25, 2017 8:56:37 PM
 */

public class ClientTest {

	private static HashMap<String, Client> cMap = new HashMap<String, Client>();
	private static Client current = null;
	private static boolean isRunning = true;
	
	public static void help(){
		System.out.println("Usage:");
		System.out.println("1.Start a client and use it: start [client_name] [ip] [port]");
		System.out.println("2.Use a started client: use [client_name]");
		System.out.println("3.try lock a key: lock [key]");
		System.out.println("4.try unlock a key: unlock [key]");
		System.out.println("5.check if own a key: check [key]");
	}
	
	public static void main(String[] args) {
		
		System.out.println("Distribute Lock Test, type help for details.");
		System.out.println("Starting default client:");
		
		current = new Client("127.0.0.1", 1111);
		cMap.put("default", current);
		
		while(isRunning){
            System.out.print("Enter: "); 
            String input = null;
			try {
				input = new BufferedReader(new InputStreamReader(System.in)).readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            String [] slist = input.split(" ");
            String type = slist[0];
            if(type.equals("start")){
            	System.out.println("Start client "+slist[1]+":");
            	current = new Client(slist[2], Integer.parseInt(slist[3]));
            	cMap.put(slist[1], current);
            }
            else if(type.equals("use")){
            	if(cMap.containsKey(slist[1])){
            		System.out.println("Use client "+slist[1]+" successfully.");
            		current = cMap.get(slist[1]);
            	}
            	else{
            		System.out.println("Client "+slist[1]+" unknown.");
            	}
            }
            else if(type.equals("lock")){
            	boolean result = current.TryLock(slist[1]);
            	if(result){
            		System.out.println("Lock Success!");
            	}
            	else{
            		System.out.println("Lock Failed!");
            	}
            }
            else if(type.equals("unlock")){
            	boolean result = current.TryUnlock(slist[1]);
            	if(result){
            		System.out.println("Unlock Success!");
            	}
            	else{
            		System.out.println("Unlock Failed!");
            	}
            }
            else if(type.equals("check")){
            	boolean result = current.OwnTheLock(slist[1]);
            	if(result){
            		System.out.println("Own the lock!");
            	}
            	else{
            		System.out.println("No own the lock!");
            	}
            }
            else if(type.equals("stop")){
            	isRunning = false;
            	System.out.println("Bye.");
            	break;
            }
            else{
            	help();
            }

		}
	}

}
