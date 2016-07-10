package com.miner;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class NetSpeaker implements Runnable {
	
	Socket socket = null;
	DataInputStream in;
	DataOutputStream out;
	BufferedReader keyboard;
	boolean running;
	public NetSpeaker ns;
	
	public NetSpeaker(){
		running = true;
		try {
			socket = new Socket("uk1.ghash.io", 3333);
			//socket = new Socket("http://[stratum+tcp://uk1.ghash.io]", 3333);
	        InputStream sin = socket.getInputStream();
	        OutputStream sout = socket.getOutputStream();
	        in = new DataInputStream(sin);
	        out = new DataOutputStream(sout);
	        keyboard = new BufferedReader(new InputStreamReader(System.in));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void speak() {
		//run listener
		ns = new NetSpeaker();
		ns.ns = ns;
		Thread thread = new Thread(ns);
		thread.start();
		//send request
		synchronized (ns) {
			ns.SendRequest("autorize");
			ns.SendRequest("subscribe");
		}
		try {
			while (ns.SendRequest(keyboard.readLine())) {		}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean SendRequest(String msg) {
		String line = null;
		switch(msg){
		case "autorize":
			line =  "{\"params\": [\"up101084139.w1\", \"password\"], \"id\": 2, \"method\": \"mining.authorize\"}\n";
			break;
		case "subscribe":
			line = "{\"id\": 1, \"method\": \"mining.subscribe\", \"params\": []}\n";
			break;
		case "exit":
			return false;
		default:
			line = "\n";
		}
		try {
			out.write(line.getBytes());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("sended " + line);
		synchronized (ns) {
			ns.notify();			
		}
		return true;
	}
	
	@Override
	public void run() {
		while(running){
			byte[] b = new byte[10000];
			int l = 0;
			try {
				l = in.read(b);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(l > 0) BlockManager.init().pushBlock(b);
			/*System.out.println("accept: b" + l);
			System.out.println(new String(b));*/
			if(l == -1)
			synchronized (this) {
				try {
					this.wait(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}	
			}
		}
	}
	
}
