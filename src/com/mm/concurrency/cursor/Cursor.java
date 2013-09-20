package com.mm.concurrency.cursor;

import java.util.Iterator;

public interface Cursor<T> extends Iterator<T> {
	
	public boolean isFresh();
	public void prefetch();
}
