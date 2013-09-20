package com.mm.concurrency.cursor.strategy;

import com.mm.concurrency.cursor.Factory;

public class AdaptiveStrategy<T> extends FixedPositionStrategy<T> {

	public AdaptiveStrategy(int triggerIndex, int batchSize, Factory<T> factory) {
		super(triggerIndex, batchSize, factory);
	}
	
	@Override
	public void delay(long millis) {
		super.delay(millis);
		
		// lower the trigger
		System.out.println("Detected delay. Reducing trigger to account");
		triggerIndex = triggerIndex - 2;
	}
}
