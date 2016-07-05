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
			int i3 = 0, i4 = 0;	//3 - brecket count, 4 - start line after comma
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
						SetNotify(map.get("param"));
						break;
						
					case "mining.set_difficulty":
						SetDifficulty(map.get("param"));
						break;
					
					default:
						break;
					}
				}
			} else {
				//TODO id != null
			}
		
	}

}
