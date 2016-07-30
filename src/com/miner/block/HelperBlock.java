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
	
	
}
