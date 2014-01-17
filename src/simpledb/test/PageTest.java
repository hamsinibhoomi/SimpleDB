package simpledb.test;

import java.util.Date;

import simpledb.file.Block;
import simpledb.file.Page;
import simpledb.server.SimpleDB;

public class PageTest {

	public static void main(String[] args) {

		SimpleDB.initFileLogAndBufferMgr("PageGetterSetterTest");

		//tests the getInt() and setInt() methods
		Page p1 = new Page();
		Block blk = new Block("Test_task3_int", 1);
		int inputInt = 113;
		p1.setInt(0, inputInt);
		p1.write(blk);
		int n = p1.getInt(0);
		System.out.println("inputInt->"+inputInt+", getInt->"+n);
		// 2nd call to the same api to ensure reliability
		n = p1.getInt(0);
		System.out.println("inputInt->"+inputInt+", getInt->"+n);
		
		// tests the getString() and setString() methods
		Page p2 = new Page();
		Block blk2 = new Block("Test_task3_string", 2);
		String inputString = "task 3 of simple db project";
		p2.setString(0, inputString);
		p2.write(blk2);
		String s = p2.getString(0);
		System.out.println("inputString->"+inputString+", getString->"+s);
		// 2nd call to the same api to ensure reliability
		s = p2.getString(0);
		System.out.println("inputString->"+inputString+", getString->"+s);
		
		// tests the getShort() and setShort() methods
		Page p3 = new Page();
		Block blk3 = new Block("Test_task3_short", 3);
		short inputShort = (short) 13;
		p3.setShort(0, inputShort);
		p3.write(blk3);
		short sh = p3.getShort(0);
		System.out.println("inputShort->"+inputShort+", getShort->"+sh);
		// 2nd call to the same api to ensure reliability
		sh = p3.getShort(0);
		System.out.println("inputShort->"+inputShort+", getShort->"+sh);
		
		// tests the getBoolean() and setBoolean() methods
		Page p4 = new Page();
		Block blk4 = new Block("Test_task3_boolean", 4);
		boolean inputBoolean = true;
		p4.setBoolean(0, inputBoolean);
		p4.write(blk4);
		boolean b = p4.getBoolean(0);
		System.out.println("inputBoolean->"+inputBoolean+", getBoolean->"+b);
		// 2nd call to the same api to ensure reliability
		b = p4.getBoolean(0);
		System.out.println("inputBoolean->"+inputBoolean+", getBoolean->"+b);
		
		// tests the getBytes() and setBytes() methods
		Page p5 = new Page();
		Block blk5 = new Block("Test_task3_bytes", 5);
		String bytes = "bytes task 3 api";
		byte[] inputBytes = bytes.getBytes();
		p5.setBytes(0, inputBytes);
		p5.write(blk5);
		byte[] byt = p5.getBytes(0);
		System.out.println("inputBoolean->"+new String(inputBytes)+", getBytes->"+new String(byt));
		// 2nd call to the same api to ensure reliability
		byt = p5.getBytes(0);
		System.out.println("inputBoolean->"+new String(inputBytes)+", getBytes->"+new String(byt));
		
		// tests the getDate() and setDate() methods
		Page p6 = new Page();
		Block blk6 = new Block("Test_task3_date", 6);
		Date inputDate = new Date("Mon Nov 24 18:06:38 EST 2014"); 
		p6.setDate(0, inputDate);
		p6.write(blk6);
		Date d = p6.getDate(0);
		System.out.println("inputDate->"+inputDate.toString()+", getDate->"+d.toString());
		// 2nd call to the same api to ensure reliability
		d = p6.getDate(0);
		System.out.println("inputDate->"+inputDate.toString()+", getDate->"+d.toString());
	}
}