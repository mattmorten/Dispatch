package com.mm.concurrency.cursor;

import java.util.Iterator;

public abstract class BatchingCursor<T> implements Cursor<T> {
	protected int position = 0;
	protected int nextRetrievalPosition = 0;
	protected int batchSize = 25;
	protected Iterator<? extends T> currentIterator = null;
	protected Factory<T> factory;
	
	public BatchingCursor(int batchSize, Factory<T> factory) {
		this.batchSize = batchSize;
		this.factory = factory;
	}

	public boolean hasNext() {
		if (position == nextRetrievalPosition){
			currentIterator = getNextBatch(position, batchSize);
			nextRetrievalPosition += batchSize;
		}
		
		return currentIterator.hasNext();
	}

	public T next() {	
		T next = currentIterator.next();
		position++;
		if (position == nextRetrievalPosition){
			currentIterator = getNextBatch(position, batchSize);
			nextRetrievalPosition += batchSize;
		}
		
		return next;
	}

	public void remove() {
		// what goes here?
	};
	
	public final boolean isFresh() {
		return position == 0 && currentIterator == null;
	}
	
	protected Iterator<? extends T> getNextBatch(int startPosition, int batchSize){
		return factory.createItems(startPosition, batchSize);
	}
	
	public void prefetch() {
		if (!isFresh()){
			throw new IllegalStateException("Already used");
		}
		currentIterator = getNextBatch(position, batchSize);
		nextRetrievalPosition += batchSize;
	}
}
