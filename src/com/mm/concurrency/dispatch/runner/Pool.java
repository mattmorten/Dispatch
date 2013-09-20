package com.mm.concurrency.dispatch.runner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.mm.concurrency.dispatch.receiver.Receiver;

public class Pool implements Runner {

	private ExecutorService pool = Executors.newFixedThreadPool(5);
	
	
	public void dataReady(Receiver receiver) {
		pool.submit(receiver);
	}
	
}
