package simpledb.buffer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import simpledb.file.*;
import simpledb.server.SimpleDB;

/**
 * Manages the pinning and unpinning of buffers to blocks.
 * @author Edward Sciore
 *
 */
public class BasicBufferMgr {

   private int numAvailable;
   // replaced the existing bufferPool which is an array with a map
   public static HashMap<Block,Buffer> hashBufferPool;
   private int bufferPoolSize;
   private HashMap<Block,Long[]> HIST;
   private HashMap<Block,Long> LAST;
   private int LRU_K = SimpleDB.LRU_K;
   // default is LRU-3
   
   private int correlatedTimePeriod = 10;
   
   /**
    * Creates a buffer manager having the specified number 
    * of buffer slots.
    * This constructor depends on both the {@link FileMgr} and
    * {@link simpledb.log.LogMgr LogMgr} objects 
    * that it gets from the class
    * {@link simpledb.server.SimpleDB}.
    * Those objects are created during system initialization.
    * Thus this constructor cannot be called until 
    * {@link simpledb.server.SimpleDB#initFileAndLogMgr(String)} or
    * is called first.
    * @param numbuffs the number of buffer slots to allocate
    * 
    * user note: all the datastructures are initialized here
    */
   BasicBufferMgr(int numbuffs) {
	  numAvailable = numbuffs;
	  bufferPoolSize = numbuffs;
      hashBufferPool = new LinkedHashMap<Block, Buffer>(numbuffs);
      HIST = new HashMap<Block, Long[]>();
      LAST = new HashMap<Block, Long>();
   }
   
   /**
    * 
    * @param numbuffs
    * @param lruK
    * 
    * this constructor takes as parameter the type of LRU-K algorithm being applied while choosing an unpinned buffer
    */
   public BasicBufferMgr(int numbuffs, int lruK){
	   this(numbuffs);
	   LRU_K = lruK;
   }
   
   /**
    * Flushes the dirty buffers modified by the specified transaction.
    * @param txnum the transaction's id number
    */
   synchronized void flushAll(int txnum) {
      Iterator<Entry<Block,Buffer>> iter = hashBufferPool.entrySet().iterator();
      while(iter.hasNext()){
    	  Entry<Block,Buffer> entry = iter.next();
    	  Buffer buff = entry.getValue();
    	  if (buff.isModifiedBy(txnum))
    	         buff.flush();
      }
   }
   
   /**
    * 
    * @param blk
    * @param time
    * @return
    * 
    * this pin method takes as an input parameter, the time at which a buffer is being pinned.
    * If this parameter is not being sent by the user, then by default the current time in milliseconds is considered  
    */
	synchronized Buffer pin(Block blk, long time) {
		
		System.out.println("------------------------------------");
		System.out.println("need to pin the buffer ->"+blk+" at time ->"+time);
		Buffer buff = findExistingBufferUsingHash(blk);
		if (buff == null) {
			System.out.println("there is no existing buffer. choosing an unpinned buffer now.");
			buff = chooseUnpinnedBuffer(time);

			if (buff == null) {
				return null;
			}

			System.out.println("replacement strategy -> " + buff.block() + " will be replaced by the block" + blk);
			hashBufferPool.remove(buff.block());

			buff.assignToBlock(blk);
		}
		if (!buff.isPinned())
			numAvailable--;
		buff.pin();

		// add the buffer to the buffer pool
		hashBufferPool.put(blk, buff);

		long current = time;
		if (LAST.containsKey(blk)) {
			long last = LAST.get(blk);
			// checking for the correlation time period condition
			if (current - last > correlatedTimePeriod) {
				Long[] history = HIST.get(blk);
				shiftValues(history, time);
				HIST.put(blk, history);
			}
		} else {
			Long[] history = initializeHistory(time);
			HIST.put(blk, history);
		}
		LAST.put(blk, current);

		return buff;
	}
   
   /**
    * Pins a buffer to the specified block. 
    * If there is already a buffer assigned to that block
    * then that buffer is used;  
    * otherwise, an unpinned buffer from the pool is chosen.
    * Returns a null value if there are no available buffers.
    * @param blk a reference to a disk block
    * @return the pinned buffer
    * 
    * user note: the handle is passed on to the pin method above where time is taken as a parameter
    */
	synchronized Buffer pin(Block blk){
		return pin(blk,System.currentTimeMillis());
	}

	/**
	 * 
	 * @param time
	 * @return
	 * 
	 * History to the LRU-K algorithm is initialized based on the 'K' value. Most recent entry goes to the 0th index, while the last entry can be
	 * retrieved from the 'k-1' index
	 */
	private Long[] initializeHistory(long time) {
		Long[] history = new Long[LRU_K];
		for (int i = 1; i < LRU_K; i++) {
			history[i] = (long) -1;
		}
		history[0] = time;
		return history;
	}

   /**
    * 	
    * @param history
    * @param time
    * 
    * shifts all values from the history array by one index
    */
   void shiftValues(Long[] history, long time){
	   long prev = history[0];
	   history[0] = time;
	   for(int i=1; i<LRU_K; i++){
		   long temp = prev;
		   prev = history[i];
		   history[i] = temp;
	   }
   }
   
   /**
    * Allocates a new block in the specified file, and
    * pins a buffer to it. 
    * Returns null (without allocating the block) if 
    * there are no available buffers.
    * @param filename the name of the file
    * @param fmtr a pageformatter object, used to format the new block
    * @return the pinned buffer
    */
   synchronized Buffer pinNew(String filename, PageFormatter fmtr) {
      Buffer buff = chooseUnpinnedBuffer(System.currentTimeMillis());
      if (buff == null)
         return null;
      buff.assignToNew(filename, fmtr);
      numAvailable--;
      buff.pin();
      return buff;
   }
   
   /**
    * Unpins the specified buffer.
    * @param buff the buffer to be unpinned
    */
   synchronized void unpin(Buffer buff) {
      buff.unpin();
      if (!buff.isPinned())
         numAvailable++;
   }
   
   /**
    * Returns the number of available (i.e. unpinned) buffers.
    * @return the number of available buffers
    */
   int available() {
      return numAvailable;
   }
   
   /**
    * 
    * @param blk
    * @return
    * 
    * replaced the sequential search algorithm with Hashing technique. If a particular block does not exist, the bufferpool 
    * will return a null value. If it exists then the reference corresponding buffer is returned.
    * 
    * Cost of Operation : Average Case : O(1)
    * worst case: O(n)
    */
   public static Buffer findExistingBufferUsingHash(Block blk) {
		  return hashBufferPool.get(blk);
   }

   /**
    * 
    * @param time
    * @return
    * 
    * Chooses an unpinned buffer based on the LRU-K algorithm. 
    */
   private Buffer chooseUnpinnedBuffer(long time) {

	   	  System.out.println("choosing a buffer to pin");
	   	  
	   	  if(hashBufferPool.size() < bufferPoolSize){
	   		  System.out.println("returning a new buffer as there is still space in the buffer pool.");
			  return new Buffer();
		  }
		  else{
			  long current = time;
			  long min = time;
			  
			  Iterator<Entry<Block,Buffer>> iter = hashBufferPool.entrySet().iterator();
			  Buffer toReturn = null;
		      while(iter.hasNext()){
		    	  Entry<Block,Buffer> entry = iter.next();
		    	  Buffer buff = entry.getValue();
		    	  
		    	  long last = LAST.get(buff.block());
		    	  long histLast = getLastAccessed(buff.block());
		    	  
		    	  if (!buff.isPinned() && (current - last) > correlatedTimePeriod && histLast < min){
		    		  // finding the buffer with the minimum entry (max backward distance)
		    		  min = histLast;
		    		  toReturn = buff;
		    	  }
		      }
		      
		      if(toReturn != null){
		    	  System.out.println("buffer choosen to replace (Victim) ->"+toReturn.block().fileName());
		      }
		      else{
		    	  System.out.println("all buffers are pinned. shit is gonna happen now.");
		      }
		      
		      return toReturn;
		  }
	   }

    /**
     * 
     * @param block
     * @return
     * 
     * returns the value at the 'k-1' index in the lru-k algorithm. Also prints the history for the block being accessed. 
     * Also prints the entry in the LAST data structure.
     */
	private long getLastAccessed(Block block) {
		Long[] history = HIST.get(block);
		String histValues = "";
		for (int i = LRU_K - 1; i >= 0; i--) {
			histValues+="|"+history[i];
		}
		System.out.println("history value for "+block.fileName()+"->["+histValues+"], Last value is ->"+LAST.get(block));
		return history[LRU_K-1];
	}
	
	/**
	 * prints all the blocks present in the buffer pool
	 */
	public static void printBufferPoolBlocks(){
		 Iterator<Entry<Block,Buffer>> iter = hashBufferPool.entrySet().iterator();
	      while(iter.hasNext()){
	    	  Entry<Block,Buffer> entry = iter.next();
	    		  System.out.print(entry.getKey().fileName()+" ");
	      } 
	      System.out.println();
	}
}
