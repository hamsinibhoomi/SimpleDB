package simpledb.test;

import simpledb.buffer.BasicBufferMgr;
import simpledb.buffer.Buffer;
import simpledb.buffer.BufferMgr;
import simpledb.file.Block;
import simpledb.server.SimpleDB;

public class LRUKTest {

	public static void main(String[] args) {
		SimpleDB.initFileLogAndBufferMgr("lruktest");
		int numOfBuffers = 4;
		BufferMgr buffer = new BufferMgr(numOfBuffers, 2);
		
		
		Block a = new Block("A",1);
		Block b = new Block("B",2);
		Block c = new Block("C",3);
		Block d = new Block("D",4);
		Block e = new Block("E",5);
		Block f = new Block("F",6);
		
		Buffer ba = buffer.pin(a,(long)2);
		buffer.unpin(ba);
		
		Buffer bb = buffer.pin(b,(long)5);
		buffer.unpin(bb);
		
		ba = buffer.pin(a,(long)8);
		buffer.unpin(ba);
		
		bb = buffer.pin(b,(long)16);
		buffer.unpin(bb);
		
		Buffer bc = buffer.pin(c,(long)20);
		buffer.unpin(bc);
		
		Buffer bd = buffer.pin(d,(long)24);
		buffer.unpin(bd);
		
		Buffer be = buffer.pin(e,(long)30);
		buffer.unpin(be);
		
		Buffer bff = buffer.pin(f,(long)40);
		buffer.unpin(bff);
		
		ba = buffer.pin(a,(long)45);
		buffer.unpin(ba);
		
		bc = buffer.pin(c,(long)54);
		buffer.unpin(bc);
		
		bd = buffer.pin(d,(long)70);
		buffer.unpin(bd);
		
		be = buffer.pin(e,(long)80);
		buffer.unpin(be);
		
		bff = buffer.pin(f,(long)100);
		buffer.unpin(bff);
		
		//System.out.println(BasicBufferMgr.hashBufferPool.size());
		System.out.print("buffer contents at the end of the test ->");
		BasicBufferMgr.printBufferPoolBlocks();
	}
}
