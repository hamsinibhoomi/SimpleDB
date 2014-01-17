package simpledb.test;

import java.util.Random;

import simpledb.buffer.BasicBufferMgr;
import simpledb.buffer.Buffer;
import simpledb.buffer.BufferMgr;
import simpledb.file.Block;
import simpledb.server.SimpleDB;

public class BufferSearchTest {

	public static void main(String[] args) {

		SimpleDB.initFileLogAndBufferMgr("bufferSearchTest");
		int numOfBuffers = 16384;
		BufferMgr bf = new BufferMgr(numOfBuffers);
		Block[] blocks = new Block[numOfBuffers];
		Buffer[] buffers = new Buffer[numOfBuffers];
		
		for(int i=0; i<numOfBuffers; i++){
			blocks[i] = new Block("bufferSearch_"+i, (i+1));
			buffers[i] = bf.pin(blocks[i]);
		}
		
		int[] runtimes = new int[10];
		Block[] blks = new Block[10];
		Random rand = new Random();
		
		for(int i=0; i<10; i++){
			long start = System.nanoTime();
			int num = rand.nextInt(numOfBuffers-1);
			BasicBufferMgr.findExistingBufferUsingHash(blocks[num]);
			long end = System.nanoTime();
			runtimes[i] = (int) (end - start);
			blks[i] = blocks[num];
		}
		
		for(int i=0; i<10; i++){
			System.out.println(blks[i]+" found in "+runtimes[i]+" nanosecs");
		}
	}
}
