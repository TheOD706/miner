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
	public static NetSpeaker ns1, ns2;
	
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
	        ns2 = this;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void speak() {
		//run listener
		ns1 = new NetSpeaker();
		Thread thread = new Thread(ns1);
		thread.start();
		//send request
		synchronized (ns2) {
			try {
				ns1.SendRequest("subscribe");
				ns2.wait(25000);
				ns1.SendRequest("autorize");
				ns2.wait(25000);
				if(!BlockManager.init().ready()) ns2.wait(10000);
				ns1.SendRequest("submit");
				ns2.wait(25000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			while (ns1.SendRequest(keyboard.readLine())) {		}
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
		case "submit":
			String[] s = BlockManager.init().Hash();
			line ="{\"params\": [\"up101084139.w1\", \"" + s[0] + "\", \"" + s[1] + "\", \"" + s[2] + "\", \"" + s[3] + "\"], \"id\": 4, \"method\": \"mining.submit\"}";
			break;
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
		synchronized (ns1) {
			ns1.notify();			
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
			synchronized (ns2) {
				ns2.notify();				
			}
			/*System.out.println("accept: b" + l);*/
			System.out.println("read " + new String(b));
			if(l == -1)
			synchronized (ns1) {
				try {
					ns1.wait(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}	
			}
		}
	}
	
}
