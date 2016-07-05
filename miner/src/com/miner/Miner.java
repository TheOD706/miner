package com.miner;

public class Miner {

	public static void main(String[] args) {
		System.out.println("miner start");
		NetSpeaker speaker = new NetSpeaker();
		speaker.speak();
		System.out.println("miner finish");
	}

}
