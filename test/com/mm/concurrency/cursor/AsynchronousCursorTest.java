package com.mm.concurrency.cursor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.mm.concurrency.cursor.AsynchronousCursor;
import com.mm.concurrency.cursor.Factory;

public class AsynchronousCursorTest {

	@Test
	public void testCursor() throws Exception {
		Factory<Long> factory = new Factory<Long>(){

			public Iterator<Long> createItems(int position, int numberOfItems) {

				if (position > 200){
					System.out.println("Stopping");
					return Collections.<Long>emptyList().iterator();
				}
				
				if (position > 0){
					try {
						Thread.sleep(5 * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} 
				}
				
				System.out.println("Fetched from " + position + " to " + (position + numberOfItems));
				List<Long> numbers = new ArrayList<Long>();
				for (long i = position; i < position + numberOfItems; i++){
					numbers.add(i);
				}
				return numbers.iterator();
			}
		};
		
		AsynchronousCursor<Long> cursor = new AsynchronousCursor<Long>(30, factory);
		
		while(cursor.hasNext()){
			System.out.println(cursor.next());
			Thread.sleep(200);
		}
	}
}
