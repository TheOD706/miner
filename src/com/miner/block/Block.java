package com.miner.block;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Block {

	public String difficulty;
	public String job_id;
	public String prevhash;
	public String coinb1;
	public String coinb2;
	public String[] merkle_branch;
	public String version;
	public String nbits;
	public String ntime;
	public boolean clean_jobs;
	public String extranonce1;
	public String extranonce2_size;
	public String extra_diff;
	public String extra_notif;
	public String nonce;
	public String extranonce2;
	public boolean ready;
	private long exnonce2_number;
	
	public Block(){
		ready = false;
		difficulty = "1";
		reset();
	}
	
	public void reset(){
		exnonce2_number = -1L;
	}
	
	public byte[] sha256(byte[] msg) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
        md.update(msg);
        return md.digest();
	}
	
	private byte[] generateCoinbase(String en2){
		byte[] cb1b = unhexlify(coinb1);
		byte[] en1b = unhexlify(extranonce1);
		byte[] en2b = unhexlify(en2);
		byte[] cb2b = unhexlify(coinb2);
		ByteBuffer bb = ByteBuffer.allocate(cb1b.length + en1b.length + en2b.length + cb2b.length);
		bb.put(cb1b);
		bb.put(en1b);
		bb.put(en2b);
		bb.put(cb2b);
		return bb.array();
	}
	
	public byte[] Merkle2(byte[] cb) {
		byte[] mr = cb;
		for(int i = 0; i < merkle_branch.length; ++i)
			mr = sha256d(concatByteArray(mr, unhexlify(merkle_branch[i])));
		return mr;
	}
	
	public byte[] concatByteArray(byte[] a, byte[] b){
		ByteBuffer bb = ByteBuffer.allocate(a.length + b.length);
		bb.put(a);
		bb.put(b);
		return bb.array();
	}
	
	public byte[] unhexlify(String s){
		//return new BigInteger(s, 16).toByteArray();
		int n = s.length() / 2;
		byte[] ba = new byte[n]; 
		for(int i = 0; i < n; ++i){
			ba[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16);
		}
		return ba;
	}
	
	public String hexlify(byte[] s){
		final char[] hexArray = "0123456789abcdef".toCharArray();
		char[] hexChars = new char[s.length * 2];
        for ( int j = 0; j < s.length; j++ ) {
            int v = s[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        String ret = new String(hexChars);
        return ret;
	}

	public byte[] swap(String string) {
		byte[] s_bin = unhexlify(string);
		byte[] bin = new byte[s_bin.length];
		int r = bin.length - 1;
		for(int i = 0; i < s_bin.length; ++i, --r)
			bin[r] = s_bin[i];
		return bin;
	}

	public byte[] swap(byte[] s_bin) {
		byte[] bin = new byte[s_bin.length];
		int r = bin.length - 1;
		for(int i = 0; i < s_bin.length; ++i, --r)
			bin[r] = s_bin[i];
		return bin;
	}

	public byte[] swaps(String string) {
		byte[] bin = unhexlify(string);
		if(bin.length % 4 != 0) return null;
		int n = bin.length;
		byte[] result = new byte[n];
		for(int i = 0; i < n; ++i){
			result[i + 3 - 2 * (i % 4)] = bin[i];
		}
		return result;
	}

	public boolean check(String nonce2, String en2) {
		byte[] coinbase = sha256d(generateCoinbase(en2));
		byte[] merkle = Merkle2(coinbase);
		byte[] v = swap(version);
		byte[] p = swaps(prevhash);
		byte[] t = swap(ntime);
		byte[] b = swap(nbits);
		byte[] header_prefix_bin = concatByteArray(v, concatByteArray(p, concatByteArray(merkle, concatByteArray(t, b))));
		System.out.println(hexlify(header_prefix_bin));
		byte[] nonce_bin = swap(nonce2);
		String hash = hexlify(swap(sha256d(concatByteArray(header_prefix_bin, nonce_bin))));
		System.out.println(hash);
		System.out.println(Target());
		return hash.compareTo(Target()) < 0;
	}

	private byte[] sha256d(byte[] ba) {
		return sha256(sha256(ba)); 
	}
	
	public String Target(){
		BigInteger bi = new BigInteger("00000000ffff0000000000000000000000000000000000000000000000000000", 16);
		bi = bi.divide(new BigInteger(difficulty));
		String s = bi.toString(16);
		while(s.length() < 64){
			s = "0" + s;
		}
		return s;		
	}

	public boolean searchBlock() {
		while(nextEN()){
			String nonce_string = runBlock(extranonce2);
			if(nonce_string != null) {
				nonce = nonce_string;
				return true;
			}
		}
		return false;
	}

	private String runBlock(String extranonce22) {
		byte[] coinbase = sha256d(generateCoinbase(extranonce22));
		byte[] merkle = Merkle2(coinbase);
		byte[] v = swap(version);
		byte[] p = swaps(prevhash);
		byte[] t = swap(ntime);
		byte[] b = swap(nbits);
		byte[] header_prefix_bin = concatByteArray(v, concatByteArray(p, concatByteArray(merkle, concatByteArray(t, b))));
		String target = Target();
		long NonceH1 = 0;
		while (true) {
			String NONCEh1 = Long.toHexString(NonceH1);
			if(NONCEh1.length() > 4) break;
			else{
				while (NONCEh1.length() < 4) NONCEh1 = "0" + NONCEh1;
				String NONCEh2 = findMinNonce(hexlify(header_prefix_bin), NONCEh1);
				byte[] nonce_bin = swap(NONCEh1 + NONCEh2);
				String hash = hexlify(swap(sha256d(concatByteArray(header_prefix_bin, nonce_bin))));
				if(hash.compareTo(target) < 0) return NONCEh1 + NONCEh2;
			}
			++NonceH1;
		}
		return null;
	}

	private String findMinNonce(String header, String NON1) {
		long non2 = 0;
		long res = -1;
		String target = "ffff";
		while(true){
			String NON2 = Long.toHexString(non2);
			if(NON2.length() > 4) break;
			else{
				while(NON2.length() < 4) NON2 = "0" + NON2;
				byte[] nonce_bin = swap(NON1 + NON2);
				String hash = hexlify(swap(sha256d(concatByteArray(unhexlify(header), nonce_bin))));
				if(hash.compareTo(target) < 0) {
					res = non2;
					target = hash;
				}
			}
		}
		String nn2 = Long.toHexString(res);
		while(nn2.length() < 4) nn2 = "0" + nn2;
		return nn2;
	}

	private boolean nextEN() {
		++exnonce2_number;
		extranonce2 = Long.toHexString(exnonce2_number);
		int size = Integer.parseInt(extranonce2_size) * 2;
		if(extranonce2.length() > size) return false;
		while (extranonce2.length() < size) {
			extranonce2 = "0" + extranonce2;			
		}
		extranonce2 = hexlify(swap(extranonce2));
		return true;
	}

}
