package com.miner.block;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import com.miner.block.HelperBlock;

public class Block {

	private static final Object Extranonce1 = null;
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
	private int extranonce2Size;
	public String nonce;
	public String extranonce2;
	public boolean ready;
	
	private AddBlock addBlock;
	private HelperBlock helperBlock;
	
	public Block(){
		addBlock = new AddBlock();
		helperBlock = new HelperBlock();
		ready = false;
		difficulty = "1";
	}
	
	public String sha256(String msg) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
        md.update(msg.getBytes());
        byte byteData[] = md.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
	}
	
	public String Merkle() {
		if(merkle_branch == null) return "";
		if(merkle_branch.length < 1){
			return "";
		} else {
			int n = 1;
			for(; n < merkle_branch.length; n *= 2);
			String[] mrk = new String[n];
			for(int i = 0; i < n; ++i) mrk[i] = sha256(merkle_branch[Math.min(merkle_branch.length - 1, i)]);
			while(n > 1){
				int m = n / 2;
				for(int i = 0; i < m; ++i){
					mrk[i] = sha256(mrk[2 * i] + mrk[2 * i + 1]);
				}
				n = m;
			}
			return mrk[0];
		}		
	}
	private String generatedExtranonce2(String job_id, int size, boolean erase){
		// for example only  dirt way to generate a unique extranonce for each job id.
		//String answer = job_id;
		String answer = "1";
		while (answer.length() < size * 2){
			answer += "0" + answer;
		}
		while (answer.length() > size * 2){
			answer = answer.substring(1);
		}
		extranonce2 = answer;
		return answer;
	}
	
	// Funciton to generate the coinbase
	private String generateCoinbase(){
		extranonce2Size = Integer.parseInt(extranonce2_size);
		StringBuilder coinbase = new StringBuilder();
		coinbase.append(coinb1);
		coinbase.append(Extranonce1);
		coinbase.append(generatedExtranonce2(job_id, extranonce2Size, clean_jobs));
		coinbase.append(coinb2);
		return coinbase.toString();
	}
	
	private String generateCoinbase(String en2){
		StringBuilder coinbase = new StringBuilder();
		coinbase.append(coinb1);
		coinbase.append(Extranonce1);
		coinbase.append(en2);
		coinbase.append(coinb2);
		return coinbase.toString();
	}
	
	public String Merkle2(String cb) {
		String mr = cb;
		for(int i = 0; i < merkle_branch.length; ++i)
			mr = sha256(sha256(mr + merkle_branch[i]));
		/*int n = mr.length() / 2;
		StringBuilder sb = new StringBuilder();
		for(int i = 2 * (n - 1); i >= 0; i -= 2){
			sb.append(mr.substring(i, i + 2));
		}
		return sb.toString();*/
		return mr;
	}
	
	public boolean Block2(){
		String target = helperBlock.getTarget(difficulty);
		String header = version + prevhash + Merkle2(generateCoinbase()) + ntime + nbits;
		long starttime = new Date().getTime();
		long l = 0;
		while(true){
			nonce = helperBlock.toHex_x8(l);
			String hash = sha256(header + nonce);
			if(target.compareToIgnoreCase(hash) > 0) {
				ntime = Long.toHexString(Long.parseLong(ntime, 16) + ((new Date().getTime() - starttime) / 1000));
				return true;
			}
			if(nonce.endsWith("0000")) System.out.println("mining work ... " + nonce);
			if(l >= 4294967295L) break;
			++l;
		}
		return false;
	}
	
	public boolean Block3() {
		extranonce2Size = Integer.parseInt(extranonce2_size);
		generatedExtranonce2(job_id, extranonce2Size, clean_jobs);
		BigInteger target = addBlock.getTarget(extra_diff, difficulty);
		long nonce = 0L;
		BigInteger h = addBlock.getHash(version, prevhash, Merkle(), ntime, nonce);
		while(h.compareTo(target) < 0){
			h = addBlock.getHash(version, prevhash, Merkle(), ntime, nonce);
			++nonce;
		}
		return true;
	}

	public String testControll(String nonce2, String en2) {
		String coinbase = sha256(sha256(generateCoinbase(en2)));
		String merkle = Merkle2(coinbase);
		String v = helperBlock.reverse(version);
		String p = helperBlock.reverse8(prevhash);
		String t = helperBlock.reverse(ntime);
		String b = helperBlock.reverse(nbits);
		String header = v + p + merkle + t + b;
		String hash = sha256(sha256(header + helperBlock.reverse(nonce2)));
		return hash;
	}

}
