package com.miner.block;

import java.math.BigInteger;

public class HelperBlock {

	
	public String getTarget(String diff) {
		BigInteger bi = new BigInteger("00000000ffff0000000000000000000000000000000000000000000000000000", 16);
		bi = bi.divide(new BigInteger(diff));
		String s = bi.toString(16);
		while(s.length() < 64){
			s = "0" + s;
		}
		return s;
	}
	
	public String toHex_x8(long l) {
		String s = Long.toHexString(l);
		while(s.length() < 8){
			s = "0" + s;
		}
		return s;
	}

	public String reverse(String string) {
		int n = string.length() / 2;
		StringBuilder sb = new StringBuilder();
		for(int i = (n - 1) * 2; n >= 0; n-=2)
			sb.append(string.substring(i, i + 1));
		return sb.toString();
	}

	public String reverse8(String string) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < string.length(); i+=8){
			int end = Math.min(i + 7, string.length() - 1);
			sb.append(reverse(string.substring(i, end)));
		}
		return sb.toString();
	}
	
	
}
