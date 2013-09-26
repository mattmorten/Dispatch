package com.mm.dispatchprogram.annotations;

import static com.mm.concurrency.dispatch.annotation.AnnotationKeyBuilder.all;

import com.mm.concurrency.dispatch.Dispatcher;
import com.mm.concurrency.dispatch.annotation.AnnotationKeyBuilder;
import com.mm.concurrency.dispatch.key.Key;
import com.mm.concurrency.dispatch.receiver.Receiver;

public class Tidyer implements Receiver {

	private Dispatcher dispatcher;

	public void run() {
		System.out.println("Finished all Ingredients. Tidying :-)");
		
		// We're done
		dispatcher.dataReceived(new Key(all(Tidyer.class)));
	}

	public void setDispatcher(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

}
