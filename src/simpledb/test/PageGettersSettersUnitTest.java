package simpledb.test;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import simpledb.file.Block;
import simpledb.file.Page;
import simpledb.server.SimpleDB;

/**
 * 
 * @author Sundeep
 *
 * This is a junit test case which tests the the getters/setters api for the 3rd task.
 * You need junit libraries to compile and run this program.
 * If you are running this in eclipse, then it is already taken care of.
 * Another simple java program which prints the contents of the retrieved/set through the api
 * is also inclused in the test package.
 */

public class PageGettersSettersUnitTest {

	@Before
    public void setUp() {
		SimpleDB.initFileLogAndBufferMgr("PageGetterSetterTest");
	}
	
	@Test
	public void testInt() {
		Page p1 = new Page();
		Block blk = new Block("Test_task3_int", 1);
		int inputInt = 113;
		p1.setInt(0, inputInt);
		p1.write(blk);
		int n = p1.getInt(0);
		assertSame("int values should be equal", inputInt, n);
		n = p1.getInt(0);
		assertSame("int values should be equal", inputInt, n);
	}

	@Test
	public void testString() {
		Page p2 = new Page();
		Block blk2 = new Block("Test_task3_string", 2);
		String inputString = "task 3 of simple db project";
		p2.setString(0, inputString);
		p2.write(blk2);
		String s = p2.getString(0);
		assertTrue(inputString.equals(s));
		s = p2.getString(0);
		assertTrue(inputString.equals(s));
	}
	
	@Test
	public void testShort() {
		Page p3 = new Page();
		Block blk3 = new Block("Test_task3_short", 3);
		short inputShort = (short) 13;
		p3.setShort(0, inputShort);
		p3.write(blk3);
		short sh = p3.getShort(0);
		assertSame("int values should be equal", inputShort, sh);
		sh = p3.getShort(0);
		assertSame("int values should be equal", inputShort, sh);
	}
	
	@Test
	public void testBoolean() {
		Page p4 = new Page();
		Block blk4 = new Block("Test_task3_boolean", 4);
		boolean inputBoolean = true;
		p4.setBoolean(0, inputBoolean);
		p4.write(blk4);
		boolean b = p4.getBoolean(0);
		assertSame("int values should be equal", inputBoolean, b);
		b = p4.getBoolean(0);
		assertSame("int values should be equal", inputBoolean, b);
	}
	
	@Test
	public void testBytes() {
		Page p5 = new Page();
		Block blk5 = new Block("Test_task3_bytes", 5);
		String bytes = "bytes task 3 api";
		byte[] inputBytes = bytes.getBytes();
		p5.setBytes(0, inputBytes);
		p5.write(blk5);
		byte[] byt = p5.getBytes(0);
		assertTrue(bytes.equals(new String(byt)));
		byt = p5.getBytes(0);
		assertTrue(bytes.equals(new String(byt)));
	}
	
	@Test
	public void testDate() {
		Page p6 = new Page();
		Block blk6 = new Block("Test_task3_date", 6);
		Date inputDate = new Date("Mon Nov 24 18:06:38 EST 2014"); 
		p6.setDate(0, inputDate);
		p6.write(blk6);
		Date d = p6.getDate(0);
		assertTrue(inputDate.equals(d));
		d = p6.getDate(0);
		assertTrue(inputDate.equals(d));
	}

}
