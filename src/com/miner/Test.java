package com.miner;


public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Test t = new Test();
		t.Speak();
	}
	
	void Speak(){
		String task = "{\"id\":null,\"params\":[1024],\"method\":\"mining.set_difficulty\"}\n{\"id\":null,\"params\":[\"6a904e1\",\"6113c885c59633ae6bc323ba538a70ab6f30eecc03f733fa0000000000000000\",\"01000000010000000000000000000000000000000000000000000000000000000000000000ffffffff4103335d06048ce4665708\",\"2e522cfabe6d6dd1faa12c53354b0ae5a18cdff734a845a5fe04de46b7e33af1c65f46405987fe040000000000000000000000013a141d9a000000001976a91480ad90d403581fa3bf46086a91b2d9d4125db6c188ac00000000\",[\"ff10abfb7b926a3b413f2f74b6f1928ab5b46725d154e229935f9ce5b634f03b\",\"6da7104ecf7cfdb71b061f717d41b675dd66a98df85a46e157a6d6dc0a9eb787\",\"27d84abec366e292f0b37898e58076414e321b622dec85e7d13d3a8d990eb927\",\"3e9a891011e4a1544f04ff682bed3d84ea37c5804f3190f7dea0ff397c4830d4\",\"6cf203f555d176fc4991f1ab91342166788dbfc434f47c797b2a9879321eb14a\",\"ec813971bf282cd2d5e8fdebbfcdd60dfb5e1693cf768ef9f547e1a8cd79f21e\",\"d8450e83cef88bacdf1348d5ee273c5524919f0b322022505ca2e7412503f3f0\",\"12da2825671efc8d93555af8f4fa74268576972e8f9c940cdd094a9cac192645\",\"1f29a0b53c9124e99653da2c03989f5ecdfdd63fefb15462f0298aad66b5bbe4\",\"d37bea176bb93864ebba2558cce7402292ad474a374737e436ab0d194b39855f\",\"6715aa20452609904feabbc246d016126d2a6bbda37ac5f67988124cf2b2344c\",\"b20ab8c782fb1b15414ca645c26525ce908bfbc3032dbe578155e40fea1353f4\"],\"20000001\",\"18059ba0\",\"5766e48c\",false],\"method\":\"mining.notify\"}";
		
		
		
		/*BlockManager bm = BlockManager.init();
		task = "{\"id\":2,\"result\":true,\"error\":null}";
		bm.pushBlock(task.getBytes());
		task = "{\"id\":null,\"params\":[1024],\"method\":\"mining.set_difficulty\"}\n{\"id\":null,\"params\":[\"-374af604\",\"0e578e3590738a7f4b969b9c984f0f204abbc34803557e410000000000000000\",\"01000000010000000000000000000000000000000000000000000000000000000000000000ffffffff4103366306049bc8745708\",\"2e522cfabe6d6daec0333ba0ad49d8e81d88ee905bd4fec7f5eead85bf0a4bae7a59a6d9be6b970400000000000000000000000185180199000000001976a91480ad90d403581fa3bf46086a91b2d9d4125db6c188ac00000000\",[\"c9a8e3da0f6540bb11b7c99b3886c3635272050d2cdae6dba86e1b4eadaa0519\",\"e259636c4df490d943071e118ba8ffddda06e6c6d6a91db08d7398c65fd6b8c0\",\"fcfa45d0c5d02fc3475aee7d54e644e2922658677cbdcbb1b81935d78edcf0f7\",\"4191a467d7291b4f5c3a9c6fb67c58b051e8c8ae2003c0e869526d0079ae134b\",\"7c03f16407b846757366edbc83df4be11df6501336fb628468665ad35661c428\",\"490aaeffc148e95e022fa30f871dcd831eed2ccc5880ab095bbfff1b846678b7\",\"2ef871e8d2518d46f45e9cfff2be9f34cf7824b6d606888e8d8fb7e21385f07d\",\"43e18df69e8cd06f1408548432ec88d35ed851b8d26896d37d7d510dfb45471a\",\"a8ee041e002dc1ed381c54a9b1c1614efa23556bed4335e1040ab78be92e02f3\",\"0635fa9bef738bc9b59e20abb6a823f817ee693a97ade50400548d1730dfbe9b\",\"8bc14dd1345be5fb590c5b42d7364616252a0cc2e09a0e49962044ab3be37b3f\",\"fab5ffa8ae9ed63ca980635511340dee26845112a13a3767f4b128b1e5d93fdc\"],\"20000001\",\"18053fd6\",\"5774c89b\",false],\"method\":\"mining.notify\"}";
		bm.pushBlock(task.getBytes());
		task = "{\"id\":1,\"result\":[[[\"mining.set_difficulty\",\"b4b6693b72a50c7116db18d6497cac52\"],[\"mining.notify\",\"ae6812eb4cd7735a302a8a9dd95cf71f\"]],\"0fd80120\",4],\"error\":null}";
		bm.pushBlock(task.getBytes());
		System.out.println(bm.Hash());*/
		
		/*AddBlock addBlock = new AddBlock();
		System.out.println(addBlock.getTarget("1", "1"));
		HelperBlock block = new HelperBlock();
		System.out.println(block.getTarget("1"));
		System.out.println(block.getTarget("32"));*/

		/*HelperBlock block = new HelperBlock();
		long l = new Long("0");
		while(true){
			System.out.println(l);
			System.out.println(block.toHex_x8(l));
			if(l >= 4294967295l) break;
			++l;
		}*/
		
		//testfor look what block algorithm return
		BlockManager bm = BlockManager.init();
		task = "{\"id\":0,\"result\":[[[\"mining.set_difficulty\",\"b4b6693b72a50c7116db18d6497cac52\"],[\"mining.notify\",\"ae6812eb4cd7735a302a8a9dd95cf71f\"]],\"f53d0211\",4],\"error\":null}\n";
		bm.pushBlock(task.getBytes());
		task = "{\"id\":1,\"result\":true,\"error\":null}\n";
		bm.pushBlock(task.getBytes());
		task = "{\"id\":null,\"params\":[4],\"method\":\"mining.set_difficulty\"}\n";
		bm.pushBlock(task.getBytes());
		task = "{\"id\":null,\"params\":[\"-4fb1d548\",\"db48fff805d2f2be98c101b2103afcd3fc0d2f2803bf92a30000000000000000\",\"01000000010000000000000000000000000000000000000000000000000000000000000000ffffffff410353950604f8b0e75708\",\"2e522cfabe6d6d3555ed13d9e4c9317fc507a9080eeef9cb14b591b0cc1636caff691168d3f24704000000000000000000000001e5129d4a000000001976a91480ad90d403581fa3bf46086a91b2d9d4125db6c188ac00000000\",[\"7c60191525d8563e379a4049e44d77be2fecdb348eaf844c86d434a0cad8d75c\",\"ae757ce8f99307303fd31b81b1672674523e84cebab7d4f5fc76c97c46fe6f87\",\"6ab95b94b7970237331551e2f22097432863ee31916d9c80fa2251739d6eb20d\",\"332eec53c5f131c6abe85d64f928151d072cae47f8477d417185b84ec2904a56\",\"3c2f9fdbcca3f01cc8ca2f8eee445271617f10889c39d5644399865fe6dc8a1d\",\"b06350717cd3e844ed5d605a4d7a5f25bf859beeb2bf1aab4e2bf7fa45ee5954\",\"7a1f44163fe18591b2c50b1499b8f09f519e9cd18d1fda14be0e87a029cda741\"],\"20000000\",\"18048ed4\",\"57e7b0f8\",true],\"method\":\"mining.notify\"}\n";
		bm.pushBlock(task.getBytes());
		//answer {"params": ["up101084139.cgminertest", "-4fb1d548", "01000000", "57e7b0f8", "2536d114"], "id": 2, "method": "mining.submit"}
		task = Boolean.toString(bm.ControllTest("2536d114", "01000000"));
		System.out.println(task);
		
	}

}
