package com.mm.concurrency.cursor;

public class Cursors {

	public static <T> Cursor<T> makeThreadSafe(Cursor<T> cursor){
		synchronized (cursor){
			if (!cursor.isFresh()){
				throw new IllegalStateException("cursor isn't fresh");
			}
			
			// By prefetching, we can guarantee that hasNext() is always a read-only operation
			// and therefore we only need to synchronize next();
			cursor.prefetch();
			
			return new ThreadSafeCursor<T>(cursor);
		}
	}
}

class ThreadSafeCursor<T> implements Cursor<T>{
	
	private Cursor<T> compound;
	
	public ThreadSafeCursor(Cursor<T> compound) {
		this.compound = compound;
	}

	public boolean hasNext() {
		return compound.hasNext();
	}

	public synchronized T next() {
		return compound.next();
	}

	public void remove() {
	}

	public boolean isFresh() {
		return compound.isFresh();
	}

	public void prefetch() {
	}
	
}
