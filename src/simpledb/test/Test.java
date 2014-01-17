package simpledb.test;

import java.util.Date;

public class Test {

	public static void main(String[] args) {
		Long[] arr = new Long[1];
		arr[0] = (long) -1;
		
		if(arr[0] == -1){
			System.out.println("hi");
		}
		else{
			System.out.println("bye");
		}
		
		System.out.println(Boolean.toString(true));
		
		Date d = new Date("Sun Nov 24 18:06:38 EST 2014");
		System.out.println(d.toString());
	}
}
