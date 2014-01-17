package simpledb.test;

import simpledb.buffer.BasicBufferMgr;
import simpledb.buffer.Buffer;
import simpledb.buffer.BufferMgr;
import simpledb.file.Block;
import simpledb.server.SimpleDB;

/**
 * 
 * @author Sundeep
 *
 * This test case helps test LRU 2,3,4,5 as there are a lot of entries that we are considering.
 * <b>Borrowed</b> this test case from <b>Madhumathi</b>, a fellow DBMS student, to test the functionality of our LRU_K implementation.
 */
public class LRUKSecondTest {

	static BufferMgr bm;
	static int LRU_K = 3;
	
	public static void main(String[] args) {
		SimpleDB.init("LRUK_Test2");
		bm = new BufferMgr(5, LRU_K);		

		Block blk1 = newBlock("1", 1);
		Block blk2 = newBlock("2", 2);
		Block blk3 = newBlock("3", 3);
		Block blk5 = newBlock("5", 5);
		Block blk7 = newBlock("7", 7);
		Block blk4 = newBlock("4", 4);
		
		pin(blk1, 2);        // 1
		pin(blk2, 6);
		pin(blk3, 9);
		pin(blk2, 12);
		pin(blk5, 16);        // 1
		pin(blk3, 19);
		pin(blk1, 21);        // 1
		pin(blk2, 25);
		pin(blk3, 29);
		pin(blk4, 31);
		pin(blk7, 32);
		pin(blk5, 35);        // 1
		pin(blk7, 36);        // 1
		pin(blk2, 42);
		pin(blk2, 43);
		pin(blk1, 47);
		pin(blk4, 49);		// 1
		pin(blk2, 55);
		pin(blk3, 56);
		pin(blk7, 62);
		pin(blk3, 67);
		pin(blk3, 68);
		pin(blk1, 69);        // 1
		pin(blk2, 70);
		pin(blk5, 75);        // 1
		pin(blk2, 76);
		pin(blk1, 82);        // 1
		pin(blk2, 89);
		pin(blk4, 96);
		pin(blk5, 98);        // 1
		pin(blk7, 108);        // 1
		pin(blk1, 120);
		pin(blk4, 136);
		pin(blk5, 145); // 1
		pin(blk3, 156);
		pin(blk7, 159);  
		pin(blk3, 167);
		pin(blk3, 168);
		pin(blk1, 169);        // 1
		pin(blk2, 170);
		pin(blk2, 176);
		pin(blk1, 182);        // 1
		pin(blk2, 189);
		pin(blk4, 196);
		pin(blk5, 198);        // 1
		pin(blk3, 206);
		pin(blk7, 208);        // 1
		pin(blk1, 220);
		pin(blk2, 249);
		pin(blk4, 256);
		pin(blk5, 268);
		pin(blk5, 277);
		pin(blk7, 289);  
		pin(blk4, 296);
		pin(blk3, 298);        // 1
		pin(blk7, 308);        // 1
		pin(blk1, 320);
		pin(blk2, 389);
		pin(blk4, 396);
		pin(blk5, 398);        // 1
		pin(blk7, 408);        // 1
		pin(blk1, 420);
		pin(blk3, 468);
		pin(blk1, 469);        // 1
		pin(blk2, 570);
		pin(blk2, 576);
		pin(blk1, 582);        // 1
		pin(blk2, 589);
		pin(blk4, 596);
		pin(blk5, 598);        // 1
		pin(blk7, 608);        // 1
		
		BasicBufferMgr.printBufferPoolBlocks();
	}

	private static Block newBlock(String fileName, int blkNum) {
		Block blk = new Block(fileName, blkNum);
		return blk;
	}
	
	static void pin (Block B, Integer n)
	{
		Buffer buf = bm.pin(B, n);
		bm.unpin(buf);
	}
}
