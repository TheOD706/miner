package com.miner;

import java.util.ArrayList;
import java.util.HashMap;

public class BlockManager {
	
	private static BlockManager bm = null;
	private Block block;
	
	private BlockManager() {
		block = new Block();
	}
	
	public static BlockManager init() {
		if(bm == null) bm = new BlockManager();
		return bm;
	}
	
	public void pushBlock(byte[] block){
		String s = new String(block);
		int i1 = s.indexOf('{');
		while(i1 > -1){
			int i2 = s.indexOf('}', i1);
			String s1 = s.substring(i1 + 1, i2).replaceAll("\"", "");
			int n = s1.length();
			int i3 = 0, i4 = 0;	//3 - Bracket count, 4 - start line after comma
			ArrayList<String> as = new ArrayList<String>();
			for(int i = 0; i < n; ++i){
				if(s1.charAt(i) == ',')
					if(i3 == 0) {
						as.add(s1.substring(i4, i));
						i4 = i + 1;
					}
				if(s1.charAt(i) == '[') ++i3;
				if(s1.charAt(i) == ']') --i3;
			}
			as.add(s1.substring(i4));
			ParseBlock(as.toArray(new String[0]));	
			i1 = s.indexOf('{', i2); 
		}
	}

	private void ParseBlock(String[] array) {
		HashMap<String, String> map = new HashMap<String, String>();
		for (String string : array) {
			String[] sa = string.split(":");
			if(sa.length >= 2){
				map.put(sa[0], sa[1]);
			}
		}
		if(map.containsKey("id"))
			if(map.get("id").equals("null")){
				if(map.containsKey("method")){
					switch (map.get("method")){
					case "mining.notify":
						SetNotify(map.get("params"));
						break;
						
					case "mining.set_difficulty":
						SetDifficulty(map.get("params"));
						break;
					
					default:
						break;
					}
				}
			} else {
				if(map.containsKey("result")){
					if(map.get("retult").equals("true")){}
					else{	//parse extranonce
						SetExtranonce(map.get("result"));
					}
				}
				//TODO id != null
			}
		
	}

	private void SetExtranonce(String string) {
		System.out.println(string);
		
		// TODO Auto-generated method stub
		
	}

	private void SetDifficulty(String string) {
		block.difficulty = string.substring(string.indexOf('[') + 1, string.indexOf(']'));
		//System.out.println(block.difficulty);
	}

	private void SetNotify(String string) {
		string = string.substring(string.indexOf('[') + 1, string.lastIndexOf(']'));
		int i1 = 0; //start new string pos after comma
		int i2 = 0; //bracket count
		int i3 = 0; //index in array
		int n = string.length();
		int param_count = 9;
		String[] sa = new String[param_count]; 
		for(int i = 0; i < n; ++i){
			if(string.charAt(i) == ','){
				if(i2 == 0){
					if(i3 < param_count){
						sa[i3] = string.substring(i1, i);
						i1 = i + 1;
						++i3;
					}
				}
			}
			if(string.charAt(i) == '[') ++i2;
			if(string.charAt(i) == ']') --i2;
		}
		if(i3 < param_count){//add last param
			sa[i3] = string.substring(i1);
		}
		block.job_id = sa[0];
		block.prevhash = sa[1];
		block.coinb1 = sa[2];
		block.coinb2 = sa[3];
		block.merkle_branch = sa[4].substring(sa[4].indexOf('[') + 1, sa[4].lastIndexOf(']')).split(",");
		block.version = sa[5];
		block.nbits = sa[6];
		block.ntime = sa[7];
		block.clean_jobs = sa[8].equals("true");
		//for(int i = 0; i < param_count; ++i) System.out.println(sa[i]);
	}

}
