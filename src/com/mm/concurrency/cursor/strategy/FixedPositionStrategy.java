package com.mm.concurrency.cursor.strategy;

import java.util.Iterator;

import com.mm.concurrency.cursor.Factory;

public class FixedPositionStrategy<T> implements FetchStrategy<T> {

	protected int triggerIndex;
	private Factory<T> factory;
	private int position;
	private int batchSize;
	private int maxFetchedPosition;
	
	public FixedPositionStrategy(int triggerIndex, int batchSize, Factory<T> factory) {
		this.triggerIndex = triggerIndex - batchSize;
		this.factory = factory;
		this.batchSize = batchSize;
	}

	public boolean shouldFetch() {
		if (position >= triggerIndex){
			return true;
		}
		return false;
	}
	
	public Iterator<T> fetch() {
		triggerIndex += batchSize;
		Iterator<T> items = factory.createItems(maxFetchedPosition, batchSize);
		maxFetchedPosition += batchSize;
		return items;
	}

	public void readStart() {
	}

	public void readEnd() {
		position++;
	}
	
	public void delay(long millis) {
		
	}

}
