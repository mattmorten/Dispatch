package com.mm.concurrency.cursor.implementations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mm.concurrency.cursor.BatchingCursor;
import com.mm.concurrency.cursor.Cursor;
import com.mm.concurrency.cursor.Cursors;
import com.mm.concurrency.cursor.Factory;

public class GlobalCounter extends BatchingCursor<Long> {
 
	public static Cursor<Long> newCounter(){
		return Cursors.makeThreadSafe(new GlobalCounter());
	}
	
	protected GlobalCounter() {
		super(25, new Factory<Long>(){
			public Iterator<Long> createItems(int position, int numberOfItems) {
				List<Long> numbers = new ArrayList<Long>();
				for (long i = position; i < numberOfItems; i++){
					numbers.add(i);
				}
				return numbers.iterator();
			}
		});
	}

}
