package simpledb.test;

import simpledb.buffer.Buffer;
import simpledb.buffer.BufferMgr;
import simpledb.file.Block;
import simpledb.server.SimpleDB;

public class SampleTest {

	public static void main(String[] args) {

		SimpleDB.initFileLogAndBufferMgr("bufferSearchTest");
		int numOfBuffers = 300;
		BufferMgr bf = new BufferMgr(numOfBuffers);
		Block[] blocks = new Block[numOfBuffers];
		Buffer[] buffers = new Buffer[numOfBuffers];
		
		for(int i=0; i<numOfBuffers; i++){
			blocks[i] = new Block("bufferSearch_"+i, (i+1));
			buffers[i] = bf.pin(blocks[i]);
		}
		
		for(int i=0; i<numOfBuffers; i++){
			bf.pin(blocks[i]);
		}
		
		Block b = new Block("bufferSearch_"+numOfBuffers, (numOfBuffers+1));
		// bf.pin(b);
		
		bf.unpin(buffers[0]);
		bf.unpin(buffers[0]);
		
		bf.pin(b);
	}
}
