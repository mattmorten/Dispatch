package com.mm.concurrency.cursor;

import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;

import com.mm.concurrency.cursor.strategy.AdaptiveStrategy;
import com.mm.concurrency.cursor.strategy.FetchStrategy;

public class AsynchronousCursor<T> implements Cursor<T> {

	private ArrayBlockingQueue<T> queue;
	private Runnable worker;
	private FetchStrategy<T> strategy;
	private boolean finished = false;
	private boolean fresh = true;
	
	private CountDownLatch latch = new CountDownLatch(1);
	
	
	public AsynchronousCursor(int batchSize, Factory<T> factory) {
		
		this.strategy = new AdaptiveStrategy<T>(Math.round((batchSize - 1)  / 2), batchSize, factory);
		queue = new ArrayBlockingQueue<T>(batchSize * 2);
		
		worker = new Worker();
		new Thread(worker).start();
		
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public T next() {
		strategy.readStart();
		fresh = false;
		T item = null;
		try {
			if (finished){
				item = queue.poll();
			} 
			else {
				item = queue.take();
			}
		} catch (InterruptedException e) {}
		synchronized (worker){
			worker.notifyAll();
		}
		strategy.readEnd();
		return item;
	}
	
	public boolean hasNext() {
		boolean available = queue.peek() != null;
		
		if (available){
			return true;
		}
		
		if (!available && finished){
			return false;
		}
		
		// Is the worker still going? If so, wait for it
		try {
			System.out.println("Waiting on worker...");
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		strategy.delay(1000);
		
		System.out.println("Resuming after worker.");
		
		return queue.peek() != null;
	}
	
	public void prefetch() {
		// Do nothing - we will have already prefetched.
	}
	
	public void remove() {
		
	}
	
	private class Worker implements Runnable {

		public void run() {
			while (!finished){
				if (strategy.shouldFetch()){
					System.out.println("Fetching....");
					Iterator<T> items = strategy.fetch();
					System.out.println("Fetched");
					if (!items.hasNext()){
						// We're all out!
						finished = true;
						System.out.println("All out");
						latch.countDown();
						break;
					}
					
					while(items.hasNext()){
						try {
							queue.put(items.next());
						} catch (InterruptedException e) {}
					}
					
					latch.countDown();
				}
				
				synchronized (this){
					try {
						this.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				latch = new CountDownLatch(1);
			}
			System.out.println("Finished.");
		}
		
	}

	public boolean isFresh() {
		return fresh;
	}
}

